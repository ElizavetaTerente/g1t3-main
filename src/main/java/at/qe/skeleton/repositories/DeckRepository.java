package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Deck;
import at.qe.skeleton.model.Userx;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;


@Transactional
public interface DeckRepository extends AbstractRepository<Deck, Long>, Serializable {

    List<Deck> findByCreateUserAndLockedFalse(Userx user);

    List<Deck> findByIsPublicTrueAndLockedFalse();

    Optional<Deck> findByName(@Param("name") String name);

    Page<Deck> findByNameContainsIgnoreCaseAndIsPublicTrueOrDescriptionContainsIgnoreCaseAndIsPublicTrue(String name, String description, Pageable pageable);


}
