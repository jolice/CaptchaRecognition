package io.riguron.captcha.response;

import lombok.*;

@Data
@NoArgsConstructor
public class Response {

    private boolean success;

    public Response(boolean success) {
        this.success = success;
    }
}
