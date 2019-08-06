package me.nextgeneric.captcha.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class NormalCaptchaTest {

    @Test
    public void testEquality() {
        NormalCaptcha normalCaptchaOne  = new NormalCaptcha(1, "callback", new byte[]{1,2,3});
        NormalCaptcha normalCaptchaTwo  = new NormalCaptcha(1, "callback", new byte[]{1,2,3});

        assertEquals(normalCaptchaOne.getContents(), normalCaptchaTwo.getContents());
        assertEquals(normalCaptchaOne.getType(), normalCaptchaTwo.getType());
        assertEquals(normalCaptchaOne.getAcceptedSolutions(), normalCaptchaTwo.getAcceptedSolutions());
        assertEquals(normalCaptchaOne.getCallbackUrl(), normalCaptchaTwo.getCallbackUrl());
        assertEquals(normalCaptchaOne.getRegistrationDate(), normalCaptchaTwo.getRegistrationDate());

        assertEquals(normalCaptchaOne, normalCaptchaTwo);
    }
}