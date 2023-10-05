package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Card;
import at.qe.skeleton.model.Deck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;

@Transactional
public interface CardRepository extends AbstractRepository<Card, Long> {

    Collection<Card> findAllByDeck(@Param("deck") Deck deck);

    Page<Card> findByDeckAndSolutionContainingIgnoreCaseOrDeckAndQuestionContainingIgnoreCase(
            Deck deck1, String str1, Deck deck2, String str2, Pageable pageable);

    Page<Card> findByDeck(Deck deck, Pageable pageable);
}
