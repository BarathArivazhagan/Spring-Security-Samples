package com.barath.app.repository;

import com.barath.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUserNameAndPassword(String userName,String password);

    User findByUserName(String userName);
}
