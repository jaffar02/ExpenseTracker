package com.example.AuthService.repository;

import com.example.AuthService.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo, Integer> {

    public UserInfo findByUsername(String username);
}
