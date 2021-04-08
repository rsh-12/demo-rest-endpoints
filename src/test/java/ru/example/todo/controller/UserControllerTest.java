package ru.example.todo.controller;
/*
 * Date: 3/26/21
 * Time: 2:10 PM
 * */

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// todo update tests
public class UserControllerTest extends AbstractTestContollerClass {

    private String requestBody(String username, String password) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("username", username);
        body.put("password", password);
        return asJsonString(body);
    }

    // Login: success
    @Test
    public void testLogin() throws Exception {
        String body = requestBody(ADMIN, "admin");

        String response = mvc.perform(post(USERS + "login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertTrue(response.contains("access_token"));
        assertTrue(response.contains("refresh_token"));
        assertTrue(response.contains("token_type"));
        assertTrue(response.contains("expires"));
    }

    // Login: fail
    @Test
    public void testLogin_NotFound() throws Exception {
        String body = requestBody("usernameNotExists", "client");

        mvc.perform(post(USERS + "login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", containsString("Invalid username/password")));
    }

    @Test
    public void testLogin_WrongPassword() throws Exception {
        String body = requestBody(USER, "wrongpassword");

        mvc.perform(post(USERS + "login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", containsString("Invalid username/password")));
    }

    // Register: success
    @Test
    public void testRegister() throws Exception {
        String body = requestBody("newUsername@mail.com", "newPassword");

        MvcResult result = mvc.perform(post(USERS + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertEquals("ok", response);
    }

    // Register: fail

    @Test
    public void testRegister_NotValid() throws Exception {
        String body = requestBody("notValidUsername", "password");

        mvc.perform(post(USERS + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("username", containsInAnyOrder("Not a valid email address")));
    }

    // Token: success
    // Token: fail
    // Delete user: success
    // Delete user:  fail
}
