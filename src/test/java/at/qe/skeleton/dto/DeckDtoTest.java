package at.qe.skeleton.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DeckDtoTest {

    @Test
    void testSetters() {
        DeckDto deck = new DeckDto(1L, "Countries", "Geography", 0, 0);
        deck.setAmountOfNewCards(5);
        deck.setAmountOfCardsToRepeat(3);

        assertEquals(1L, deck.getDeckId());
        assertEquals("Countries", deck.getName());
        assertEquals("Geography", deck.getDescription());
        assertEquals(5, deck.getAmountOfNewCards());
        assertEquals(3, deck.getAmountOfCardsToRepeat());
    }


    @Test
    void testToString() {
        DeckDto deck = new DeckDto(1L, "Countries", "Geography", 0, 0);
        String expected = "DeckDto{deckId=1, name='Countries', description='Geography', amountOfNewCards=0, amountOfCardsToRepeat=0}";
        assertEquals(expected, deck.toString());
    }

    @Test
    void testEquals() {
        DeckDto deck1 = new DeckDto(1L, "Geography");
        DeckDto deck2 = new DeckDto(1L, "Geography");
        DeckDto deck3 = new DeckDto(2L, "History");
        assertEquals(deck1, deck2);
        assertNotEquals(deck1, deck3);
    }


}
