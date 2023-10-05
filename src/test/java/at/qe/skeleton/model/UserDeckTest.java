package at.qe.skeleton.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDeckTest {
    private UserDeck userDeck;
    private Map<Card, CardAlgorithmData> cardAlgorithmData;
    private Userx user;
    private Deck deck;

    @BeforeEach
    public void setUp() {
        user = new Userx("username", "password", "firstName", "lastName", "email", Collections.singleton(UserxRole.STUDENT));
        deck = new Deck(user, "Deck", "description");
        userDeck = new UserDeck(user, deck);
        cardAlgorithmData = new HashMap<>();
    }

    @Test
    public void testGetCardAlgorithmData() {
        userDeck.setCardAlgorithmData(cardAlgorithmData);
        assertEquals(cardAlgorithmData, userDeck.getCardAlgorithmData());
    }

    @Test
    public void testGetDeck() {
        assertEquals(deck, userDeck.getDeck());
    }

    @Test
    public void testSetCardAlgorithmData() {
        Map<Card, CardAlgorithmData> cardAlgorithmData = new HashMap<>();
        userDeck.setCardAlgorithmData(cardAlgorithmData);
        assertEquals(cardAlgorithmData, userDeck.getCardAlgorithmData());
    }

    @Test
    public void testGetToBeRepeated() {
        userDeck.setToBeRepeated(5);
        assertEquals(5, userDeck.getToBeRepeated());
    }

    @Test
    public void testSetToBeRepeated() {
        userDeck.setToBeRepeated(5);
        assertEquals(5, userDeck.getToBeRepeated());
    }

    @Test
    public void testGetNewCards() {
        userDeck.setNewCards(10);
        assertEquals(10, userDeck.getNewCards());
    }

    @Test
    public void testSetNewCards() {
        userDeck.setNewCards(10);
        assertEquals(10, userDeck.getNewCards());
    }
}