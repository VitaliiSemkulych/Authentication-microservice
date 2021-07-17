package com.webbook.resourceserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "users")
public class User extends  AbstractEntity{

    @Column(unique = false, nullable = false)
    private String userName;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(name = "phone",nullable = true )
    private String phoneNumber;

    @Transient
    private boolean active;


    public User() {
    }

    public User(String userName, String email, String phoneNumber, boolean active) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.active = active;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public String getUserName() {
        return this.userName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;
        final User other = (User) o;
        if (!other.canEqual((Object) this)) return false;
        if (!super.equals(o)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    public int hashCode() {
        int result = super.hashCode();
        return result;
    }

    public String toString() {
        return "User(userName=" + this.getUserName() + ", email=" + this.getEmail() + ")";
    }

    public static class UserBuilder {
        private String userName;
        private String email;
        private String phoneNumber;
        private boolean active;

        UserBuilder() {
        }

        public UserBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public User build() {
            return new User(userName, email, phoneNumber, active);
        }

        public String toString() {
            return "User.UserBuilder(userName=" + this.userName + ", email=" + this.email + ", phoneNumber=" + this.phoneNumber + ", active=" + this.active + ")";
        }
    }
}
