package at.qe.skeleton.model;

import at.qe.skeleton.repositories.CardAlgorithmDataRepository;
import at.qe.skeleton.repositories.UserDeckRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class StudyIteratorTest {

    @Mock
    private UserDeckRepository userDeckRepository;
    @Mock
    private CardAlgorithmDataRepository cardAlgorithmDataRepository;

    @Test
    public void NewCardsStudySessionRateAllCardsGreater3() {
        UserDeck userDeck = new UserDeck(mock(Userx.class), mock(Deck.class));
        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);
        Card card3 = mock(Card.class);
        CardAlgorithmData cardAlgorithmData1 = new CardAlgorithmData(card1, userDeck);
        CardAlgorithmData cardAlgorithmData2 = new CardAlgorithmData(card2, userDeck);
        CardAlgorithmData cardAlgorithmData3 = new CardAlgorithmData(card3, userDeck);
        HashMap<Card, CardAlgorithmData> map = new HashMap<>();
        map.put(card1, cardAlgorithmData1);
        map.put(card2, cardAlgorithmData2);
        map.put(card3, cardAlgorithmData3);
        userDeck.setCardAlgorithmData(map);
        StudyIterator studyIterator = new StudyIterator(userDeck, List.of(cardAlgorithmData1, cardAlgorithmData2, cardAlgorithmData3),
                userDeckRepository, cardAlgorithmDataRepository);

        studyIterator.next();
        studyIterator.rateCard(card1, 5);
        assertTrue(studyIterator.hasNext());
        assertEquals(1, cardAlgorithmData1.getLearnInterval());
        assertEquals(1, cardAlgorithmData1.getRepetitions());

        studyIterator.next();
        studyIterator.rateCard(card2, 4);
        assertTrue(studyIterator.hasNext());
        assertEquals(1, cardAlgorithmData2.getLearnInterval());
        assertEquals(1, cardAlgorithmData2.getRepetitions());

        studyIterator.next();
        studyIterator.rateCard(card3, 4);
        assertFalse(studyIterator.hasNext());
        assertEquals(1, cardAlgorithmData3.getLearnInterval());
        assertEquals(1, cardAlgorithmData3.getRepetitions());
    }

    @Test
    public void NewCardStudySessionRateOneCardSmaller4() {
        UserDeck userDeck = new UserDeck(mock(Userx.class), mock(Deck.class));
        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);
        Card card3 = mock(Card.class);
        CardAlgorithmData cardAlgorithmData1 = new CardAlgorithmData(card1, userDeck);
        CardAlgorithmData cardAlgorithmData2 = new CardAlgorithmData(card2, userDeck);
        CardAlgorithmData cardAlgorithmData3 = new CardAlgorithmData(card3, userDeck);
        HashMap<Card, CardAlgorithmData> map = new HashMap<>();
        map.put(card1, cardAlgorithmData1);
        map.put(card2, cardAlgorithmData2);
        map.put(card3, cardAlgorithmData3);
        userDeck.setCardAlgorithmData(map);
        StudyIterator studyIterator = new StudyIterator(userDeck, List.of(cardAlgorithmData1, cardAlgorithmData2, cardAlgorithmData3),
                userDeckRepository, cardAlgorithmDataRepository);

        studyIterator.next();
        studyIterator.rateCard(card1, 2);
        assertTrue(studyIterator.hasNext());
        assertEquals(1, cardAlgorithmData1.getLearnInterval());
        assertEquals(1, cardAlgorithmData1.getRepetitions());

        studyIterator.next();
        studyIterator.rateCard(card2, 4);
        assertTrue(studyIterator.hasNext());
        assertEquals(1, cardAlgorithmData2.getLearnInterval());
        assertEquals(1, cardAlgorithmData2.getRepetitions());

        studyIterator.next();
        studyIterator.rateCard(card3, 4);
        assertTrue(studyIterator.hasNext());
        assertEquals(1, cardAlgorithmData3.getLearnInterval());
        assertEquals(1, cardAlgorithmData3.getRepetitions());

        studyIterator.next();
        studyIterator.rateCard(card1, 5);
        assertFalse(studyIterator.hasNext());
        assertEquals(6, cardAlgorithmData1.getLearnInterval());
        assertEquals(2, cardAlgorithmData1.getRepetitions());
    }

    @Test
    public void MixedCardsStudySessionRateAllCardsGreater3() {
        UserDeck userDeck = new UserDeck(mock(Userx.class), mock(Deck.class));
        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);
        Card card3 = mock(Card.class);
        CardAlgorithmData cardAlgorithmData1 = new CardAlgorithmData(card1, userDeck);
        cardAlgorithmData1.setLearnInterval(6);
        cardAlgorithmData1.setRepetitions(2);
        cardAlgorithmData1.seteFactor(2.5);
        CardAlgorithmData cardAlgorithmData2 = new CardAlgorithmData(card2, userDeck);
        cardAlgorithmData2.setLearnInterval(6);
        cardAlgorithmData2.setRepetitions(2);
        cardAlgorithmData2.seteFactor(2.5);
        CardAlgorithmData cardAlgorithmData3 = new CardAlgorithmData(card3, userDeck);
        HashMap<Card, CardAlgorithmData> map = new HashMap<>();
        map.put(card1, cardAlgorithmData1);
        map.put(card2, cardAlgorithmData2);
        map.put(card3, cardAlgorithmData3);
        userDeck.setCardAlgorithmData(map);
        StudyIterator studyIterator = new StudyIterator(userDeck, List.of(cardAlgorithmData1, cardAlgorithmData2, cardAlgorithmData3),
                userDeckRepository, cardAlgorithmDataRepository);

        studyIterator.next();
        studyIterator.rateCard(card1, 5);
        assertTrue(studyIterator.hasNext());
        assertEquals(15, cardAlgorithmData1.getLearnInterval());
        assertEquals(3, cardAlgorithmData1.getRepetitions());

        studyIterator.next();
        studyIterator.rateCard(card2, 4);
        assertTrue(studyIterator.hasNext());
        assertEquals(15, cardAlgorithmData2.getLearnInterval());
        assertEquals(3, cardAlgorithmData2.getRepetitions());

        assertEquals(card3, studyIterator.next());
        studyIterator.rateCard(card3, 4);
        assertFalse(studyIterator.hasNext());
        assertEquals(1, cardAlgorithmData3.getLearnInterval());
        assertEquals(1, cardAlgorithmData3.getRepetitions());
    }

    @Test
    public void MixedCardsStudySessionRateOneCardSmaller4() {
        UserDeck userDeck = new UserDeck(mock(Userx.class), mock(Deck.class));
        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);
        Card card3 = mock(Card.class);
        CardAlgorithmData cardAlgorithmData1 = new CardAlgorithmData(card1, userDeck);
        cardAlgorithmData1.setLearnInterval(6);
        cardAlgorithmData1.setRepetitions(2);
        cardAlgorithmData1.seteFactor(2.5);
        CardAlgorithmData cardAlgorithmData2 = new CardAlgorithmData(card2, userDeck);
        cardAlgorithmData2.setLearnInterval(6);
        cardAlgorithmData2.setRepetitions(2);
        cardAlgorithmData2.seteFactor(2.5);
        CardAlgorithmData cardAlgorithmData3 = new CardAlgorithmData(card3, userDeck);
        HashMap<Card, CardAlgorithmData> map = new HashMap<>();
        map.put(card1, cardAlgorithmData1);
        map.put(card2, cardAlgorithmData2);
        map.put(card3, cardAlgorithmData3);
        userDeck.setCardAlgorithmData(map);
        StudyIterator studyIterator = new StudyIterator(userDeck, List.of(cardAlgorithmData1, cardAlgorithmData2, cardAlgorithmData3),
                userDeckRepository, cardAlgorithmDataRepository);

        studyIterator.next();
        studyIterator.rateCard(card1, 5);
        assertTrue(studyIterator.hasNext());
        assertEquals(15, cardAlgorithmData1.getLearnInterval());
        assertEquals(3, cardAlgorithmData1.getRepetitions());

        studyIterator.next();
        studyIterator.rateCard(card2, 2);
        assertTrue(studyIterator.hasNext());
        assertEquals(1, cardAlgorithmData2.getLearnInterval());
        assertEquals(3, cardAlgorithmData2.getRepetitions());

        assertEquals(card2, studyIterator.next());
        studyIterator.rateCard(card2, 4);
        assertTrue(studyIterator.hasNext());
        assertEquals(2, cardAlgorithmData2.getLearnInterval());
        assertEquals(4, cardAlgorithmData2.getRepetitions());

        studyIterator.next();
        studyIterator.rateCard(card3, 4);
        assertFalse(studyIterator.hasNext());
        assertEquals(1, cardAlgorithmData3.getLearnInterval());
        assertEquals(1, cardAlgorithmData3.getRepetitions());
    }

}