package io.riguron.captcha.repository;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphs;
import io.riguron.captcha.type.Captcha;
import io.riguron.captcha.type.NormalCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class CaptchaRepositoryTest extends RepositoryTest {

    private Consumer<Captcha> EMPTY_ACTION = o -> {
    };

    private Captcha originalCaptcha;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private CaptchaRepository captchaRepository;

    @Before
    public void persist() {
        runTransaction(() -> save(1, captcha -> log.info("Saved initial captcha")));
    }

    @Test
    public void loadExistingCaptcha() {
        runTransaction(() -> fetchAndCompare(originalCaptcha.getId(), id -> captchaRepository.findById(id), EMPTY_ACTION));
    }

    @Test
    public void testLoadWithNoSolvers() {
        runTransaction(() -> fetchAndCompare(1, identifier -> captchaRepository.findById(identifier,
                EntityGraphs.named("solutions")), captcha -> {
            assertEquals(0, captcha.getSolutions().size());
        }));
    }

    @Test
    public void testLoadWithExistingSolvers() {

        runTransaction(() -> save(2, captcha -> {
            captcha.addSolution("a");
            captcha.addSolution("b");
        }));

        runTransaction(() -> fetchAndCompare(originalCaptcha.getId(), identifier -> captchaRepository.findById(identifier, EntityGraphs.named("solutions")),
                captcha -> assertTrue(captcha.getSolutions().containsAll(Arrays.asList("a", "b")))));
    }

    private void fetchAndCompare(int captchaId, Function<Integer, Optional<Captcha>> optionalSupplier, Consumer<Captcha> postAssertions) {
        reset();
        Optional<Captcha> optional = optionalSupplier.apply(captchaId);

        assertTrue(optional.isPresent());
        assertEquals(this.originalCaptcha, optional.get());
        postAssertions.accept(optional.get());
        assertSelectCount(1);
    }

    private void save(int originatorId, Consumer<Captcha> postAction) {
        Captcha newCaptcha = new NormalCaptcha(originatorId, "callbackURL", new byte[]{1, 2, 3});
        postAction.accept(newCaptcha);
        reset();
        this.originalCaptcha = captchaRepository.save(newCaptcha);
        assertInsertCount(2);

    }

    @After
    public void cleanUp() {
        this.captchaRepository.deleteAll();
    }


    private void runTransaction(Runnable runnable) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                runnable.run();
            }
        });
    }


}