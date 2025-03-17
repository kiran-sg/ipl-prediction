package com.ipl.prediction.iplprediction.repository;

import com.ipl.prediction.iplprediction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM ipl_user u WHERE u.userId = ?1")
    User findByUserId(String userId);

}
