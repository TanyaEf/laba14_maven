package com.nixsolutions.efimenko.laba13;

import java.util.Date;

public class User {

    private Long id;
    private Long role;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Date birthDate;

    @SuppressWarnings("deprecation")
    public User() {
        this(0L, "", "", "", "", "", 0L, new Date(1700, 01, 01));
    }

    public User(Long id, String firstName, 
            String lastName, String email,
            String login, String password,
            Long role, Date birthDate) {
        super();
        this.id = id;
        this.role = role;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "id: " + this.id + " role: " + this.role + " login: "
                + this.login + " firs name: " + this.firstName + " last name: "
                + this.lastName + " e-mail: " + this.email + " birthday: "
                + this.birthDate;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long newId) {
        this.id = newId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String newLogin) {
        this.login = newLogin;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String newFirstName) {
        this.firstName = newFirstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String newLastName) {
        this.lastName = newLastName;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(Date newDate) {
        this.birthDate = newDate;
    }

    public void setRole(long newRole) {
        this.role = newRole;
    }

    public long getRole() {
        return this.role;
    }
}
