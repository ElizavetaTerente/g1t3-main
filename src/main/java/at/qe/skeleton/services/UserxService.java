package at.qe.skeleton.services;


import at.qe.skeleton.model.Deck;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.UserxRole;
import at.qe.skeleton.repositories.UserDeckRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class UserxService implements Serializable {

    @Autowired
    private UserxRepository userRepository;

    @Autowired
    private UserDeckRepository userDeckRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Userx> getAllUsers() {
        return userRepository.findAll();
    }


    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public Userx loadUser(String username) {
        return userRepository.findFirstByUsername(username);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void saveUser(Userx user) {
        if (user.isNew()) {
            user.setCreateDate(new Date());
            user.setCreateUser(getAuthenticatedUser());
        } else {
            user.setUpdateDate(new Date());
            user.setUpdateUser(getAuthenticatedUser());
        }
        userRepository.save(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(Userx user) {
        userRepository.delete(user);
    }

    public Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    public boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return auth.isAuthenticated() && !auth.getName().equals("anonymousUser");
        } else {
            return false;
        }
    }

    public String getCurrentUserName() {
        if (!isLoggedIn()) {
            return "";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName(); //get logged in username

    }

    public String returnHighestRole(String username) {
        Set<UserxRole> roles = userRepository.findFirstByUsername(username).getRoles();
        if (roles.contains(UserxRole.ADMIN)) {
            return "admin";
        } else {
            return "student";
        }
    }

    public Userx getUserById(String username) {
        Optional<Userx> user = userRepository.findById(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new IllegalArgumentException("User with such id is not found");
        }
    }

    public Collection<Userx> getAllUsersBesidesCurrentOne() {
        Collection<Userx> users = userRepository.findAll();
        users.remove(getAuthenticatedUser());
        return users;
    }

    public boolean ifUserOwnsDeck(Deck deck) {
        return userDeckRepository.existsByUserDeckIdUserAndUserDeckIdDeck(getAuthenticatedUser(), deck);
    }


}
