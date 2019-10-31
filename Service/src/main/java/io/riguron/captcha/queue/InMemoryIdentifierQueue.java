package io.riguron.captcha.queue;

import io.riguron.captcha.queue.IdentifierQueue;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class InMemoryIdentifierQueue implements IdentifierQueue {

    private Deque<Integer> captchaDeque = new LinkedBlockingDeque<>();

    @Override
    public void pushFront(Integer id) {
        this.captchaDeque.addFirst(id);
    }

    @Override
    public void pushBack(Integer id) {
        this.captchaDeque.addLast(id);
    }

    @Override
    public Optional<Integer> pollFirst() {
        return Optional.ofNullable(this.captchaDeque.poll());
    }

}
