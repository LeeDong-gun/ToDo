package com.example.todo1.dto;


import com.example.todo1.entity.Todo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class TodoResponseDto {
    private Long id;
    private String user;
    private String todo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;

    public TodoResponseDto(Todo todo) {
        this.id = todo.getId();
        this.user = todo.getUser();
        this.todo = todo.getTodo();
        this.createAt = todo.getCreateAt();
        this.updateAt = todo.getUpdateAt();
    }
}
