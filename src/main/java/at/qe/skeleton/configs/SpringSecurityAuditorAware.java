package at.qe.skeleton.configs;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.UserxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpringSecurityAuditorAware implements AuditorAware<Userx> {

    private final UserxService userservice;

    @Autowired
    public SpringSecurityAuditorAware(UserxService userservice) {
        this.userservice = userservice;
    }

    @Override
    public Optional<Userx> getCurrentAuditor() {
        return Optional.ofNullable(userservice.getAuthenticatedUser());
    }
}
