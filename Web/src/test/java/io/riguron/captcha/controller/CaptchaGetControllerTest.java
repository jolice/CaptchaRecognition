package io.riguron.captcha.controller;

import io.riguron.captcha.CaptchaDistributionService;
import io.riguron.captcha.TestConfiguration;
import io.riguron.captcha.type.Captcha;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CaptchaGetController.class)
public class CaptchaGetControllerTest {

    @MockBean
    private CaptchaDistributionService captchaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails
    public void getNextCaptcha() throws Exception {


        String base64 = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get("src", "test", "resources").resolve("captcha.png")));

        when(captchaService.takeNextCaptcha(anyInt())).thenAnswer(
                invocationOnMock -> {
                    Captcha captcha = mock(Captcha.class);
                    when(captcha.getContents()).thenReturn(new String(Base64.getDecoder().decode(base64)));
                    return Optional.of(captcha);
                }
        );

        this.mockMvc.perform(
                get("/get")
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        );
    }
}