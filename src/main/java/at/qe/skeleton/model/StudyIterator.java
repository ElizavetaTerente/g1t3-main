package at.qe.skeleton.model;

import at.qe.skeleton.repositories.CardAlgorithmDataRepository;
import at.qe.skeleton.repositories.UserDeckRepository;

import java.time.Instant;
import java.time.Period;
import java.util.*;

/**
 * The studyIterator implements the algorithm responsible for the shown cards in the study-view
 * Use the iterator.next() Method to cycle through the cards to learn.
 * Use iterator.ratecard(card, difficulty) to rate a card with a specific diffulty
 * The parameters "toBeRepeated" and "newCards" in The userDeck Entity get updated in real-time
 */
public class StudyIterator implements Iterator<Card> {

    private final PriorityQueue<CardAlgorithmData> queue;
    private final UserDeck userDeck;
    private final UserDeckRepository userDeckRepository;
    private final CardAlgorithmDataRepository cardAlgorithmDataRepository;

    public StudyIterator(UserDeck userDeck, List<CardAlgorithmData> cardAlgorithmData,
                         UserDeckRepository userDeckRepository, CardAlgorithmDataRepository cardAlgorithmDataRepository) {
        this.userDeck = userDeck;
        this.userDeckRepository = userDeckRepository;
        this.cardAlgorithmDataRepository = cardAlgorithmDataRepository;
        // new Cards should be queued last, already repeated cards should all have the same priority
        queue = new PriorityQueue<>((o1, o2) -> {
            int r1 = Math.min(o1.getRepetitions(), 1);
            int r2 = Math.min(o2.getRepetitions(), 1);
            return Integer.compare(r2, r1);
        });
        queue.addAll(cardAlgorithmData);
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public Card next() {
        if (!hasNext()) {
            throw new NoSuchElementException("There are no Cards left to learn");
        }
        return queue.poll().getCard();
    }

    /**
     * {@param difficulty should range 0..5 where 0 is the most difficult meaning "total blackout, didnt answer"
     * and 5 meaning "easy, fast answer", if the card rates a lower difficulty than 4 it gets re-added to the queue}
     * this method also updates the values "toBeRepeated" and "newCards" in The userDeck Entity
     */
    public void rateCard(Card card, int difficulty) {
        CardAlgorithmData cardAlgorithmData = userDeck.getCardAlgorithmData().get(card);
        cardAlgorithmData.addRepetition();

        int n = cardAlgorithmData.getRepetitions();
        if (n == 1) {
            userDeck.setNewCards(userDeck.getNewCards() - 1);
            if (difficulty < 4) {
                userDeck.setToBeRepeated(userDeck.getToBeRepeated() + 1);
            }
        } else if (difficulty > 3) {
            userDeck.setToBeRepeated(userDeck.getToBeRepeated() - 1);
        }

        if (difficulty < 3) {
            cardAlgorithmData.setLearnInterval(1);
        }
        if (difficulty < 4) {
            queue.add(cardAlgorithmData);
        }
        if (difficulty > 2) {
            if (n == 1) {
                cardAlgorithmData.setLearnInterval(1);
            } else if (n == 2) {
                cardAlgorithmData.setLearnInterval(6);
            } else {
                cardAlgorithmData.setLearnInterval(
                        (int) (cardAlgorithmData.getLearnInterval() * cardAlgorithmData.geteFactor()));
                cardAlgorithmData.seteFactor(
                        Math.max(1.3, cardAlgorithmData.geteFactor() - 0.8 + 0.28 * difficulty - 0.02 * difficulty * difficulty));
            }
        }
        if (difficulty >= 4) {
            cardAlgorithmData.setRepeatDate(Date.from(Instant.now().plus(Period.ofDays(cardAlgorithmData.getLearnInterval()))));
        }
        userDeckRepository.save(userDeck);
        cardAlgorithmDataRepository.save(cardAlgorithmData);
    }


}