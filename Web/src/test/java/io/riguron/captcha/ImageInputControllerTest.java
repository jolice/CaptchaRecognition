package io.riguron.captcha;

import io.riguron.captcha.input.image.Base64InputController;
import io.riguron.captcha.input.image.MultipartInputController;
import io.riguron.captcha.response.CaptchaAddResponse;
import io.riguron.captcha.input.image.ImageCaptchaRegistration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {Base64InputController.class, MultipartInputController.class})
public class ImageInputControllerTest {

    private static final int ADDED_CAPTCHA_ID = 2933445;

    @MockBean
    private ImageCaptchaRegistration imageCaptchaRegistration;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        when(imageCaptchaRegistration.produce(any(), any())).thenReturn(new CaptchaAddResponse(true, ADDED_CAPTCHA_ID));
    }

    @Test
    public void whenAddCaptchaBase64ThenAdded() throws Exception {

        String base64 = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get("src", "test", "resources").resolve("captcha.png")));

        this.mockMvc.perform(
                post("/add")
                        .param("method", "base64")
                        .param("body", base64)
                        .param("token", WebSuiteTestConfig.USER_TOKEN)
        ).andExpect(
                status().isOk()
        ).andDo(
                print()
        ).andExpect(
                jsonPath("$.id", is(ADDED_CAPTCHA_ID))
        );

        verify(imageCaptchaRegistration).produce(argThat(captchaUpload -> {
            try {
                return Arrays.equals(captchaUpload.upload(), Base64.getDecoder().decode(base64));
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }), any());
    }

    @Test
    public void whenAddCaptchaMultipartThenAdded() throws Exception {

        final byte[] contents = new byte[]{1, 2, 3};

        this.mockMvc.perform(
                multipart("/add")
                        .file("file", contents)
                        .param("token", WebSuiteTestConfig.USER_TOKEN)
                        .param("method", "post")
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.id", is(ADDED_CAPTCHA_ID))
        );

        verify(imageCaptchaRegistration).produce(argThat(captchaUpload -> {
            try {
                return Arrays.equals(captchaUpload.upload(), contents);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }), any());

    }


}