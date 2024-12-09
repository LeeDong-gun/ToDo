package com.example.todo1.service;


import com.example.todo1.dto.TodoRequestDto;
import com.example.todo1.dto.TodoResponseDto;
import com.example.todo1.entity.Todo;
import com.example.todo1.repository.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;

    public TodoServiceImpl (TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // 일정 생성
    @Override
    public TodoResponseDto saveTodo(TodoRequestDto dto) {
        Todo todo = new Todo(dto.getUser(), dto.getTodo(), dto.getPassword(), dto.getCreateAt(), dto.getUpdateAt());
        return todoRepository.saveTodo(todo);
    }

    // 일정 전체 조회
    @Override
    public List<TodoResponseDto> findAllTodos() {
        List<TodoResponseDto> allTodos = todoRepository.findAllTodos();
        return allTodos;
    }

    // 일정 단건 조회
    @Override
    public TodoResponseDto findTodoById(Long id) {
        Todo todo = todoRepository.findTodoByIdOrElseThrow(id);
        return new TodoResponseDto(todo);
    }

    // 일정 조건 조회
    @Override
    public List<TodoResponseDto> findTodoByUserOrUpdateAt(String user, LocalDate update) {
        List<TodoResponseDto> userOrUpdateAt = todoRepository.findTodoByUserOrUpdateTime(user, update);
        return userOrUpdateAt;
    }

    // 작성자명 및 일정 수정
    @Transactional
    @Override
    public TodoResponseDto updateTodo(Long id, String user, String todo, String password, LocalDateTime updateAt) {
        //수정 할 일정 불러오기
        Todo find = todoRepository.findTodoById(id).orElseThrow(() -> new IllegalArgumentException(
                "A schedule with the corresponding id could not be found."));

        // 비밀번호 검증
        if (!find.getPassword().equals(password)) {
            throw new IllegalArgumentException("Password does not match.");
        }

        if (user == null && todo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The User and todo are required values.");
        }

        int updateRow = todoRepository.updateTodo(id, user, todo, updateAt);

        if (updateRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        Todo setTodo = todoRepository.findTodoByIdOrElseThrow(id);

        return new TodoResponseDto(setTodo);
    }

    // 일정 삭제
    @Override
    public void deleteTodo(Long id, String password) {
        //삭제 할 일정 불러오기
        Todo find = todoRepository.findTodoById(id).orElseThrow(() -> new IllegalArgumentException(
                "A schedule with the corresponding id could not be found."));

        // 비밀번호 검증
        if (!find.getPassword().equals(password)) {
            throw new IllegalArgumentException("Password does not match.");
        }

        int deleteTodo = todoRepository.deleteTodo(id);

        if (deleteTodo == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
    }
}
