package com.example.todo1.repository;

import com.example.todo1.dto.TodoResponseDto;
import com.example.todo1.entity.Todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    TodoResponseDto saveTodo(Todo todo);
    List<TodoResponseDto> findAllTodos();
    Optional<Todo> findTodoById(Long id);
    Todo findTodoByIdOrElseThrow(Long id);
    List<TodoResponseDto> findTodoByUserOrUpdateTime(String user, LocalDate updateAt);
    int updateTodo(Long id, String user, String todo, LocalDateTime updateAt);
    int deleteTodo(Long id);
}
