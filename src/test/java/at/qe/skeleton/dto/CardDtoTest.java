package at.qe.skeleton.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class CardDtoTest {

    @ParameterizedTest
    @CsvSource({
            "1, 'What is the capital of France?', 'Paris', 5, 3",
            "2, 'What is the largest planet in our solar system?', 'Jupiter', 10, 1"
    })
    void testConstructor(Long cardId, String question, String solution) {
        CardDto card = new CardDto(cardId, question, solution);
        assertEquals(cardId, card.getCardId());
        assertEquals(question, card.getQuestion());
        assertEquals(solution, card.getSolution());
    }

    @Test
    void testSetters() {
        CardDto card = new CardDto(1L, "What is the capital of France?", "Paris");
        card.setNewCards(5);
        card.setCardsToRepeat(3);

        assertEquals(1L, card.getCardId());
        assertEquals("What is the capital of France?", card.getQuestion());
        assertEquals("Paris", card.getSolution());
        assertEquals(5, card.getNewCards());
        assertEquals(3, card.getCardsToRepeat());
    }


    @ParameterizedTest
    @CsvSource({
            "1, 'What is the capital of France?', 'Paris'",
            "2, 'What is the largest planet in our solar system?', 'Jupiter'"
    })
    void testFullConstructor(Long cardId, String question, String solution) {
        CardDto card = new CardDto(cardId, question, solution);
        assertEquals(cardId, card.getCardId());
        assertEquals(question, card.getQuestion());
        assertEquals(solution, card.getSolution());
    }


    @Test
    void testToString() {
        CardDto card = new CardDto(1L, "What is the capital of France?", "Paris", 5, 3);
        String expected = "CardDto{cardId=1, question='What is the capital of France?', solution='Paris', newCards=5, cardsToRepeat=3}";
        assertEquals(expected, card.toString());
    }

    @Test
    void testEquals() {
        CardDto card1 = new CardDto(1L, "What is the capital of France?", "Paris", 5, 3);
        CardDto card2 = new CardDto(1L, "What is the capital of France?", "Paris", 5, 3);
        CardDto card3 = new CardDto(2L, "What is the capital of Italy?", "Rome", 7, 2);
        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
    }

    @Test
    void testConstructor_emptyCardId() {
        Exception exception = assertThrows(NullPointerException.class, () -> new CardDto(null, "What is the capital of France?", "Paris"));
        assertEquals("CardId must not be null", exception.getMessage());
    }

    @Test
    void testConstructor_emptyQuestion() {
        Exception exception = assertThrows(NullPointerException.class, () -> new CardDto(null, "What is the capital of France?", "Paris"));
        assertEquals("CardId must not be null", exception.getMessage());

    }

    @Test
    void testConstructor_emptySolution() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new CardDto(1L, "What is the capital of France?", null));
        assertEquals("Solution must not be null", exception.getMessage());
    }

    @Test
    void testFullConstructor_negativeNewCards() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new CardDto(1L, "What is the capital of France?", "Paris", -5, 3));
        assertEquals("newCards must not be negative", exception.getMessage());
    }

    @Test
    void testFullConstructor_negativeCardsToRepeat() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new CardDto(1L, "What is the capital of France?", "Paris", 5, -3));
        assertEquals("CardsToRepeat must be positive", exception.getMessage());
    }

    @Test
    void testSetters_negativeNewCards() {
        CardDto card = new CardDto(1L, "What is the capital of France?", "Paris");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> card.setNewCards(-5));
        assertEquals("NewCards must be positive", exception.getMessage());
    }


}
