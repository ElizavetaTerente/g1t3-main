package at.qe.skeleton.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * Entity representing users.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Entity
public class Userx implements Persistable<String>, Serializable, Comparable<Userx> {

    private static final long serialVersionUID = 1L;
    boolean enabled;
    @Id//eindeutig aber verändrbar deswegen veränderbar
    @Column(length = 100)
    private String username;
    @JsonIgnore
    @ManyToOne(optional = false)
    private Userx createUser;
    @Column(nullable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @JsonIgnore
    @LastModifiedBy
    @ManyToOne(optional = true)
    private Userx updateUser;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    @ElementCollection(targetClass = UserxRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "Userx_UserxRole")
    @Enumerated(EnumType.STRING)
    private Set<UserxRole> roles;
    @JsonIgnore
    @OneToMany(mappedBy = "createUser", cascade = CascadeType.REMOVE)
    private Set<Deck> createdDecks;
    @JsonIgnore
    @OneToMany(mappedBy = "userDeckId.user", cascade = CascadeType.REMOVE)
    private Set<UserDeck> deckData;

    public Userx() {
    }

    //to add new user
    public Userx(String username, String password, String firstName, String lastName, String email, Set<UserxRole> roles) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
        this.enabled = true;
    }

    //to edit user
    public Userx(String username, String firstName, String lastName, Set<UserxRole> roles) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.enabled = true;
    }

    public Set<Deck> getCreatedDecks() {
        return createdDecks;
    }

    public void setCreatedDecks(Set<Deck> createdDecks) {
        this.createdDecks = createdDecks;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserxRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserxRole> roles) {
        this.roles = roles;
    }

    public Userx getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Userx createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Userx userx = (Userx) o;
        return Objects.equals(username, userx.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "at.qe.skeleton.model.User[ id=" + username + " ]";
    }

    @Override
    public String getId() {
        return getUsername();
    }

    public void setId(String id) {
        setUsername(id);
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

    @Override
    public int compareTo(Userx o) {
        return this.username.compareTo(o.getUsername());
    }

}
