package com.webbook.webbook.repository;



import com.webbook.webbook.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends PagingAndSortingRepository<User,Long> {

    Optional<User> findByEmail(String userEmail);
    boolean existsByEmail(String email);
    Optional<User> findByActivationCode(String code);

//    @Query("SELECT u.id FROM User u WHERE u.email=:userEmail")
//    Long findIdByEmail(@Param("userEmail")String userEmail);


}
