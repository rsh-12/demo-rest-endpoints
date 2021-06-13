package ru.example.todo.controller;
/*
 * Date: 3/26/21
 * Time: 2:10 PM
 * */

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import ru.example.todo.entity.User;
import ru.example.todo.exception.CustomException;
import ru.example.todo.service.UserService;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest extends AbstractControllerTestClass {

    @MockBean
    private UserService userService;

    @Test
    @WithUserDetails(ADMIN)
    public void getUser_ShouldReturnUser() throws Exception {
        given(userService.findUserById(Mockito.anyLong()))
                .willReturn(new User("user", "password"));

        mvc.perform(get(API_USERS + 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username", is("user")))
                .andDo(print());

        verify(userService, times(1)).findUserById(Mockito.anyLong());
    }

    @Test
    @WithUserDetails(ADMIN)
    public void getUser_ShouldReturnNotFound() throws Exception {
        given(userService.findUserById(Mockito.anyLong()))
                .willThrow(new CustomException("Not Found", "User Not Found", HttpStatus.NOT_FOUND));

        mvc.perform(get(API_USERS + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error", containsStringIgnoringCase("not found")))
                .andDo(print());

        verify(userService, times(1)).findUserById(Mockito.anyLong());
    }


}
