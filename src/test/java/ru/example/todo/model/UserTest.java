package ru.example.todo.model;
/*
 * Date: 5/13/21
 * Time: 7:40 AM
 * */

import org.junit.Test;
import ru.example.todo.entity.User;
import ru.example.todo.enums.Role;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {

    @Test
    public void createUser_ShouldContainRoleUser() {
        User user = new User();
        assertTrue(user.getRoles().contains(Role.ROLE_USER));
        assertFalse(user.getRoles().contains(Role.ROLE_ADMIN));
    }

    @Test
    public void setRoles_ShouldContainAllRoles() {
        Set<Role> roles = Set.of(Role.ROLE_USER, Role.ROLE_ADMIN);
        User user = new User();
        user.setRoles(roles);
        assertTrue(user.getRoles().containsAll(roles));
    }

    @Test
    public void removeRole_ShouldRemoveRole() {
        User user = new User();
        assertTrue(user.getRoles().contains(Role.ROLE_USER));

        user.clearRoles();
        assertFalse(user.getRoles().stream()
                .anyMatch(role -> role.equals(Role.ROLE_USER) || role.equals(Role.ROLE_ADMIN)));
    }
}