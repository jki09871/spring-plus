package org.example.expert.domain.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResults;
import org.example.expert.domain.todo.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todos")
    public ResponseEntity<TodoSaveResponse> saveTodo(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody TodoSaveRequest todoSaveRequest
    ) {
        return ResponseEntity.ok(todoService.saveTodo(authUser, todoSaveRequest));
    }

    @GetMapping("/todos")
    public ResponseEntity<Page<TodoResponse>> getTodos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false)LocalDateTime startTime,
            @RequestParam(required = false)LocalDateTime endTime
            ) {
        return ResponseEntity.ok(todoService.getTodos(page, size, keyword, startTime, endTime));
    }

    @GetMapping("/todos/search")
    public ResponseEntity<Page<TodoSearchResults>> getTodoSearchResults(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, name = "title") String title,
            @RequestParam(required = false)LocalDateTime startTime,
            @RequestParam(required = false)LocalDateTime endTime,
            @RequestParam(required = false) String nickname
    ) {
        System.out.println("title = " + title);
        return ResponseEntity.ok(todoService.getTodoSearchResults(page, size, title, startTime, endTime, nickname));
    }

    @GetMapping("/todos/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable long todoId) {
        return ResponseEntity.ok(todoService.getTodo(todoId));
    }
}
