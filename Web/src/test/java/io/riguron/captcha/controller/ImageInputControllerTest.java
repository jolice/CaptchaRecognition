package io.riguron.captcha.controller;

import io.riguron.captcha.TestConfiguration;
import io.riguron.captcha.controller.image.Base64InputController;
import io.riguron.captcha.controller.image.MultipartInputController;
import io.riguron.captcha.response.CaptchaAddResponse;
import io.riguron.captcha.controller.image.ImageCaptchaRegistration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
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

    private static final int ADDED_CAPTCHA_ID = 1;

    @MockBean
    private ImageCaptchaRegistration imageCaptchaRegistration;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        when(imageCaptchaRegistration.produce(any(), any(), anyInt())).thenReturn(new CaptchaAddResponse(true, ADDED_CAPTCHA_ID));
    }

    @Test
    @WithUserDetails
    public void whenAddCaptchaBase64ThenAdded() throws Exception {

        this.mockMvc.perform(
                post("/add")
                        .param("method", "base64")
                        .param("body", base64())

        ).andExpect(
                status().isOk()
        ).andDo(
                print()
        ).andExpect(
                jsonPath("$.id", is(ADDED_CAPTCHA_ID))
        );

        verify(imageCaptchaRegistration).produce(argThat(captchaUpload -> {
            try {
                return Arrays.equals(captchaUpload.upload(), Base64.getDecoder().decode(base64()));
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }), any(), anyInt());
    }

    @Test
    public void notAuthorizedBase64() throws Exception {
        this.mockMvc.perform(
                post("/add")
                        .param("method", "base64")
                        .param("body", base64())

        ).andExpect(
                status().isUnauthorized()
        ).andDo(
                print()
        );
    }

    @Test
    public void notAuthorizedMultipart() throws Exception {
        this.mockMvc.perform(
                multipart("/add")
                        .file("file", contents())
                        .param("method", "post")
        ).andExpect(
                status().isUnauthorized()
        );
    }


    @Test
    @WithUserDetails
    public void whenAddCaptchaMultipartThenAdded() throws Exception {
        this.mockMvc.perform(
                multipart("/add")
                        .file("file", contents())
                        .param("method", "post")
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.id", is(ADDED_CAPTCHA_ID))
        );

        verify(imageCaptchaRegistration).produce(argThat(captchaUpload -> {
            try {
                return Arrays.equals(captchaUpload.upload(), contents());
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }), any(), anyInt());
    }

    private String base64() {
        try {
            return Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get("src", "test", "resources").resolve("captcha.png")));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private byte[] contents() {
        return new byte[]{1, 2, 3};
    }


}