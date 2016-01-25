package com.nixsolutions.efimenko.laba13.interfaces;

import java.util.List;

import com.nixsolutions.efimenko.laba13.User;

public interface UserDao {

    void create(User user) throws Throwable;

    void update(User user) throws Throwable;

    void remove(User user) throws Throwable;

    List<User> findAll() throws Throwable;

    User findByLogin(String login) throws Throwable;

    User findByEmail(String email) throws Throwable;
}
