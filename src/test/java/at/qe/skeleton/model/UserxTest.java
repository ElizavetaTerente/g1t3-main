package at.qe.skeleton.model;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserxTest {

    @Test
    void testUserxConstructor() {
        String username = "username";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "email@example.com";
        Set<UserxRole> roles = new HashSet<>();
        roles.add(UserxRole.ADMIN);
        roles.add(UserxRole.STUDENT);

        Userx userx = new Userx(username, password, firstName, lastName, email, roles);

        assertEquals(username, userx.getUsername());
        assertEquals(password, userx.getPassword());
        assertEquals(firstName, userx.getFirstName());
        assertEquals(lastName, userx.getLastName());
        assertEquals(email, userx.getEmail());
        assertEquals(roles, userx.getRoles());
    }

    @Test
    void testUserxEditConstructor() {
        String username = "username";
        String firstName = "firstName";
        String lastName = "lastName";
        Set<UserxRole> roles = new HashSet<>();
        roles.add(UserxRole.ADMIN);
        roles.add(UserxRole.STUDENT);

        Userx userx = new Userx(username, firstName, lastName, roles);

        assertEquals(username, userx.getUsername());
        assertEquals(firstName, userx.getFirstName());
        assertEquals(lastName, userx.getLastName());
        assertEquals(roles, userx.getRoles());
    }
}
