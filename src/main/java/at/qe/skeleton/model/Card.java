package at.qe.skeleton.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cardId;
    @Column
    private String question;
    @Column
    private String solution;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "deckId", nullable = false)
    private Deck deck;
    @OneToMany(mappedBy = "cardAlgorithmDataId.card", cascade = CascadeType.REMOVE)
    private Set<CardAlgorithmData> cardAlgorithmDataSet;

    public Card() {
    }

    /**
     * This is a copy Constructor, it copies a card to a certain deck
     */
    public Card(Card card, Deck deck) {
        this.deck = deck;
        this.question = card.question;
        this.solution = card.solution;
    }

    public Card(Deck deck, String question, String solution) {
        this.deck = deck;
        this.question = question;
        this.solution = solution;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (cardId != card.cardId) return false;
        if (!Objects.equals(question, card.question)) return false;
        if (!Objects.equals(solution, card.solution)) return false;
        return Objects.equals(deck, card.deck);
    }

    @Override
    public int hashCode() {
        int result = (int) (cardId ^ (cardId >>> 32));
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (solution != null ? solution.hashCode() : 0);
        result = 31 * result + (deck != null ? deck.hashCode() : 0);
        return result;
    }
}
