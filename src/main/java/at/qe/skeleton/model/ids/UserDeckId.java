package at.qe.skeleton.model.ids;


import at.qe.skeleton.model.Deck;
import at.qe.skeleton.model.Userx;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserDeckId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private Userx user;
    @ManyToOne
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    public UserDeckId() {
    }

    public UserDeckId(Userx user, Deck deck) {
        this.user = user;
        this.deck = deck;
    }

    public Userx getUser() {
        return user;
    }

    public void setUser(Userx user) {
        this.user = user;
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
        UserDeckId that = (UserDeckId) o;
        return user.equals(that.user) && deck.equals(that.deck);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, deck);
    }


}
