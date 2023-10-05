package at.qe.skeleton.model;

import at.qe.skeleton.model.ids.CardAlgorithmDataId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * This class saves user-specific Deck properties like
 * reversed or amount of cards that are new or have to be repeated
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CardAlgorithmData implements Serializable {
    @EmbeddedId
    private CardAlgorithmDataId cardAlgorithmDataId;
    @CreatedDate
    @Temporal(TemporalType.DATE)
    private Date repeatDate;
    @Column(columnDefinition = "integer default 0")
    private int repetitions;
    @Column(columnDefinition = "double default 2.5")
    private double eFactor;
    @Column(columnDefinition = "integer default 0")
    private int learnInterval;

    public CardAlgorithmData() {
    }

    public CardAlgorithmData(Card card, UserDeck userDeck) {
        cardAlgorithmDataId = new CardAlgorithmDataId(userDeck, card);
    }

    public CardAlgorithmDataId getCardAlgorithmDataId() {
        return cardAlgorithmDataId;
    }

    public void setCardAlgorithmDataId(CardAlgorithmDataId cardAlgorithmDataId) {
        this.cardAlgorithmDataId = cardAlgorithmDataId;
    }

    public Date getRepeatDate() {
        return repeatDate;
    }

    public void setRepeatDate(Date repeatDate) {
        this.repeatDate = repeatDate;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public double geteFactor() {
        return eFactor;
    }

    public void seteFactor(double eFactor) {
        this.eFactor = eFactor;
    }

    public int getLearnInterval() {
        return learnInterval;
    }

    public void setLearnInterval(int learnInterval) {
        this.learnInterval = learnInterval;
    }

    public void addRepetition() {
        ++repetitions;
    }

    public Card getCard() {
        return cardAlgorithmDataId.getCard();
    }

    public UserDeck getUserDeck() {
        return cardAlgorithmDataId.getUserDeck();
    }
}
