package com.example.oauth2.config;

import com.example.oauth2.domain.SnsOAuth2User;
import com.example.oauth2.domain.User;
import com.example.oauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SnsOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof OidcUser) {
            // google
            SnsOAuth2User odicUser = SnsOAuth2User.OAuth2Provider.google.convert((OidcUser) principal);
            User user = userService.loadUser(odicUser);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
            );
            System.out.println("success handler oidc => " + principal);
        } else if (principal instanceof OAuth2User) {
            //naver
            SnsOAuth2User oAuth2User = SnsOAuth2User.OAuth2Provider.naver.convert((OAuth2User) principal);
            User user = userService.loadUser(oAuth2User);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
            );
            System.out.println("success handler OAuth2 => " + principal);
        }
        System.out.println("successHandler => " + response.toString());
        request.getRequestDispatcher("/api/snsLogin").forward(request, response);
    }
}
