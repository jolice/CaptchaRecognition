package me.nextgeneric.captcha.security;

import me.nextgeneric.captcha.UserRequestHolder;
import me.nextgeneric.captcha.user.UserProfile;
import me.nextgeneric.captcha.user.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private UserRequestHolder userRequestHolder;
    private UserProfileService userProfileService;

    @Autowired
    public TokenAuthenticationFilter(UserRequestHolder userRequestHolder, UserProfileService userProfileService) {
        this.userRequestHolder = userRequestHolder;
        this.userProfileService = userProfileService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String token = httpServletRequest.getParameter("token");

        if (token != null) {
            Optional<UserProfile> userProfile = userProfileService.findByToken(token);
            if (userProfile.isPresent()) {
                UserProfile loadedProfile = userProfile.get();
                userRequestHolder.setUserProfile(loadedProfile);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loadedProfile.getLogin(), null, Collections.emptyList()));
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                httpServletResponse.getWriter().println("No user associated with specified token");
            }
        } else {
            httpServletResponse.getWriter().println("No token specified");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/account/open");
    }
}
