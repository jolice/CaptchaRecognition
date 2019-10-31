package io.riguron.captcha.repository;

import io.riguron.captcha.user.UserBalance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserBalanceRepository extends CrudRepository<UserBalance, Integer> {

    @Query("select ub from UserBalance ub JOIN ub.userProfile p WHERE p.id = :userId")
    UserBalance findByUserId(@Param("userId") Integer userId);


}
