package io.riguron.captcha.queue;

import java.util.Optional;

public interface IdentifierQueue {

    void pushFront(Integer id);

    void pushBack(Integer id);

    Optional<Integer> pollFirst();

}
