package com.example.todo1.controller;


import com.example.todo1.dto.TodoRequestDto;
import com.example.todo1.dto.TodoResponseDto;
import com.example.todo1.service.TodoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;
    private final JdbcTemplate jdbcTemplate;

    public TodoController(TodoService todoService, JdbcTemplate jdbcTemplate) {
        this.todoService = todoService;
        this.jdbcTemplate = jdbcTemplate;
    }

    // 일정 생성
    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo (@RequestBody TodoRequestDto dto) {
        return new ResponseEntity<>(todoService.saveTodo(dto), HttpStatus.CREATED);
    }

    // 일정 전체 조회
    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> findAllTodos() {
        List<TodoResponseDto> allTodos = todoService.findAllTodos();
        return new ResponseEntity<>(allTodos, HttpStatus.OK);
    }

    // 일정 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> findTodoById(@PathVariable Long id) {
        return new ResponseEntity<>(todoService.findTodoById(id), HttpStatus.OK);
    }

    /**
     * 일정 조건 조회
     * @param user
     * @param updateAt
     * @return
     */
    @GetMapping("/or")
    public ResponseEntity<List<TodoResponseDto>> findTodoByUserOrUpdateAt(
            @RequestParam(required = false) String user,
            @RequestParam(required = false) LocalDate updateAt
            ) {

            List<TodoResponseDto> findTodo = todoService.findTodoByUserOrUpdateAt(user, updateAt);

        if (findTodo.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(findTodo);
    }

    // 일정 및 사용자명 수정
    @PatchMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(
            @PathVariable Long id,
            @RequestBody TodoRequestDto dto
    ) {
        TodoResponseDto updateDto = todoService.updateTodo(id, dto.getUser(), dto.getTodo(), dto.getPassword(), dto.getUpdateAt());

        return ResponseEntity.ok(updateDto);
    }

    // 일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long id,
            @RequestBody TodoRequestDto dto
    ) {
        todoService.deleteTodo(id, dto.getPassword());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
