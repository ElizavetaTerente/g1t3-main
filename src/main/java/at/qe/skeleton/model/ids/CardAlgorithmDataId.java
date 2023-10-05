package at.qe.skeleton.model.ids;

import at.qe.skeleton.model.Card;
import at.qe.skeleton.model.UserDeck;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CardAlgorithmDataId implements Serializable {
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "username", referencedColumnName = "username", nullable = false),
            @JoinColumn(name = "deck_id", referencedColumnName = "deck_id", nullable = false)
    })
    private UserDeck userDeck;
    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    public CardAlgorithmDataId() {

    }

    public CardAlgorithmDataId(UserDeck userDeck, Card card) {
        this.userDeck = userDeck;
        this.card = card;
    }

    public UserDeck getUserDeck() {
        return userDeck;
    }

    public void setUserDeck(UserDeck userDeck) {
        this.userDeck = userDeck;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardAlgorithmDataId that = (CardAlgorithmDataId) o;

        if (!Objects.equals(userDeck, that.userDeck)) return false;
        return Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        int result = userDeck != null ? userDeck.hashCode() : 0;
        result = 31 * result + (card != null ? card.hashCode() : 0);
        return result;
    }
}
