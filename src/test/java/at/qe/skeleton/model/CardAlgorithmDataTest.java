package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardAlgorithmDataTest {

    @Mock
    private Card card;
    @Mock
    private UserDeck userDeck;
    @Mock
    private CardAlgorithmData cardAlgorithmDataMock;

    @Test
    public void testAddRepetition() {
        CardAlgorithmData cardAlgorithmData = new CardAlgorithmData(card, userDeck);
        cardAlgorithmData.addRepetition();
        assertEquals(1, cardAlgorithmData.getRepetitions());
    }

    @Test
    public void testGetCard() {
        when(cardAlgorithmDataMock.getCard()).thenReturn(card);
        assertEquals(card, cardAlgorithmDataMock.getCard());
    }

    @Test
    public void testGetUserDeck() {
        when(cardAlgorithmDataMock.getUserDeck()).thenReturn(userDeck);
        assertEquals(userDeck, cardAlgorithmDataMock.getUserDeck());
    }
}
