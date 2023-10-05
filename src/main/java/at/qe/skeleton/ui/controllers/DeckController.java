package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.dto.CardDto;
import at.qe.skeleton.dto.DeckDto;
import at.qe.skeleton.model.Card;
import at.qe.skeleton.model.Deck;
import at.qe.skeleton.model.StudyIterator;
import at.qe.skeleton.model.UserDeck;
import at.qe.skeleton.services.DeckService;
import at.qe.skeleton.services.UserxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A Controller which handles http requests from front end which are connected with deck operations
 */

@RestController
public class DeckController {
    private DeckService deckService;
    private UserxService userxService;
    private UserDeck userDeck;
    private StudyIterator studyIterator;
    private boolean reverted;

    public DeckController(DeckService deckService, UserxService userxService) {
        this.deckService = deckService;
        this.userxService = userxService;
    }

    /**
     * @return list of all decks of authenticated user
     */
    @GetMapping("/decks")
    public ResponseEntity<List<DeckDto>> getAllDecks(){
        List<Deck> decks = deckService.getUserDecks(userxService.getAuthenticatedUser())
                .stream().map(UserDeck::getDeck).collect(Collectors.toList());
        List<DeckDto> deckDtos = new ArrayList<>();
        decks.stream().forEach(deck -> deckDtos.add(new DeckDto(deck.getDeckId(),deck.getName(),deck.getDescription(),deckService.getUserDeck(deck.getDeckId(), userxService.getCurrentUserName()).getNewCards(),deckService.getUserDeck(deck.getDeckId(), userxService.getCurrentUserName()).getToBeRepeated())));
        return new ResponseEntity<>(deckDtos, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @PostMapping("/decks/{name}/{description}")
    public void addNewDeck(@PathVariable String name, @PathVariable String description){
        deckService.saveNewDeck(userxService.getAuthenticatedUser(), name, description);
    }

    /** finding deck by {is}
     * @param id
     * @return
     * @throws IllegalAccessException if its not user's deck or not public deck
     */
    @GetMapping("/student/deckDetails/{id}")
    public ResponseEntity<Deck> findDeck(@PathVariable Long id) throws IllegalAccessException{
        Deck deck = deckService.getDeckById(id);
        if(!userxService.ifUserOwnsDeck(deck) && !deck.isPublic()){
            throw new IllegalAccessException("U cannot see deck details, this is not your deck nor public one");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(deck);
        }
    }

    /** find all cards or a deck with {id}
     * @param id
     * @return
     * @throws IllegalAccessException if its not user's deck or not public deck
     */
    @GetMapping("/student/cardsOfADeck/{id}")
    public ResponseEntity<Collection<Card>> findAllCardsOfADeck(@PathVariable Long id) throws IllegalAccessException{
        Deck deck = deckService.getDeckById(id);
        if(!userxService.ifUserOwnsDeck(deck) && !deck.isPublic()){
            throw new IllegalAccessException("U cannot see cards of a deck, this is not your deck nor public one");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(deckService.findAllCardsOfADeck(id));
        }
    }

    /**
     * need when deck is in reverted mode
     * @param id
     * @return cards with changes question and solution
     * @throws IllegalAccessException if deck is not user's
     */
    @GetMapping("/student/RevertedCardsOfADeck/{id}")
    public ResponseEntity<Collection<CardDto>> findAllRevertedCardsOfADeck(@PathVariable Long id) throws IllegalAccessException{
        Deck deck = deckService.getDeckById(id);
        if(!userxService.ifUserOwnsDeck(deck) && !deck.isPublic()){
            throw new IllegalAccessException("U cannot see reverted cards of a deck, this is not your deck nor public one");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(deckService.findAllRevertedCardsOfADeck(id));
        }
    }

    /**
     * @return all public decks
     */
    @GetMapping("/publicDecks")
    public List<Deck> getPublicDecks(){
        return deckService.getPublicDecks();
    }


    /**
     * @param id
     * @throws IllegalAccessException if deck is not user's
     */
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @PostMapping("/student/publishDeck/{id}")
    public void publishDeck(@PathVariable Long id) throws IllegalAccessException{
        Deck deck = deckService.getDeckById(id);
        if(userxService.ifUserOwnsDeck(deck)){
            deckService.makePublic(id);
        }else{
            throw new IllegalAccessException("U cannot publish not your deck");
        }
    }

    /**
     * @param id
     * @throws IllegalAccessException if deck is not user's
     */
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @DeleteMapping("/student/deleteDeck/{id}")
    public void deleteDeck(@PathVariable Long id) throws IllegalAccessException{
        Deck deck = deckService.getDeckById(id);
        if(userxService.ifUserOwnsDeck(deck)){
            deckService.deleteDeck(deckService.getDeckById(id));
        }else{
            throw new IllegalAccessException("U cannot delete not your deck");
        }
    }

    /** add new card to deck with {id}
     * @param id
     * @param question
     * @param solution
     * @throws IllegalAccessException if deck is not user's
     */
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @PostMapping("/addNewCard/{id}/{question}/{solution}")
    public void addNewCardToDeck(@PathVariable Long id,@PathVariable String question,@PathVariable String solution) throws IllegalAccessException{
        Deck deck = deckService.getDeckById(id);
        if(userxService.ifUserOwnsDeck(deck)){
            deckService.saveCard(id, question, solution);
        }else{
            throw new IllegalAccessException("U cannot add card to not your deck");
        }
    }

    /** deletes  card to deck with {cardId}
     * @param cardId
     * @throws IllegalAccessException if deck is not user's
     */
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @DeleteMapping("/deleteCardFromDeck/{cardId}")
    public void deleteCardFromDeck(@PathVariable Long cardId) throws IllegalAccessException{
        Deck deck = deckService.getCardById(cardId).getDeck();
        if(userxService.ifUserOwnsDeck(deck)){
            deckService.deleteCard(deckService.getCardById(cardId));
        }else{
            throw new IllegalAccessException("U cannot delete card from not your deck");
        }
    }


    /**
     * for study mode: intialize iterator and recieve first card
     * checking with the first card if the deck is reverted to reuse this value and not check it again with the every next card (rateCardAndNextCard methode)
     *
     * @param deckId
     * @return first card to study. If reverted return reverted card, if no cards to study return empty optional
     * @throws IllegalAccessException if deck is not user's
     */
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/cardToStudy/{deckId}")
    public Optional<CardDto> cardsToStudy(@PathVariable Long deckId) throws IllegalAccessException{
        Deck deck = deckService.getDeckById(deckId);
        if(!userxService.ifUserOwnsDeck(deck)) {
            throw new IllegalAccessException("U cannot study not your deck");
        }
        userDeck = deckService.getUserDeck(deckId,userxService.getCurrentUserName());
        studyIterator = deckService.getStudyIterator(userDeck);
        if(studyIterator.hasNext()) {
            Card card = studyIterator.next();
            if(deckService.getUserDeck(deckId, userxService.getCurrentUserName()).isReverted()) {
                reverted = true;
                return Optional.of(new CardDto(card.getCardId(), card.getSolution(),
                        card.getQuestion(), userDeck.getNewCards(), userDeck.getToBeRepeated()));
            }else {
                reverted = false;
                return Optional.of(new CardDto(card.getCardId(), card.getQuestion(), card.getSolution(), userDeck.getNewCards(), userDeck.getToBeRepeated()));
            }
        }else{
            return Optional.empty();
        }
    }

    /**
     * rating card and recieve next card by reusing the same iterator which was initialized in previous methode
     * as soon as i recieve empty object i print(in frontend) finishing message
     * @param deckId
     * @param cardId
     * @param score
     * @return next card to study
     * @throws IllegalAccessException if deck is not user's
     */
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @PostMapping("/rateCard/{deckId}/{cardId}/{score}")
    public Optional<CardDto> rateCardAndNextCard(@PathVariable Long deckId,@PathVariable Long cardId, @PathVariable String score) throws IllegalAccessException {
        if(!userxService.ifUserOwnsDeck(deckService.getDeckById(deckId))){
            throw new IllegalAccessException("U cannot rate card from not your deck");
        }
        userDeck = deckService.getUserDeck(deckId,userxService.getCurrentUserName()); //if im doing this - # new cards changes, without it changes nothing
        studyIterator.rateCard(deckService.getCardById(cardId),Integer.parseInt(score));
        if(studyIterator.hasNext()) {
            Card card = studyIterator.next();
            if(reverted) {
                return Optional.of(new CardDto(card.getCardId(), card.getSolution(), card.getQuestion(), userDeck.getNewCards(), userDeck.getToBeRepeated()));
            }else {
                return Optional.of(new CardDto(card.getCardId(), card.getQuestion(), card.getSolution(), userDeck.getNewCards(), userDeck.getToBeRepeated()));
            }
        }else{
            return Optional.empty();
        }

    }

    /** edit deck (name and description)
     * @param deckId
     * @param deckName
     * @param deckDesc
     * @throws IllegalAccessException if deck if not user's
     */
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @PostMapping("/editDeck/{deckId}/{deckName}/{deckDesc}")
    public void editDeck(@PathVariable Long deckId, @PathVariable String deckName,@PathVariable String deckDesc) throws IllegalAccessException{
        if(!userxService.ifUserOwnsDeck(deckService.getDeckById(deckId))){
            throw new IllegalAccessException("U cannot edit not your deck");
        }
        deckService.editDeck(deckId,deckName,deckDesc);
    }

    /** adds new card to deck with @param deckId
     * @param deckId
     * @param cardId
     * @param question
     * @param solution
     * @throws IllegalAccessException if deck if not user's
     */
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @PostMapping("/editCard/{deckId}/{cardId}/{question}/{solution}")
    public void editCard(@PathVariable Long deckId,@PathVariable Long cardId, @PathVariable String question,@PathVariable String solution) throws IllegalAccessException{
        if(!userxService.ifUserOwnsDeck(deckService.getDeckById(deckId))){
            throw new IllegalAccessException("U cannot edit not your card");
        }
        if(deckService.getUserDeck(deckId,userxService.getCurrentUserName()).isReverted()) {
            deckService.editCard(cardId, solution, question);
        }else{
            deckService.editCard(cardId, question, solution);
        }
    }

    /** add public deck to user's private decks
     * @param deckId
     * @throws IllegalAccessException if deck is not public
     * @return true in case of success, otherwise false
     */
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @PostMapping("/addToMyDecks/{deckId}")
    public boolean addPublicDeckToMyDecks(@PathVariable Long deckId) throws IllegalAccessException{
        if(!deckService.getDeckById(deckId).isPublic()){throw new IllegalAccessException("U cannot add not public deck");}
        boolean success = deckService.subscribePublicDeck(userxService.getAuthenticatedUser(), deckService.getDeckById(deckId));
        if(success) {
            return true;
        }else {
            return false;
        }
    }

    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/checkIfDeckNameExists/{deckName}")
    public boolean checkIfDeckNameExists(@PathVariable String deckName){
        return deckService.isPresentByName(deckName);
    }

    @PostMapping("/admin/blockDeck/{deckId}")
    public void blockDeck(@PathVariable Long deckId){
        deckService.lockDeck(deckId);
    }

    /** make deck reverted (change all card's solutions and questions)
     * @param deckId
     * @throws IllegalAccessException if deck is not user's
     */
    @PostMapping("/inverseDeck/{deckId}")
    public void inverse(@PathVariable Long deckId) throws IllegalAccessException{
        if(!userxService.ifUserOwnsDeck(deckService.getDeckById(deckId))){
            throw new IllegalAccessException("U cannot revert not your deck");
        }
        deckService.inverse(deckService.getUserDeck(deckId, userxService.getCurrentUserName()));
    }


    /**
     * checks if deck with {deckId} is reverted
     * @param deckId
     * @return true if deck is reverted, otherwise false
     */
    @GetMapping("/checkIfReverted/{deckId}")
    public boolean checkIfReverted(@PathVariable Long deckId){
        Deck deck = deckService.getDeckById(deckId);
        if(deck.isPublic()){
            return deckService.getUserDeck(deckId, deckService.getDeckById(deckId).getCreateUser().getUsername()).isReverted();
        }else {
            return deckService.getUserDeck(deckId, userxService.getCurrentUserName()).isReverted();
        }
    }

    /**
     * @param deckId
     * @return true if deck is public, otherwise false
     */
    @GetMapping("/isDeckPublic/{deckId}")
    public boolean checkIfDeckIsPublic(@PathVariable Long deckId){

        return deckService.getDeckById(deckId).isPublic();
    }

    /**
     * @param deckId
     * @return true if deck belongs to user, otherwise false
     */
    @GetMapping("/isDeckBelongsToUser/{deckId}")
    public boolean isDeckBelongsToUser(@PathVariable Long deckId){
        if(deckService.getUserDeck(deckId, userxService.getCurrentUserName()) != null) {
            return true;
        }
        return false;
    }



}
