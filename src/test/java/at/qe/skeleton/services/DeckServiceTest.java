package at.qe.skeleton.services;

import at.qe.skeleton.model.Deck;
import at.qe.skeleton.repositories.DeckRepository;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class DeckServiceTest {
    @Autowired
    SessionInfoBean sessionInfoBean;
    DeckRepository deckRepository;
    DeckService deckService;
    UserxService userxService;

    @Autowired
    public DeckServiceTest(DeckRepository deckRepository, DeckService deckService, UserxService userxService) {
        this.deckRepository = deckRepository;
        this.deckService = deckService;
        this.userxService = userxService;
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    void testAmountOfDecksUsingGetAll() {
        assertEquals(6, (long) deckService.getAllDecks().size());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    void getDeckById() {
        Optional<Deck> deck = deckRepository.findById(1L);
        assertEquals(deck.orElse(null), deckService.getDeckById(1));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    void isPresentByDeckId() {
        assertTrue(deckService.isPresentByDeckId(1));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    void isPresentByName() {
        assertTrue(deckService.isPresentByName("english"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    void saveNewDeck() {
        deckService.saveNewDeck(sessionInfoBean.getCurrentUser(), "test", "desc");
        assertTrue(deckService.isPresentByName("test"));
        deckService.deleteDeck(deckRepository.findByName("test").orElse(null));
    }

    @Test
    void getPublicDecks() {
        assertEquals(2, deckService.getPublicDecks().size());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    void makePublic() {
        // make sure deck with id 1 is not public already
        Deck test = deckService.getDeckById(1);
        test.setIsPublic(false);
        assertEquals(1, test.getDeckId());
        deckRepository.save(test);

        deckService.makePublic(1L);

        assertTrue(deckService.getDeckById(1).isPublic());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    void deleteDeck() {
        assertTrue(deckRepository.existsById(1L));
        Deck temp = deckService.getDeckById(1L);

        deckService.deleteDeck(deckService.getDeckById(1L));
        assertFalse(deckRepository.existsById(1L));
        deckRepository.save(temp);
    }


}