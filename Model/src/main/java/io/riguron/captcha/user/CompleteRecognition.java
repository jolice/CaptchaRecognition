package io.riguron.captcha.user;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Data
@NoArgsConstructor
public class CompleteRecognition {

    @Id
    private Integer id;

    public CompleteRecognition(UserProfile profile, int minimumAttempts, int maximumAttempts, int minimumMatches) {
        this.profile = profile;
        this.minimumAttempts = minimumAttempts;
        this.maximumAttempts = maximumAttempts;
        this.minimumMatches = minimumMatches;
    }

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private UserProfile profile;

    @Column
    @Min(2)
    @Max(7)
    private int minimumAttempts;

    @Column
    @Min(2)
    @Max(10)
    private int maximumAttempts;

    @Column
    @Min(2)
    @Max(5)
    private int minimumMatches;

    @AssertTrue
    public boolean checkBounds() {
        return maximumAttempts > minimumAttempts && minimumMatches <= minimumAttempts;
    }

}


