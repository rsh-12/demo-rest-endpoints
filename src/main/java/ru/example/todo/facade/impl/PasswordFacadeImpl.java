package ru.example.todo.facade.impl;
/*
 * Date: 6/5/21
 * Time: 10:47 PM
 * */

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.example.todo.exception.CustomException;
import ru.example.todo.facade.PasswordFacade;
import ru.example.todo.messaging.MessagingService;
import ru.example.todo.messaging.requests.TokenRequest;
import ru.example.todo.service.UserService;

@Component
public class PasswordFacadeImpl implements PasswordFacade {

    private final MessagingService messagingService;
    private final UserService userService;

    public PasswordFacadeImpl(MessagingService messagingService, UserService userService) {
        this.messagingService = messagingService;
        this.userService = userService;
    }

    @Override
    public void updatePassword(TokenRequest token, String password) {

        String email = messagingService.sendTokenAndReceiveEmail(token);
        if (email == null || email.isBlank()) {
            throw new CustomException("An error occurred while generating the token",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        userService.updatePassword(email, password);
    }
}
