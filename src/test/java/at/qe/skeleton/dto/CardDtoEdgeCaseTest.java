package at.qe.skeleton.dto;
/*
import org.junit.Test;

public class CardDtoEdgeCaseTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyQuestion() {
        CardDto card1 = new CardDto(1L, "", "Paris");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySolution() {
        CardDto card2 = new CardDto(1L, "What is the capital of France?", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValues() {
        CardDto card3 = new CardDto(1L, "What is the capital of France?", "Paris", -5, -3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCardId() {
        CardDto card = new CardDto(null, "What is the capital of France?", "Paris");
    }
      WE NEED COnstructors like
         public CardDto(Long cardId, String question, String solution) {
        if (cardId == null) {
            throw new IllegalArgumentException("Card Id cannot be null.");
        }
        if (question == null || question.isEmpty()) {
            throw new IllegalArgumentException("Question cannot be empty or null.");
        }
        if (solution == null || solution.isEmpty()) {
            throw new IllegalArgumentException("Solution cannot be empty or null.");
        }
        this.cardId = cardId;
        this.question = question;
        this.solution = solution;
    }

    public CardDto(Long cardId, String question, String solution, int newCards, int cardsToRepeat) {
        if (cardId == null) {
            throw new IllegalArgumentException("Card Id cannot be null.");
        }
        if (question == null || question.isEmpty()) {
            throw new IllegalArgumentException("Question cannot be empty or null.");
        }
        if (solution == null || solution.isEmpty()) {
            throw new IllegalArgumentException("Solution cannot be empty or null.");
        }
        if (newCards < 0) {
            throw new IllegalArgumentException("Number of new cards cannot be negative.");
        }
        if (cardsToRepeat < 0) {
            throw new IllegalArgumentException("Number of cards to repeat cannot be negative.");
        }
        this.cardId = cardId;
        this.question = question;
        this.solution = solution;
        this.newCards = newCards;
        this.cardsToRepeat = cardsToRepeat;
    }
 in CardDto for this Clas to work



}
*/