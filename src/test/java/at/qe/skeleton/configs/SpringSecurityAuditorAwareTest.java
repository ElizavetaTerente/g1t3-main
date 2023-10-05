package at.qe.skeleton.configs;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.UserxRole;
import at.qe.skeleton.services.UserxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SpringSecurityAuditorAwareTest {
    @Mock
    private UserxService userxService;
    @InjectMocks
    private SpringSecurityAuditorAware springSecurityAuditorAware;
    private Userx userx;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userx = new Userx("username", "password", "firstName", "lastName", "email", Collections.singleton(UserxRole.STUDENT));
    }

    @Test
    public void testGetCurrentAuditor() {
        when(userxService.getAuthenticatedUser()).thenReturn(userx);
        assertEquals(userx, springSecurityAuditorAware.getCurrentAuditor().get());
    }


}