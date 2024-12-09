package com.example.todo1.repository;


import com.example.todo1.dto.TodoResponseDto;
import com.example.todo1.entity.Todo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class JdbcTemplateTodoRepository implements TodoRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateTodoRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 일정생성
    @Override
    public TodoResponseDto saveTodo(Todo todo) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("todo").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user", todo.getUser());
        parameters.put("todo", todo.getTodo());
        parameters.put("password", todo.getPassword());
        parameters.put("create_at", todo.getCreateAt());
        parameters.put("update_at", todo.getUpdateAt());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return new TodoResponseDto(key.longValue(), todo.getUser(), todo.getTodo(), todo.getCreateAt(), todo.getUpdateAt());
    }

    // 일정 전체 조회
    @Override
    public List<TodoResponseDto> findAllTodos() {
        return jdbcTemplate.query("select * from todo", todoRowMapper());
    }

    private RowMapper<TodoResponseDto> todoRowMapper() {
        return new RowMapper<TodoResponseDto>() {
            @Override
            public TodoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new TodoResponseDto(
                        rs.getLong("id"),
                        rs.getString("user"),
                        rs.getString("todo"),
                        rs.getTimestamp("create_at").toLocalDateTime(),
                        rs.getTimestamp("update_at").toLocalDateTime()
                );
            }
        };
    }

    // 일정 단건 조회
    @Override
    public Optional<Todo> findTodoById(Long id) {
        List<Todo> result = jdbcTemplate.query("select * from todo where id = ?", todoRowMapperV2(), id);
        return result.stream().findAny();
    }

    @Override
    public Todo findTodoByIdOrElseThrow(Long id) {
        List<Todo> result = jdbcTemplate.query("select * from todo where id = ?", todoRowMapperV2(), id);

        return result.stream().findAny().orElseThrow();
    }

    private RowMapper<Todo> todoRowMapperV2() {
        return new RowMapper<Todo>() {
            @Override
            public Todo mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Todo(
                        rs.getLong("id"),
                        rs.getString("user"),
                        rs.getString("todo"),
                        rs.getString("password"),
                        rs.getTimestamp("create_at").toLocalDateTime(),
                        rs.getTimestamp("update_at").toLocalDateTime()
                );
            }
        };
    }

    // 일정 조건 조회
    @Override
    public List<TodoResponseDto> findTodoByUserOrUpdateTime(String user, LocalDate updateAt) {
        String query = "select * from todo where";
        List<Object> params = new ArrayList<>();

        if (user != null) {
            query += " user = ?";
            params.add(user);
        }
        if (updateAt != null) {
            if (!params.isEmpty()) {
                query += " and ";
            }
            query += " date(update_at) = ?";
            params.add(updateAt);
        }
        if (params.isEmpty()) {
            return Collections.emptyList();
        }
        return jdbcTemplate.query(query, todoRowMapper(), params.toArray());
    }

    // 일정 및 작성자명 수정
    @Override
    public int updateTodo(Long id, String user, String todo, LocalDateTime updateAt) {

        if (user != null) {
            jdbcTemplate.update("update todo set user = ?, update_at = ? where id = ?", user, updateAt, id);
        }

        if (todo != null) {
            jdbcTemplate.update("update todo set todo = ?, update_at = ? where id = ?", todo, updateAt, id);
        }
        return 1;
    }

    // 일정 삭제
    @Override
    public int deleteTodo(Long id) {
        return jdbcTemplate.update("delete from todo where id = ?", id);
    }

}


