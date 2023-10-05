package at.qe.skeleton.exceptions;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

//class to show custom 403 error page and log this action
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    public static final Logger LOG
            = Logger.getLogger(CustomAccessDeniedHandler.class.getName());

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException {

        //log the attemot and user who did it
        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();
        LOG.warning("User: " + auth.getName()
                + " attempted to access the protected URL: "
                + request.getRequestURI());
        //redirect to custom error page
        response.sendRedirect(request.getContextPath() + "/error/403.html");
    }
}
