package io.riguron.captcha.controller;

import io.riguron.captcha.CaptchaService;
import io.riguron.captcha.TestConfiguration;
import io.riguron.captcha.controller.text.TextInputController;
import io.riguron.captcha.type.TextCaptcha;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TextInputController.class)
public class TextInputControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaptchaService captchaService;

    @Test
    @WithUserDetails
    public void postText() throws Exception {
        when(captchaService.addCaptcha(eq(
                new TextCaptcha(1, "http://spring.io", "aBcDe")
        ))).thenReturn(5);

        mockMvc.perform(
                post("/add")
                .param("method", "text")
                .param("text", "aBcDe")
                .param("callbackUrl", "http://spring.io")
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.id", is(5))
        ).andExpect(
                jsonPath("$.success", is(true))
        );
    }

    @Test
    public void nonAuthorized() throws Exception {
        mockMvc.perform(
                post("/add")
                .param("method", "text")
                .param("text", "Won't reach")
        ).andExpect(
                status().isUnauthorized()
        );
    }


}
