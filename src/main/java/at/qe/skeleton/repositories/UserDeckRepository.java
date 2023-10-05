package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Deck;
import at.qe.skeleton.model.UserDeck;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.ids.UserDeckId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserDeckRepository extends AbstractRepository<UserDeck, UserDeckId> {
    List<UserDeck> findByUserDeckIdUser(Userx user);

    List<UserDeck> findByUserDeckIdDeck(Deck deck);

    UserDeck findByUserDeckIdDeckAndUserDeckIdUserUsername(Deck deck, String username);

    Page<UserDeck> findByUserDeckIdDeckNameContainsIgnoreCaseOrUserDeckIdDeckDescriptionContainsIgnoreCase(
            String str1, String str2, Pageable pageable);

    Page<UserDeck> findByUserDeckIdUserContainsIgnoreCaseAndUserDeckIdDeckNameContainsIgnoreCaseOrUserDeckIdUserContainsIgnoreCaseAndUserDeckIdDeckDescriptionContainsIgnoreCase(
            Userx user1, String str1, Userx user2, String str2, Pageable pageable);

    Page<UserDeck> findByUserDeckIdUser(Userx user, Pageable pageable);

    boolean existsByUserDeckIdUserAndUserDeckIdDeck(Userx userx, Deck deck);
}
