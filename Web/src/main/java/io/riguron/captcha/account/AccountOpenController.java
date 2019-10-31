package io.riguron.captcha.account;

import io.riguron.captcha.user.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountOpenController {

    private final UserProfileService userProfileService;

    @Autowired
    public AccountOpenController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping("/register")
    public AccountOpenResponse openAccount(@RequestParam String login, @RequestParam String password) {

        userProfileService.register(login, password);
        return new AccountOpenResponse(login);
    }
}
