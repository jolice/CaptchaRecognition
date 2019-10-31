package io.riguron.captcha.solve;

import io.riguron.captcha.CaptchaStatus;
import io.riguron.captcha.queue.IdentifierQueue;
import io.riguron.captcha.repository.CaptchaRepository;
import io.riguron.captcha.type.Captcha;
import io.riguron.captcha.type.CaptchaType;
import io.riguron.captcha.user.CompleteRecognition;
import io.riguron.captcha.user.UserProfile;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CompleteRecognitionServiceTest {

    private static final int CAPTCHA_ID = 1;
    private static final int SOLVER_ID = 2;
    private Captcha captcha = spy(new TestCaptcha());
    private CompleteRecognition completeRecognition = spy(new CompleteRecognition(mock(UserProfile.class), 3, 7, 5));
    private CaptchaSolution captchaSolution = new CaptchaSolution(CAPTCHA_ID, SOLVER_ID, "solution");

    private CompleteRecognitionService completeRecognitionService;

    @Before
    public void prepare() {
        CaptchaSolvingValidator captchaSolvingValidator = mock(CaptchaSolvingValidator.class);
        SuccessfulSolving successfulSolving = mock(SuccessfulSolving.class);
        IdentifierQueue identifierQueue = mock(IdentifierQueue.class);
        CaptchaRepository captchaRepository = mock(CaptchaRepository.class);
        when(captchaRepository.findById(eq(CAPTCHA_ID), any())).thenReturn(Optional.of(captcha));
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