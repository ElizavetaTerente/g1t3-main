package at.qe.skeleton.configs;

import at.qe.skeleton.services.UserxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    final UserxService userxService;

    @Autowired
    public LoginSuccessHandler(UserxService userxService) {
        this.userxService = userxService;
    }

    //according to role of a authenticated user returns or index page for admin or index page for a user
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String name = userxService.getAuthenticatedUser().getUsername();
        String redirectURL = "/" + userxService.returnHighestRole(name) + "/index.html";
        response.sendRedirect(redirectURL);

    }
}
