package com.homework.user_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table (name = "myusers")
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column (name = "name")
    private String name;

    @Setter
    @Column (name = "email")
    private String email;

    @Setter
    @Column (name = "age")
    private Integer age;

    @Column (name = "created_at")
    private LocalDateTime createdAt;

    public User() {}

    public User(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("/%d, %s, %s, %d, ", id, name, email, age) + createdAt + "/";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id &&
                age == user.age &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age);
    }
}