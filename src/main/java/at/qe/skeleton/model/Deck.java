package at.qe.skeleton.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Deck implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long deckId;
    @JsonIgnore
    @ManyToOne
    @CreatedBy
    private Userx createUser;
    @JsonIgnore
    @ManyToOne
    @LastModifiedBy
    private Userx updateUser;
    @JsonIgnore
    @OneToMany(mappedBy = "userDeckId.deck", cascade = CascadeType.REMOVE)
    private Set<UserDeck> userDecks;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(length = 100)
    private String name;
    @Column(length = 200)
    private String description;
    @JsonIgnore
    @OneToMany(mappedBy = "deck", cascade = CascadeType.REMOVE)
    private Set<Card> cards;
    @Column(columnDefinition = "boolean default false")
    private boolean isPublic;
    @Column(columnDefinition = "boolean default false")
    private boolean locked;

    public Deck() {
    }

    /**
     * This is a copy Constructor, it copies a deck to a certain user,
     * the contained cards will also be copied
     */
    public Deck(Deck deck, Userx createUser) {
        this.createUser = createUser;
        this.name = deck.name;
        this.description = deck.description;
        this.isPublic = false;
        this.cards = deck.cards.stream().map((x) -> new Card(x, this)).collect(Collectors.toSet());
    }

    //dont remove this constructor its needed to add new deck (because in the beginning user only input name and description)
    public Deck(Userx createUser, String name, String description) {
        this.createUser = createUser;
        this.name = name;
        this.description = description;
    }

    public Deck(long deckId, Userx createUser, String name, String description, Set<Card> cards, boolean isPublic) {
        this.deckId = deckId;
        this.createUser = createUser;
        this.name = name;
        this.description = description;
        this.cards = cards;
        this.isPublic = isPublic;
    }

    public Deck(long deckId) {
        this.deckId = deckId;
    }

    public Set<UserDeck> getUserDecks() {
        return userDecks;
    }

    public void setUserDecks(Set<UserDeck> userDecks) {
        this.userDecks = userDecks;
    }

    public long getDeckId() {
        return deckId;
    }

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }

    public Userx getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Userx createUser) {
        this.createUser = createUser;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public int getAmountOfCards() {
        if (cards == null) {
            return 0;
        }
        return cards.size();
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Userx getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Userx updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setIsPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return deckId == deck.deckId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deckId);
    }

    @Override
    public String toString() {
        return "Deck{" +
                "deckId=" + deckId +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isPublic=" + isPublic +
                ", locked=" + locked +
                '}';
    }

}
