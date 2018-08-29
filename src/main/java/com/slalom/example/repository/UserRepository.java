package com.slalom.example.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.slalom.example.domain.User;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends Repository<User, Long>, JpaSpecificationExecutor<User> {
    User findByUsername(String username);

    User save(User user);

    @Transactional
    Long deleteByUsername(String username);

    /**
     * Creating Database Queries From Method Names
     */
    List<User> findByFirstNameOrLastName(String firstName, String lastName);

    Long countByFirstNameOrLastName(String firstName, String lastName);

    List<User> findDistinctByFirstNameOrLastName(String firstName, String lastName);
    /**
     * Creating Database Queries With the @Query Annotation
     */
    @Query("select u from User u where u.phone like '%648%'")
    List<User> searchByPhonePart();

    @Query("select u from User u where " +
            "lower(u.username) like lower(concat('%',:searchTerm,'%'))")
    List<User> searchByUsernamePart(@Param("searchTerm") String searchTerm);

    /**
     * Creating Database Queries With Named Queries
     */
    @Query(nativeQuery = true)
    List<User> findBySearchTermNamedNative();

    @Query
    List<User> findBySearchTermNamed(@Param("searchTerm") String searchTerm);

}
