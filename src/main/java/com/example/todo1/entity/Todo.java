package com.example.todo1.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
public class Todo {

    private Long id;
    private String user;
    private String todo;
    private String password;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public Todo(String user, String todo, String password, LocalDateTime createAt, LocalDateTime updateAt) {
        this.user = user;
        this.todo = todo;
        this.password = password;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public Todo(Long id, String user, String todo, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.user = user;
        this.todo = todo;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
