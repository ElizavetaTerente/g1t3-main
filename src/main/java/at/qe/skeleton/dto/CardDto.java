package at.qe.skeleton.dto;

/**
 * Class to comfortably pass data to front end
 * Contains data from 2 classes : Card and Deck
 * Also used for reverted mode of a deck : when reverted cards for studying mode oder detailed view are required
 * then while creating new CardDto arguments "question" and "solution" passed to constructor with changed places
 */

import java.util.Objects;

public class CardDto {
    private final Long cardId;
    private String question;
    private String solution;
    private int newCards;
    private int cardsToRepeat;

    public CardDto(Long cardId, String question, String solution, int newCards, int cardsToRepeat) {
        this.cardId = Objects.requireNonNull(cardId, "CardId must not be null");
        this.question = Objects.requireNonNull(question, "Question must not be null");
        this.solution = Objects.requireNonNull(solution, "Solution must not be null");
        if (newCards < 0) {
            throw new IllegalArgumentException("newCards must not be negative");
        }
        if (cardsToRepeat < 0) {
            throw new IllegalArgumentException("CardsToRepeat must be positive");
        }

        this.newCards = newCards;
        this.cardsToRepeat = cardsToRepeat;
    }

    public CardDto(Long cardId, String question, String solution) {
        this.cardId = Objects.requireNonNull(cardId, "CardId must not be null");
        this.question = Objects.requireNonNull(question, "Question must not be null");
        this.solution = Objects.requireNonNull(solution, "Solution must not be null");
        this.newCards = 0;
        this.cardsToRepeat = 0;
    }


    public Long getCardId() {
        return cardId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public int getNewCards() {
        return newCards;
    }

    public void setNewCards(int newCards) {
        if (newCards < 0) {
            throw new IllegalArgumentException("NewCards must be positive");
        }
        this.newCards = newCards;
    }

    public int getCardsToRepeat() {
        if (cardsToRepeat < 0) {
            throw new IllegalArgumentException("CardsToRepeat must be positive");
        }
        return cardsToRepeat;
    }


    public void setCardsToRepeat(int cardsToRepeat) {
        this.cardsToRepeat = cardsToRepeat;
    }

    @Override
    public String toString() {
        return "CardDto{cardId=" + cardId +
                ", question='" + question + '\'' +
                ", solution='" + solution + '\'' +
                ", newCards=" + newCards +
                ", cardsToRepeat=" + cardsToRepeat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardDto cardDto = (CardDto) o;
        return newCards == cardDto.newCards &&
                cardsToRepeat == cardDto.cardsToRepeat &&
                Objects.equals(cardId, cardDto.cardId) &&
                Objects.equals(question, cardDto.question) &&
                Objects.equals(solution, cardDto.solution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, question, solution, newCards, cardsToRepeat);
    }

}
