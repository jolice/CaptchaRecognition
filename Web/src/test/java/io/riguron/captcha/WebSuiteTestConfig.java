package io.riguron.captcha;

import io.riguron.captcha.user.UserProfile;
import io.riguron.captcha.user.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)})
public class WebSuiteTestConfig {


    public static final String USER_TOKEN = "token";

    public static final int USER_ID = 42;

    @Bean
    public AuthenticationSuccessHandler authSuccessHandler() {
        return (request, response, authentication) -> {
        };
    }

    @Autowired
    private WebApplicationContext context;


    @Bean
    public MockMvc setup() {
        return MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public UserRequestHolder userRequestHolder() {
        return new UserRequestHolder();
    }

    @Bean
    public UserProfileService userProfileService() {
        UserProfileService userProfileService = mock(UserProfileService.class);
        when(userProfileService.findByToken(USER_TOKEN)).thenAnswer(invocationOnMock -> {
            UserProfile userProfile = mock(UserProfile.class);
            when(userProfile.getId()).thenReturn(USER_ID);
            return Optional.of(userProfile);
        });
        return userProfileService;
    }

}
