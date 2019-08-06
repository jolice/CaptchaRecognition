package me.nextgeneric.captcha.solve;

import me.nextgeneric.captcha.CaptchaStatus;
import me.nextgeneric.captcha.IdentifierQueue;
import me.nextgeneric.captcha.repository.CaptchaRepository;
import me.nextgeneric.captcha.type.Captcha;
import me.nextgeneric.captcha.type.CaptchaType;
import me.nextgeneric.captcha.user.CompleteRecognition;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CompleteRecognitionServiceTest {

    private static final int CAPTCHA_ID = 1;
    private static final int SOLVER_ID = 2;
    private Captcha captcha = spy(new TestCaptcha());
    private CompleteRecognition completeRecognition = spy(new CompleteRecognition(true, 3, 7, 5));
    private CaptchaSolution captchaSolution = new CaptchaSolution(CAPTCHA_ID, SOLVER_ID, "solution");

    private CompleteRecognitionService completeRecognitionService;

    @Before
    public void prepare() {
        CaptchaSolvingValidator captchaSolvingValidator = mock(CaptchaSolvingValidator.class);
        SuccessfulSolving successfulSolving = mock(SuccessfulSolving.class);
        IdentifierQueue identifierQueue = mock(IdentifierQueue.class);
        CaptchaRepository captchaRepository = mock(CaptchaRepository.class);
        when(captchaRepository.findByIdWithSolutions(eq(CAPTCHA_ID))).thenReturn(Optional.of(captcha));
        this.completeRecognitionService = new CompleteRecognitionService(captchaSolvingValidator, successfulSolving, identifierQueue, captchaRepository);
    }

    @Test
    public void whenAcceptLessThanMinBoundThenJustAccepted() {

        // Two times, limit is three
        completeRecognitionService.proceedCompleteRecognition(captchaSolution, completeRecognition);
        completeRecognitionService.proceedCompleteRecognition(captchaSolution, completeRecognition);
        assertEquals(CaptchaStatus.PROCESSING, captcha.getCaptchaStatus());
    }


    private static class TestCaptcha extends Captcha {

        @Override
        public String getContents() {
            return "contents";
        }

        @Override
        public CaptchaType getType() {
            return CaptchaType.IMAGE;
        }
    }
}