package at.qe.skeleton.services;

import at.qe.skeleton.dto.CardDto;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.ids.UserDeckId;
import at.qe.skeleton.repositories.CardAlgorithmDataRepository;
import at.qe.skeleton.repositories.CardRepository;
import at.qe.skeleton.repositories.DeckRepository;
import at.qe.skeleton.repositories.UserDeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeckService {
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final EmailService emailService;
    private final UserDeckRepository userDeckRepository;
    private final CardAlgorithmDataRepository cardAlgorithmDataRepository;

    @Autowired
    public DeckService(DeckRepository deckRepository, CardRepository cardRepository, EmailService emailService,
                       UserDeckRepository userDeckRepository, CardAlgorithmDataRepository cardAlgorithmDataRepository) {
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
        this.emailService = emailService;
        this.userDeckRepository = userDeckRepository;
        this.cardAlgorithmDataRepository = cardAlgorithmDataRepository;
    }


    public List<Deck> getAllDecks() {
        return deckRepository.findAll();
    }

    public Deck getDeckById(long id) {
        return deckRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("invalid Deck Id"));
    }

    public boolean isPresentByDeckId(long id) {
        return deckRepository.findById(id).isPresent();
    }

    public boolean isPresentByName(String name) {
        return deckRepository.findByName(name).isPresent();
    }

    public void saveNewDeck(Userx user, String name, String description) {
        Deck toSave = deckRepository.save(new Deck(user, name, description));
        userDeckRepository.save(new UserDeck(user, toSave));
    }

    public List<Deck> getPublicDecks() {
        return deckRepository.findByIsPublicTrueAndLockedFalse();
    }

    public void makePublic(Long deckId) {
        Deck deck = getDeckById(deckId);
        deck.setIsPublic(true);
        deckRepository.save(deck);
    }

    public void deleteDeck(Deck deck) {
        deckRepository.delete(deck);
    }

    public void saveCard(long deckId, String question, String solution) {
        cardRepository.save(new Card(getDeckById(deckId), question, solution));
    }

    public void deleteCard(Card card) {
        cardRepository.delete(card);
    }

    public Card getCardById(long id) {
        return cardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("invalid Card Id"));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void lockDeck(Long deckId) {
        Deck deck = getDeckById(deckId);
        deck.setLocked(true);
        deckRepository.save(deck);
        userDeckRepository.deleteAll(userDeckRepository.findByUserDeckIdDeck(deck));

        // extract all the current users of the deck
        Set<Userx> users = deck.getUserDecks()
                .stream()
                .map(UserDeck::getUserDeckId)
                .map(UserDeckId::getUser)
                .collect(Collectors.toSet());

        // notify all the users via Email
        for (Userx u : users) {
            String userMail = u.getEmail();
            if (userMail != null) {
                emailService.sendSimpleEmail(userMail,
                        String.format("Your Deck '%s' got locked", deck.getName()),
                        String.format("Hello %s,\n your Deck '%s' got locked by one of our admins because it violates" +
                                "our policies.", u.getFirstName(), deck.getName()));
            }
        }
    }

    public void editDeck(Long deckId, String deckName, String deckDesc) {
        Deck deck = getDeckById(deckId);
        deck.setName(deckName);
        deck.setDescription(deckDesc);
        deckRepository.save(deck);
    }

    public Collection<Card> findAllCardsOfADeck(Long id) {
        return cardRepository.findAllByDeck(getDeckById(id));
    }

    public void editCard(Long cardId, String question, String solution) {
        Card card = getCardById(cardId);
        card.setQuestion(question);
        card.setSolution(solution);
        cardRepository.save(card);
    }

    //adding public deck to user's decks
    public boolean subscribePublicDeck(Userx user, Deck deck) {
        if (userDeckRepository.existsByUserDeckIdUserAndUserDeckIdDeck(user, deck)) {
            return false;
        }
        Deck newDeck = new Deck(deck, user);
        deckRepository.save(newDeck);
        cardRepository.saveAll(newDeck.getCards());
        userDeckRepository.save(new UserDeck(user, newDeck));
        return true;
    }

    public UserDeck getUserDeck(long deckId, String username) {
        return userDeckRepository.findByUserDeckIdDeckAndUserDeckIdUserUsername(getDeckById(deckId), username);
    }

    public List<UserDeck> getUserDecks(Userx user) {
        updateAll(user);
        return userDeckRepository.findByUserDeckIdUser(user);
    }

    private void updateAll(Userx user) {
        List<UserDeck> userDecks = userDeckRepository.findByUserDeckIdUser(user);
        userDecks.forEach(this::update);
    }

    private void update(UserDeck userDeck) {
        Map<Card, CardAlgorithmData> cardAlgorithmData = userDeck.getCardAlgorithmData();
        for (var card : userDeck.getDeck().getCards()) {
            cardAlgorithmData.putIfAbsent(card, new CardAlgorithmData(card, userDeck));
        }
        cardAlgorithmDataRepository.saveAll(cardAlgorithmData.values());
        userDeck.setNewCards(cardAlgorithmDataRepository.countByCardAlgorithmDataIdUserDeckAndRepetitionsIs(userDeck, 0));
        userDeck.setToBeRepeated(cardAlgorithmDataRepository.countByRepeatDateBeforeAndTodayAndRepeatedAtleastOnce(userDeck));
        userDeckRepository.save(userDeck);
    }

    public StudyIterator getStudyIterator(UserDeck userDeck) {
        return userDeck.getCardAlgorithmData().values().isEmpty() ? null : new StudyIterator(
                userDeck, cardAlgorithmDataRepository.findByRepeatDateBeforeAndToday(userDeck),
                userDeckRepository, cardAlgorithmDataRepository);
    }

    //make deck reverted
    public void inverse(UserDeck userDeck) {
        userDeck.setReverted(!userDeck.isReverted());
        userDeckRepository.save(userDeck);
    }

    //returns reverted cards of a deck
    public Set<CardDto> findAllRevertedCardsOfADeck(Long deckId) {
        Set<CardDto> revertedCards = new HashSet<>();
        findAllCardsOfADeck(deckId).stream().forEach(card -> revertedCards.add(new CardDto(card.getCardId(), card.getSolution(), card.getQuestion())));
        return revertedCards;
    }
}
