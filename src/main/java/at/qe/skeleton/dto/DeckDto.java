package at.qe.skeleton.dto;

import java.util.Objects;

/**
 * Class to comfortably pass data to front end
 * Contains dara of 2 classes (Deck and UserDeck)
 */
public class DeckDto {

    private Long deckId;
    private String name;
    private String description;
    private int amountOfNewCards;
    private int amountOfCardsToRepeat;

    public DeckDto(Long deckId, String name, String description, int amountOfNewCards, int amountOfCardsToRepeat) {
        this.deckId = deckId;
        this.name = name;
        this.description = description;
        this.amountOfNewCards = amountOfNewCards;
        this.amountOfCardsToRepeat = amountOfCardsToRepeat;
    }

    public DeckDto(long deckId, String name) {

        this.deckId = deckId;
        this.name = name;
    }

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmountOfNewCards() {
        return amountOfNewCards;
    }

    public void setAmountOfNewCards(int amountOfNewCards) {
        this.amountOfNewCards = amountOfNewCards;
    }

    public int getAmountOfCardsToRepeat() {
        return amountOfCardsToRepeat;
    }

    public void setAmountOfCardsToRepeat(int amountOfCardsToRepeat) {
        this.amountOfCardsToRepeat = amountOfCardsToRepeat;
    }

    @Override
    public String toString() {
        return "DeckDto{" +
                "deckId=" + deckId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amountOfNewCards=" + amountOfNewCards +
                ", amountOfCardsToRepeat=" + amountOfCardsToRepeat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeckDto deckDto = (DeckDto) o;
        return amountOfNewCards == deckDto.amountOfNewCards && amountOfCardsToRepeat == deckDto.amountOfCardsToRepeat && Objects.equals(deckId, deckDto.deckId) && Objects.equals(name, deckDto.name) && Objects.equals(description, deckDto.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deckId, name, description, amountOfNewCards, amountOfCardsToRepeat);
    }
}