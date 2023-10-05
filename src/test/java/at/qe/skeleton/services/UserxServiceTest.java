package at.qe.skeleton.services;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.UserDeckRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserxServiceTest {

    @Mock
    UserxRepository userRepository;

    @InjectMocks
    UserxService userxService;

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        assertEquals(0, userxService.getAllUsers().size());
    }

    @Test
    void loadUser() {
        when(userRepository.findFirstByUsername(anyString())).thenReturn(new Userx());
        assertNotNull(userxService.loadUser("username"));
    }

    @Test
    void isLoggedIn() {
        assertFalse(userxService.isLoggedIn());
    }

    @Test
    void getCurrentUserName() {
        assertEquals("", userxService.getCurrentUserName());
    }


}
