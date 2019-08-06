package me.nextgeneric.captcha;

import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.BlockingDeque;
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
