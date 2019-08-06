package me.nextgeneric.captcha.type;

import lombok.*;
import me.nextgeneric.captcha.CaptchaStatus;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.BiFunction;

@Entity
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public abstract class Captcha {

    @Id
    @Column(updatable = false, unique = true, name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "status")
    @Setter
    @Enumerated(EnumType.STRING)
    private CaptchaStatus captchaStatus = CaptchaStatus.PROCESSING;

    @Column(nullable = false, name = "registration_date")
    @CreationTimestamp
    @EqualsAndHashCode.Include
    private Timestamp registrationDate;

    @Version
    @Column(name = "version")
    @EqualsAndHashCode.Include
    private long version;

    @Column(nullable = false, updatable = false, name = "originator_id")
    @EqualsAndHashCode.Include
    private int originatorId;

    @Column(updatable = false, name = "callback_url")
    private String callbackUrl;

    @ElementCollection(fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Map<Integer, Boolean> solvers = new HashMap<>();

    @Column(name = "timeout_attempts")
    @Setter
    private int timeoutAttempts;

    @ElementCollection(fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Collection<String> solutions = new ArrayList<>();

    @Setter
    @EqualsAndHashCode.Include
    private String finalSolution;

    public Captcha(int originatorId, String callbackUrl) {
        this.originatorId = originatorId;
        this.callbackUrl = callbackUrl;
    }

    public void addSolution(String solution) {
        this.solutions.add(solution);
    }

    public void addSolver(Integer solver) {
        this.solvers.put(solver, false);
    }

    public boolean hasSolved(Integer solver) {
        return this.solvers.getOrDefault(solver, false);
    }

    public void removeSolver(Integer solver) {
        this.solvers.remove(solver);
    }

    public boolean hasNotSolver(Integer solverId) {
        return !this.solvers.containsKey(solverId);
    }

    public Collection<String> getSolutions() {
        return Collections.unmodifiableCollection(this.solutions);
    }

    public Set<Integer> getSolvers() {
        return this.solvers.keySet();
    }

    public int getAcceptedSolutions() {
        return solutions.size();
    }

    public abstract String getContents();

    public abstract CaptchaType getType();

}
