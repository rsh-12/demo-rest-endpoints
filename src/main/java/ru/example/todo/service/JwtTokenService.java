package ru.example.todo.service;

import org.springframework.security.core.Authentication;
import ru.example.todo.enums.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * This interface consists methods for working with the JWT token.
 *
 * @see ru.example.todo.service.impl.JwtTokenServiceImpl
 */
public interface JwtTokenService {

    /**
     * Returns an access token as a string.
     *
     * @param userId the user id
     * @param roles  the user roles
     * @return the signed JWT as a string with user's roles and id in the body
     * @see io.jsonwebtoken.Jwts
     */
    String buildAccessToken(Long userId, Set<Role> roles);

    /**
     * Gets the access token from header param "Authorization".
     *
     * @param request the http request
     * @return the access token as a string from request, if the Authorization header
     * is missing, returns an empty string
     */
    String resolveAccessToken(HttpServletRequest request);

    /**
     * Checks whether an access token has expired or not.
     *
     * @param accessToken the access token as a string
     * @return the boolean
     * @throws ru.example.todo.exception.CustomException if the access token has expired
     */
    boolean isAccessTokenValid(String accessToken);

    /**
     * Gets the user id from the access token body.
     *
     * @param accessToken the access token as a string
     * @return the user id as a Long
     */
    Long getId(String accessToken);

    /**
     * Gets user roles from the access token body.
     *
     * @param accessToken the access token as a string
     * @return the user roles
     */
    Set<Role> getUserRoles(String accessToken);

    /**
     * Retrieves the user data from the access token body:
     * id and roles. Creates an instance with of the User class
     * with the received data, then the UserDetailsImpl instance,
     * passing the created User instance to the constructor.
     * Returns an instance of the UsernamePasswordAuthenticationToken,
     * passing a UserDetailsImpl instance, empty string and user roles
     * to the constructor.
     *
     * @param accessToken the access token as a string
     * @return the authentication
     * @see ru.example.todo.entity.User
     * @see ru.example.todo.security.UserDetailsImpl
     * @see org.springframework.security.authentication.UsernamePasswordAuthenticationToken
     */
    Authentication getAuthentication(String accessToken);
}
