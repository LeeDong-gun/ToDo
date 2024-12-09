package com.example.todo1.service;

import com.example.todo1.dto.TodoRequestDto;
import com.example.todo1.dto.TodoResponseDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TodoService {
    TodoResponseDto saveTodo(TodoRequestDto dto);
    List<TodoResponseDto> findAllTodos();
    TodoResponseDto findTodoById(Long id);
    List<TodoResponseDto> findTodoByUserOrUpdateAt(String user, LocalDate update);
    TodoResponseDto updateTodo(Long id, String user, String todo, String password, LocalDateTime updateAt);
    void deleteTodo(Long id, String password);
}
