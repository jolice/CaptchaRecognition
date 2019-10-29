package io.riguron.captcha.account;

import io.riguron.captcha.user.UserProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    private UserProfileService userProfileService;

    public AccountController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/open")
    public String openAccount(String login) {
        return userProfileService.register(login).getApiKey();
    }

}
