package at.qe.skeleton.model;


import at.qe.skeleton.model.ids.UserDeckId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;


/**
 * This class saves user-specific Deck properties like
 * reversed or amount of cards that are new or have to be repeated
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserDeck implements Serializable {

    @EmbeddedId
    private UserDeckId userDeckId;

    @OneToMany(mappedBy = "cardAlgorithmDataId.userDeck", cascade = CascadeType.REMOVE)
    private Map<Card, CardAlgorithmData> cardAlgorithmData;

    @Column(columnDefinition = "integer default 0")
    private int toBeRepeated;
    @Column(columnDefinition = "integer default 0")
    private int newCards;
    @Column(columnDefinition = "boolean default false")
    private boolean reverted;// = false;

    public UserDeck() {
    }

    public UserDeck(Userx user, Deck deck) {
        userDeckId = new UserDeckId(user, deck);
    }

    public Map<Card, CardAlgorithmData> getCardAlgorithmData() {
        return cardAlgorithmData;
    }

    public void setCardAlgorithmData(Map<Card, CardAlgorithmData> cardAlgorithmData) {
        this.cardAlgorithmData = cardAlgorithmData;
    }

    public Deck getDeck() {
        return userDeckId.getDeck();
    }

    public UserDeckId getUserDeckId() {
        return userDeckId;
    }

    public void setUserDeckId(UserDeckId userDeckId) {
        this.userDeckId = userDeckId;
    }

    public int getToBeRepeated() {
        return toBeRepeated;
    }

    public void setToBeRepeated(int toBeRepeated) {
        this.toBeRepeated = toBeRepeated;
    }

    public int getNewCards() {
        return newCards;
    }

    public void setNewCards(int newCards) {
        this.newCards = newCards;
    }

    public boolean isReverted() {
        return reverted;
    }

    public void setReverted(boolean reverted) {
        this.reverted = reverted;
    }
}
