package me.nextgeneric.captcha.solve;

import me.nextgeneric.captcha.repository.CaptchaRepository;
import me.nextgeneric.captcha.type.Captcha;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class CaptchaImmediateSolverTest {

    private static final int CAPTCHA_ID = 1;

    private static final int SOLVER_ID = 2;

    @Test
    public void whenSolveImmediatelyThenCallsDelegated() {


        CaptchaRepository captchaRepository = mock(CaptchaRepository.class);
        CaptchaSolvingValidator captchaSolvingValidator = mock(CaptchaSolvingValidator.class);
        SuccessfulSolving successfulSolving = mock(SuccessfulSolving.class);

        Captcha captcha = mock(Captcha.class);

        when(captchaRepository.findById(eq(CAPTCHA_ID))).thenReturn(Optional.of(captcha));

        CaptchaImmediateSolver captchaImmediateSolver = new CaptchaImmediateSolver(captchaRepository, captchaSolvingValidator, successfulSolving);
        CaptchaSolution captchaSolution = new CaptchaSolution(CAPTCHA_ID, SOLVER_ID, "solution");


        captchaImmediateSolver.solveImmediately(captchaSolution);

        verify(captchaRepository).findById(eq(CAPTCHA_ID));
        verify(captchaSolvingValidator).validateBeforeAcceptingSolution(eq(captcha), eq(SOLVER_ID));
        verify(successfulSolving).successfulRecognition(eq(captcha), eq("solution"));
    }
}