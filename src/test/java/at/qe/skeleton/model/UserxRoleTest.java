package at.qe.skeleton.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserxRoleTest {

    @Test
    public void testUserxRoleValues() {
        assertEquals("ADMIN", UserxRole.ADMIN.name());
        assertEquals("STUDENT", UserxRole.STUDENT.name());
    }
}
