package at.qe.skeleton.model;

import at.qe.skeleton.repositories.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeckTest {
    @Autowired
    CardRepository cardRepository;
    @Mock
    private Userx userx;
    @Mock
    private Card card;
    @InjectMocks
    private Deck deck;
    @Mock
    private Userx createUser;
    @Mock
    private Set<Card> cards;

    @Test
    void testNotEquals() {
        // Arrange
        Deck deck2 = new Deck(1);
        deck2.setDeckId(2);

        assertNotEquals(deck, deck2);
    }


    @Test
    void testCopyConstructor() {

        when(createUser.getUsername()).thenReturn("userxy");
        //   when(cards.size()).thenReturn(2);
        Deck deck2 = new Deck(deck, createUser);

        assertEquals(deck2.getCreateUser().getUsername(), "userxy");
        //   assertEquals(deck2.getCards().size(), 2);
    }

    @Test
    void testDeckConstructor() {

        when(createUser.getUsername()).thenReturn("userMark");
        Deck deck2 = new Deck(createUser, "German B1", "Vocabluary 1");

        assertEquals(deck2.getCreateUser().getUsername(), "userMark");
        assertEquals(deck2.getName(), "German B1");
        assertEquals(deck2.getDescription(), "Vocabluary 1");
    }


    @Test
    void testIsLocked() {
        Deck deck = new Deck();
        deck.setLocked(true);
        assertTrue(deck.isLocked());
        deck.setLocked(false);
        assertFalse(deck.isLocked());
    }

    @Test
    void testIsPublic() {
        Deck deck = new Deck();
        deck.setIsPublic(true);
        assertTrue(deck.isPublic());
        deck.setIsPublic(false);
        assertFalse(deck.isPublic());
    }
/*
    @Test
    void testAddCard() {

        Deck deck = new Deck();
        Card card = new Card();
        deck.addCard(card);
        assertTrue(deck.getCards().contains(card));
    }

 */

    @Test
    void testEquals() {
        Deck deck1 = new Deck(1);
        Deck deck2 = new Deck(1);
        assertEquals(deck1, deck2);
        Card card1 = new Card();
        card1.setCardId(1);
        Card card2 = new Card();
        card2.setCardId(1);
        assertEquals(card1, card2);

        card1.setCardId(1);

        card2.setCardId(2);
        assertNotEquals(card1, card2);

        card1.setCardId(1);
        assertEquals(card1, card1);
    }

    @Test
    void testHashCode() {
        Card card1 = new Card();
        card1.setCardId(1);
        Card card2 = new Card();
        card2.setCardId(1);
        assertEquals(card1.hashCode(), card2.hashCode());

        card1.setCardId(1);

        card2.setCardId(2);
        assertNotEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void testToString() {
        Deck deck = new Deck();
        deck.setName("deckName");
        assertEquals(deck.getName(), "deckName");
    }

    //null Object
    @Test
    void testEquals_nullObject() {
        Card card1 = new Card();
        card1.setCardId(1);
        assertNotEquals(null, card1);
    }

    // different object type:
    @Test
    void testEquals_differentObjectType() {
        Card card1 = new Card();
        card1.setCardId(1);
        assertNotEquals("string object", card1);
    }

}