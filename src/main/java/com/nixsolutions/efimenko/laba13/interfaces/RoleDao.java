package com.nixsolutions.efimenko.laba13.interfaces;

import com.nixsolutions.efimenko.laba13.Role;

public interface RoleDao {

    void create(Role role) throws Throwable;

    void update(Role role) throws Throwable;

    void remove(Role role) throws Throwable;

    Role findByName(String name) throws Throwable;
}
