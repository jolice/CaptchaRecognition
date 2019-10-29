package io.riguron.captcha;

import io.riguron.captcha.exception.InactiveCaptchaException;
import io.riguron.captcha.exception.InvalidSolverException;
import io.riguron.captcha.solve.CaptchaSolvingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static io.riguron.captcha.WebSuiteTestConfig.USER_TOKEN;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CaptchaSolveController.class)
public class CaptchaSolveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaptchaSolvingService captchaService;

    @Test
    public void solve() throws Exception {
        postAndAssert(true, "accepted");
    }

    @Test
    public void whenInactiveCaptchaError() throws Exception {
        doThrow(InactiveCaptchaException.class).when(captchaService).solveCaptcha(ArgumentMatchers.any());
        postAndAssert(false, "unsolved");
    }

    @Test
    public void whenInvalidSolverError() throws Exception {
        doThrow(InvalidSolverException.class).when(captchaService).solveCaptcha(ArgumentMatchers.any());
        postAndAssert(false, "expired");
    }

    private void postAndAssert(boolean success, String result) throws Exception {
        this.mockMvc.perform(
                post("/solve")
                        .param("token", USER_TOKEN)
                        .param("captchaId", "1")
                        .param("solution", "abcde")
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.success", is(success))
        ).andExpect(
                jsonPath("$.result", containsString(result))
        );
    }
}