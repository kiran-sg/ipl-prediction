package com.ipl.prediction.iplprediction.repository;

import com.ipl.prediction.iplprediction.entity.IplUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<IplUser, Long> {

    @Query("SELECT u FROM ipl_users u WHERE u.userId = ?1")
    IplUser findByUserId(String userId);

}
