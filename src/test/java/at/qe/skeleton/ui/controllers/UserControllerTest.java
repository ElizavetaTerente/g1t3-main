package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.UserxRole;
import at.qe.skeleton.services.UserxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserxService userxService;

    @InjectMocks
    UserController userController;

    @Test
    void checkRoles_shouldReturn1_whenUserIsStudent() throws IllegalAccessException {
        Userx user = new Userx("student", "password", "first", "last", "student@email.com",
                Collections.singleton(UserxRole.STUDENT));
        when(userxService.getAuthenticatedUser()).thenReturn(user);
        when(userxService.returnHighestRole("student")).thenReturn("student");

        int result = userController.checkRoles();

        assertEquals(1, result);
    }

    @Test
    void checkRoles_shouldReturn2_whenUserIsStudentAndAdmin() throws IllegalAccessException {
        Userx user = new Userx("student_admin", "password", "first", "last", "student_admin@email.com",
                Collections.singleton(UserxRole.STUDENT));
        user.getRoles();
        when(userxService.getAuthenticatedUser()).thenReturn(user);
        when(userxService.returnHighestRole("student_admin")).thenReturn("admin");

        int result = userController.checkRoles();

        assertEquals(2, result);
    }

    @Test
    void checkRoles_shouldReturn3_whenUserIsAdmin() throws IllegalAccessException {
        Userx user = new Userx("admin", "password", "first", "last", "admin@email.com",
                Collections.singleton(UserxRole.ADMIN));
        when(userxService.getAuthenticatedUser()).thenReturn(user);
        when(userxService.returnHighestRole("admin")).thenReturn("admin");

        int result = userController.checkRoles();

        assertEquals(3, result);
    }
}
