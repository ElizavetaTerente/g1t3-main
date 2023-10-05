package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Card;
import at.qe.skeleton.model.CardAlgorithmData;
import at.qe.skeleton.model.UserDeck;
import at.qe.skeleton.model.ids.CardAlgorithmDataId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface CardAlgorithmDataRepository extends AbstractRepository<CardAlgorithmData, CardAlgorithmDataId> {

    @Query("select c from CardAlgorithmData c where c.repeatDate <= CURRENT_DATE and c.cardAlgorithmDataId.userDeck =:userDeck")
    List<CardAlgorithmData> findByRepeatDateBeforeAndToday(@Param("userDeck") UserDeck userDeck);

    int countByCardAlgorithmDataIdUserDeckAndRepetitionsIs(UserDeck userDeck, int repetitions);

    @Query("select COUNT(c) from CardAlgorithmData c where c.repeatDate <= CURRENT_DATE and c.cardAlgorithmDataId.userDeck = ?1 and c.repetitions > 0")
    int countByRepeatDateBeforeAndTodayAndRepeatedAtleastOnce(UserDeck userDeck);

    Optional<CardAlgorithmData> findByCardAlgorithmDataIdCardAndCardAlgorithmDataIdUserDeck(Card card, UserDeck userDeck);
}
