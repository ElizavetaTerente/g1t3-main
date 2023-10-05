package at.qe.skeleton.configs;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.UserxRole;
import at.qe.skeleton.services.UserxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginSuccessHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private UserxService userxService;

    @InjectMocks
    private LoginSuccessHandler loginSuccessHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onAuthenticationSuccess_shouldRedirectToCorrectURL() throws Exception {
        when(userxService.getAuthenticatedUser()).thenReturn(new Userx("username", "password", "firstName", "lastName", "email", Collections.singleton(UserxRole.STUDENT)));
        when(userxService.returnHighestRole(any())).thenReturn("STUDENT");
        loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        verify(response).sendRedirect("/STUDENT/index.html");
    }
}
