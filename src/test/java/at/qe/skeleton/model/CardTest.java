package at.qe.skeleton.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CardTest {
    private final long TEST_ID = 1L;
    private final String TEST_QUESTION = "What is the capital of France?";
    private final String TEST_SOLUTION = "Paris";
    @Mock
    private Deck mockDeck;
    private Card testCard;

    @BeforeEach
    public void setUp() {
        testCard = new Card(mockDeck, TEST_QUESTION, TEST_SOLUTION);
        testCard.setCardId(TEST_ID);
    }

    @Test
    public void testGetCardId() {
        assertEquals(TEST_ID, testCard.getCardId());
    }

    @Test
    public void testGetQuestion() {
        assertEquals(TEST_QUESTION, testCard.getQuestion());
    }

    @Test
    public void testGetSolution() {
        assertEquals(TEST_SOLUTION, testCard.getSolution());
    }

    @Test
    public void testGetDeck() {
        assertEquals(mockDeck, testCard.getDeck());
    }
}
