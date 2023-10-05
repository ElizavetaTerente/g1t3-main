package at.qe.skeleton.tests;

import at.qe.skeleton.dto.CardDto;
import at.qe.skeleton.dto.DeckDto;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.DeckRepository;
import at.qe.skeleton.services.DeckService;
import at.qe.skeleton.services.UserxService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import at.qe.skeleton.ui.controllers.DeckController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class DeckControllerTest {
    @Autowired
    DeckRepository deckRepository;
    @Autowired
    SessionInfoBean sessionInfoBean;
    @Mock
    DeckService deckService;
    @Mock
    UserxService userxService;
    @Mock
    StudyIterator studyIterator;
    @InjectMocks
    DeckController deckController;

    @Test
    public void testGetAllisNotNull() {
        ResponseEntity<List<DeckDto>> decks = deckController.getAllDecks();
        Assertions.assertNotNull(decks);
    }

    @Test
    public void testAddNewDeck() {
        // setup
        String name = "testDeck";
        String description = "testDescription";
        Userx user = new Userx();
        user.setUsername("testUser");
        when(userxService.getAuthenticatedUser()).thenReturn(user);

        deckController.addNewDeck(name, description);

        // verify
        verify(deckService).saveNewDeck(eq(user), eq(name), eq(description));
    }


    @Test
    public void testFindDeck() throws IllegalAccessException {

        Deck test = deckRepository.findById(1L).orElse(new Deck(1L));
        test.setIsPublic(false);

        when(deckService.getDeckById(1L)).thenReturn(test);
        when(userxService.ifUserOwnsDeck(test)).thenReturn(true);

        ResponseEntity<Deck> deckResponseEntity = deckController.findDeck(1L);
        assertEquals(test, deckResponseEntity.getBody());
    }

    @Test
    public void testFindDeckIfCase() throws IllegalAccessException {

        Deck test = deckRepository.findById(1L).orElse(new Deck(1L));
        test.setIsPublic(true);

        //changed userOwnsDeck to false, so takes isPublic
        when(deckService.getDeckById(1L)).thenReturn(test);
        when(userxService.ifUserOwnsDeck(test)).thenReturn(false);

        ResponseEntity<Deck> deckResponseEntity = deckController.findDeck(1L);
        assertEquals(test, deckResponseEntity.getBody());
    }

    @Test
    public void testFindDeckElseCase() {

        Deck test = deckRepository.findById(1L).orElse(new Deck(1L));
        test.setIsPublic(false);

        //changed userOwnsDeck to false, so takes isPublic
        when(deckService.getDeckById(1L)).thenReturn(test);
        when(userxService.ifUserOwnsDeck(test)).thenReturn(false);

        assertThrows(IllegalAccessException.class, () -> deckController.findDeck(1L));
    }

    @Test
    public void testFindAllCardsOfADeck() throws IllegalAccessException {
        Deck deck = deckRepository.findById(1L).orElse(new Deck(1L));
        Collection<Card> cards = deckService.findAllCardsOfADeck(1L);

        // setup
        deck.setIsPublic(false);
        when(deckService.getDeckById(1L)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(true);
        // check response
        ResponseEntity<Collection<Card>> response = deckController.findAllCardsOfADeck(1L);
        assertEquals(cards, response.getBody());
    }

    @Test
    public void testFindAllCardsOfADeckIfCase() throws IllegalAccessException {
        Deck deck = deckRepository.findById(1L).orElse(new Deck(1L));
        Collection<Card> cards = deckService.findAllCardsOfADeck(1L);

        // setup
        deck.setIsPublic(true);
        when(deckService.getDeckById(1L)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(false);
        // check response
        ResponseEntity<Collection<Card>> response = deckController.findAllCardsOfADeck(1L);
        assertEquals(cards, response.getBody());
    }

    @Test
    public void testFindAllCardsOfADeckElseCase() {
        Deck deck = deckRepository.findById(1L).orElse(new Deck(1L));

        // setup
        deck.setIsPublic(false);
        when(deckService.getDeckById(1L)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(false);
        // check response
        assertThrows(IllegalAccessException.class, () -> deckController.findAllCardsOfADeck(1L));
    }

    @Test
    public void testFindAllRevertedCardsOfADeck() throws IllegalAccessException {
        Deck deck = deckRepository.findById(1L).orElse(new Deck(1L));
        Set<CardDto> cards = deckService.findAllRevertedCardsOfADeck(1L);

        // setup
        deck.setIsPublic(false);
        when(deckService.getDeckById(1L)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(true);
        // check response
        ResponseEntity<Collection<CardDto>> response = deckController.findAllRevertedCardsOfADeck(1L);
        assertEquals(cards, response.getBody());
    }

    @Test
    public void testFindAllRevertedCardsOfADeckIfCase() throws IllegalAccessException {
        Deck deck = deckRepository.findById(1L).orElse(new Deck(1L));
        Set<CardDto> cards = deckService.findAllRevertedCardsOfADeck(1L);

        // setup
        deck.setIsPublic(true);
        when(deckService.getDeckById(1L)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(false);
        // check response
        ResponseEntity<Collection<CardDto>> response = deckController.findAllRevertedCardsOfADeck(1L);
        assertEquals(cards, response.getBody());
    }

    @Test
    public void testFindAllRevertedCardsOfADeckElseCase() {
        Deck deck = deckRepository.findById(1L).orElse(new Deck(1L));

        // setup
        deck.setIsPublic(false);
        when(deckService.getDeckById(1L)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(false);
        // check response
        assertThrows(IllegalAccessException.class, () -> deckController.findAllRevertedCardsOfADeck(1L));
    }

    @Test
    public void testGetPublicDecks() {
        Collection<Deck> decks = deckService.getPublicDecks();
        assertEquals(decks, deckController.getPublicDecks());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void testPublishDeck() throws IllegalAccessException {
        long deckId = 1L;
        Deck deck = new Deck(deckId);
        deck.setCreateUser(sessionInfoBean.getCurrentUser());
        when(deckService.getDeckById(deckId)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(true);

        // when
        deckController.publishDeck(deckId);

        // then
        verify(deckService).makePublic(deckId);
        verify(deckService, never()).deleteDeck(any());
        verify(userxService).ifUserOwnsDeck(deck);
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void testPublishDeckElseCase() {
        long deckId = 1L;
        Deck deck = new Deck(deckId);
        deck.setCreateUser(new Userx());
        when(deckService.getDeckById(deckId)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(false);

        assertThrows(IllegalAccessException.class, () -> deckController.publishDeck(deckId));

        // verify
        verify(deckService, never()).makePublic(deckId);
        verify(deckService, never()).deleteDeck(any());
        verify(userxService).ifUserOwnsDeck(deck);
    }


    @Test
    public void testDeleteDeck() throws IllegalAccessException {
        // setup
        Deck deck = new Deck();
        when(deckService.getDeckById(1L)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(true);

        deckController.deleteDeck(1L);

        // verify that the deck was deleted
        verify(deckService).deleteDeck(deck);
    }

    @Test
    public void testDeleteDeckElseCase() {
        // setup
        Deck deck = new Deck();
        when(deckService.getDeckById(1L)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(false);

        // assert that an exception is thrown
        assertThrows(IllegalAccessException.class, () -> deckController.deleteDeck(1L));

        // verify that the deck was not deleted
        verify(deckService, never()).deleteDeck(deck);
    }

    @Test
    public void testAddNewCardToDeck() throws IllegalAccessException {
        long deckId = 1L;
        String question = "What is the capital of France?";
        String solution = "Paris";

        Deck deck = mock(Deck.class);
        when(deckService.getDeckById(deckId)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(true);
        deckController.addNewCardToDeck(deckId, question, solution);
        verify(deckService).saveCard(deckId, question, solution);
    }


    @Test
    public void testAddNewCardToDeckException() {
        long deckId = 1L;
        String question = "What is the capital of France?";
        String solution = "Paris";

        Deck deck = mock(Deck.class);
        when(deckService.getDeckById(deckId)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(false);

        assertThrows(IllegalAccessException.class, () -> deckController.addNewCardToDeck(deckId, question, solution));
    }

    @Test
    @WithMockUser(authorities = "STUDENT")
    public void deleteCardFromDeck() throws IllegalAccessException {
        long cardId = 1L;
        Deck deck = mock(Deck.class);
        Card card = mock(Card.class);

        when(deckService.getCardById(cardId)).thenReturn(card);
        when(card.getDeck()).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(true);

        deckController.deleteCardFromDeck(cardId);

        verify(deckService, times(1)).deleteCard(card);
    }

    @Test
    @WithMockUser(authorities = "STUDENT")
    public void deleteCardFromDeck_ThrowException() {
        long cardId = 1L;
        Deck deck = mock(Deck.class);
        Card card = mock(Card.class);

        when(deckService.getCardById(cardId)).thenReturn(card);
        when(card.getDeck()).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(false);

        Assertions.assertThrows(IllegalAccessException.class, () -> deckController.deleteCardFromDeck(cardId));

        verify(deckService, times(0)).deleteCard(card);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testBlockDeck() {
        // Given
        Long deckId = 1L;

        deckController.blockDeck(deckId);
        verify(deckService).lockDeck(deckId);
    }

    @Test
    void testCardsToStudy_normalCase() throws IllegalAccessException {
        // // normal case where user has access and there are cards to study
        // setup

        // 1.say that user owns the deck
        long deckId = 1L;
        Deck deck = mock(Deck.class);
        when(deckService.getDeckById(deckId)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(true);

        // get user deck
        UserDeck userDeck = mock(UserDeck.class);
        when(deckService.getUserDeck(deckId, userxService.getCurrentUserName())).thenReturn(userDeck);

        // mock the iterator
        when(deckService.getStudyIterator(userDeck)).thenReturn(studyIterator);
        when(studyIterator.hasNext()).thenReturn(true);

        Card card = mock(Card.class);
        when(studyIterator.next()).thenReturn(card);
        when(card.getCardId()).thenReturn(1L);
        when(card.getQuestion()).thenReturn("question");
        when(card.getSolution()).thenReturn("solution");

        when(userDeck.isReverted()).thenReturn(false);
        when(userDeck.getNewCards()).thenReturn(2);
        when(userDeck.getToBeRepeated()).thenReturn(3);

        // run
        Optional<CardDto> cardDto = deckController.cardsToStudy(deckId);

        // assert
        assertTrue(cardDto.isPresent());
        assertEquals(1L, cardDto.get().getCardId());
        assertEquals("question", cardDto.get().getQuestion());
        assertEquals("solution", cardDto.get().getSolution());
        assertEquals(2, cardDto.get().getNewCards());
        assertEquals(3, cardDto.get().getCardsToRepeat());
    }

    @Test
    void testCardsToStudy_userNotAllowed() {
        // setup (user does not own deck)
        long deckId = 1L;
        Deck deck = mock(Deck.class);
        when(deckService.getDeckById(deckId)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(false);

        assertThrows(IllegalAccessException.class, () -> deckController.cardsToStudy(deckId));
    }

    @Test
    public void testCardsToStudy_noCardsToStudy() throws IllegalAccessException {
        // no cards in deck -> exception
        long deckId = 1L;
        Deck deck = mock(Deck.class);
        when(deckService.getDeckById(deckId)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(true);

        // get user deck
        UserDeck userDeck = mock(UserDeck.class);
        when(deckService.getUserDeck(deckId, userxService.getCurrentUserName())).thenReturn(userDeck);

        when(deckService.getStudyIterator(userDeck)).thenReturn(studyIterator);
        when(studyIterator.hasNext()).thenReturn(false);

        Optional<CardDto> res = deckController.cardsToStudy(deckId);

        assertEquals(Optional.empty(), res);
    }

    @Test
    public void testRateCardAndNextCard() throws IllegalAccessException {
        // setup (case where everything runs through)
        long deckId = 1L;
        long cardId = 2L;
        String score = "3";
        CardDto expectedCard = new CardDto(3L, "question3", "solution3", 0, 0);

        UserDeck userDeck = mock(UserDeck.class);
        Deck deck = mock(Deck.class);
        Card card = mock(Card.class);

        when(userxService.getCurrentUserName()).thenReturn("user1");
        when(deckService.getDeckById(deckId)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(true);
        when(deckService.getUserDeck(deckId, "user1")).thenReturn(userDeck);
        when(deckService.getCardById(cardId)).thenReturn(card);
        when(studyIterator.hasNext()).thenReturn(true);
        when(studyIterator.next()).thenReturn(card);
        when(card.getCardId()).thenReturn(expectedCard.getCardId());
        when(card.getQuestion()).thenReturn(expectedCard.getQuestion());
        when(card.getSolution()).thenReturn(expectedCard.getSolution());
        when(userDeck.getNewCards()).thenReturn(expectedCard.getNewCards());
        when(userDeck.getToBeRepeated()).thenReturn(expectedCard.getCardsToRepeat());

        // run
        Optional<CardDto> actualCard = deckController.rateCardAndNextCard(deckId, cardId, score);

        // Assert
        assertTrue(actualCard.isPresent());
        assertEquals(expectedCard, actualCard.get());
        verify(studyIterator).rateCard(card, Integer.parseInt(score));
    }

    @Test
    public void testRateCardAndNextCard_Exception() {
        // setup
        long deckId = 1L;
        long cardId = 2L;
        String score = "3";

        Deck deck = mock(Deck.class);

        when(deckService.getDeckById(deckId)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(false);

        // Assert
        assertThrows(IllegalAccessException.class, () -> deckController.rateCardAndNextCard(deckId, cardId, score));
    }

    @Test
    void testRateCardAndNextCard_ReturnEmpty() throws IllegalAccessException {
        // setup
        long deckId = 1L;
        long cardId = 1L;
        String score = "5";
        UserDeck userDeck = mock(UserDeck.class);
        Deck deck = mock(Deck.class);
        Card card = mock(Card.class);

        when(deckService.getDeckById(deckId)).thenReturn(deck);
        when(userxService.ifUserOwnsDeck(deck)).thenReturn(true);
        when(deckService.getUserDeck(deckId, userxService.getCurrentUserName())).thenReturn(userDeck);
        when(deckService.getCardById(cardId)).thenReturn(card);
        when(studyIterator.hasNext()).thenReturn(false);

        Optional<CardDto> result = deckController.rateCardAndNextCard(deckId, cardId, score);

        assertTrue(result.isEmpty());
    }

    @Test
    void testEditDeck() throws IllegalAccessException {

        long deckId = 1L;
        String deckName = "New Deck Name";
        String deckDesc = "New Deck Description";

        when(userxService.ifUserOwnsDeck(any())).thenReturn(true);

        deckController.editDeck(deckId, deckName, deckDesc);

        verify(deckService, times(1)).editDeck(deckId, deckName, deckDesc);
    }

    @Test
    void testEditDeckException() {

        long deckId = 1L;
        String deckName = "New Deck Name";
        String deckDesc = "New Deck Description";

        when(userxService.ifUserOwnsDeck(any())).thenReturn(false);

        assertThrows(IllegalAccessException.class, () -> deckController.editDeck(deckId, deckName, deckDesc));

        verify(deckService, times(0)).editDeck(deckId, deckName, deckDesc);
    }


    @Test
    void testEditCard() throws IllegalAccessException {

        long deckId = 1L;
        long cardId = 1L;
        String question = "New Question";
        String solution = "New Solution";

        UserDeck userDeck = mock(UserDeck.class);
        userDeck.setReverted(false);

        when(userxService.ifUserOwnsDeck(any())).thenReturn(true);
        when(userxService.getCurrentUserName()).thenReturn(sessionInfoBean.getCurrentUserName());
        when(deckService.getUserDeck(deckId, sessionInfoBean.getCurrentUserName())).thenReturn(userDeck);


        deckController.editCard(deckId, cardId, question, solution);

        verify(deckService, times(1)).editCard(cardId, question, solution);
    }

    @Test
    void testEditCardReverted() throws IllegalAccessException {
        long deckId = 1L;
        long cardId = 1L;
        String question = "New Question";
        String solution = "New Solution";

        UserDeck userDeck = mock(UserDeck.class);
        userDeck.setReverted(true);

        when(userxService.ifUserOwnsDeck(any())).thenReturn(true);
        when(userxService.getCurrentUserName()).thenReturn(sessionInfoBean.getCurrentUserName());
        when(deckService.getUserDeck(deckId, sessionInfoBean.getCurrentUserName())).thenReturn(userDeck);
        when(userDeck.isReverted()).thenReturn(true);

        deckController.editCard(deckId, cardId, question, solution);

        verify(deckService, times(1)).editCard(cardId, solution, question);
    }

    @Test
    void testEditCardException() {
        long deckId = 1L;
        long cardId = 1L;
        String question = "New Question";
        String solution = "New Solution";

        when(userxService.ifUserOwnsDeck(any())).thenReturn(false);

        assertThrows(IllegalAccessException.class, () -> deckController.editCard(deckId, cardId, question, solution));
        verify(deckService, times(0)).editCard(cardId, question, solution);
    }

    @Test
    public void testAddPublicDeckToMyDecks() throws IllegalAccessException {
        long deckId = 1L;
        Deck deck = mock(Deck.class);
        when(deck.isPublic()).thenReturn(true);
        when(deckService.getDeckById(deckId)).thenReturn(deck);

        Userx mockUser = mock(Userx.class);
        when(userxService.getAuthenticatedUser()).thenReturn(mockUser);
        when(deckService.subscribePublicDeck(mockUser, deck)).thenReturn(true);

        boolean result = deckController.addPublicDeckToMyDecks(deckId);

        assertTrue(result);
    }

    @Test
    public void testAddPublicDeckToMyDecksFalse() throws IllegalAccessException {
        long deckId = 1L;
        Deck deck = mock(Deck.class);
        when(deck.isPublic()).thenReturn(true);
        when(deckService.getDeckById(deckId)).thenReturn(deck);

        Userx mockUser = mock(Userx.class);
        when(userxService.getAuthenticatedUser()).thenReturn(mockUser);
        when(deckService.subscribePublicDeck(mockUser, deck)).thenReturn(false);

        boolean result = deckController.addPublicDeckToMyDecks(deckId);

        assertFalse(result);
    }

    @Test
    public void testAddPublicDeckToMyDecksException() {
        long deckId = 1L;
        Deck deck = mock(Deck.class);
        when(deck.isPublic()).thenReturn(false);
        when(deckService.getDeckById(deckId)).thenReturn(deck);

        assertThrows(IllegalAccessException.class, () -> deckController.addPublicDeckToMyDecks(deckId));
    }

    @Test
    void testCheckIfDeckNameExists() {
        String deckName = "deckName";
        when(deckService.isPresentByName(deckName)).thenReturn(true);
        boolean result = deckController.checkIfDeckNameExists(deckName);

        assertTrue(result);
    }

    @Test
    public void testInverse() throws IllegalAccessException {
        long deckId = 1L;
        UserDeck userDeck = mock(UserDeck.class);
        when(userxService.ifUserOwnsDeck(deckService.getDeckById(deckId))).thenReturn(true);
        when(deckService.getUserDeck(deckId, userxService.getCurrentUserName())).thenReturn(userDeck);

        deckController.inverse(deckId);

        verify(deckService).inverse(userDeck);
    }

    @Test
    public void testInverseException() {
        long deckId = 1L;
        when(userxService.ifUserOwnsDeck(deckService.getDeckById(deckId))).thenReturn(false);

        Assertions.assertThrows(IllegalAccessException.class, () -> deckController.inverse(deckId));
    }

    @Test
    public void testCheckIfReverted() {
        // Given
        long deckId = 1L;
        Deck deck = mock(Deck.class);
        UserDeck userDeck = mock(UserDeck.class);
        Userx user = mock(Userx.class);
        when(deckService.getDeckById(deckId)).thenReturn(deck);
        when(userxService.getCurrentUserName()).thenReturn("testUser");

        // When deck is public
        when(deck.isPublic()).thenReturn(true);
        when(deck.getCreateUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");
        when(deckService.getUserDeck(deckId, "testUser")).thenReturn(userDeck);
        when(userDeck.isReverted()).thenReturn(true);
        boolean result = deckController.checkIfReverted(deckId);

        // When deck is not public
        when(deck.isPublic()).thenReturn(false);
        when(deckService.getUserDeck(deckId, "testUser")).thenReturn(userDeck);
        when(userDeck.isReverted()).thenReturn(false);
        boolean result2 = deckController.checkIfReverted(deckId);

        // Then
        assertTrue(result);
        assertFalse(result2);
    }

    @Test
    void testCheckIfDeckIsPublic() {
        long deckId = 1L;
        Deck test = mock(Deck.class);
        test.setIsPublic(true);

        when(deckService.getDeckById(deckId)).thenReturn(test);
        when(deckService.getDeckById(deckId).isPublic()).thenReturn(true);

        boolean result = deckController.checkIfDeckIsPublic(deckId);

        assertTrue(result);
    }



}
