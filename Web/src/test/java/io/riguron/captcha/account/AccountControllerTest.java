package io.riguron.captcha.account;

import io.riguron.captcha.user.UserProfile;
import io.riguron.captcha.user.UserProfileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountOpenController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProfileService userProfileService;

    @Test
    public void openAccount() throws Exception {

        UserProfile userProfile = mock(UserProfile.class);
        when(userProfile.getLogin()).thenReturn("KEY");

        this.mockMvc.perform(
                post("/register")
                        .param("login", "JohnDoe")
                        .param("password", "pass")
        ).andExpect(
                status().isOk()
        ).andDo(
                print()
        ).andExpect(
                jsonPath("$.name", is("JohnDoe"))
        );
    }
}