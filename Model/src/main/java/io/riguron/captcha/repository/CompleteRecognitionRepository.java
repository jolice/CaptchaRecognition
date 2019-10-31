package io.riguron.captcha.repository;

import io.riguron.captcha.user.CompleteRecognition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompleteRecognitionRepository extends CrudRepository<CompleteRecognition, Integer> {

    @Query("select c from CompleteRecognition c JOIN c.profile cp WHERE cp.id = :id")
    Optional<CompleteRecognition> findByUserId(@Param("id") Integer id);

}
