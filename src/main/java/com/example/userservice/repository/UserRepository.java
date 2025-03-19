package com.example.userservice.repository;

import com.example.userservice.entities.UserInfoDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserInfoDto, String> {
    public UserInfoDto findByUserId(String userId);
}
