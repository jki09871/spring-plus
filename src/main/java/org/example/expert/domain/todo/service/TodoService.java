package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResults;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoFindByIdWithUserRepository;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.todo.repository.TodoSearchResultsRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    private final TodoFindByIdWithUserRepository todoFindByIdWithUserRepository;

    private final TodoSearchResultsRepository todoSearchResultsRepository;

    @Transactional
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        System.out.println("authUser = " + authUser);
        User user = User.fromAuthUser(authUser);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail())
        );
    }

    public Page<TodoSearchResults> getTodoSearchResults(int page, int size, String title, LocalDateTime startTime, LocalDateTime endTime, String nickname) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return todoSearchResultsRepository.searchTodos(pageable, title, startTime, endTime, nickname);

    }

    public Page<TodoResponse> getTodos(int page, int size, String keyword, LocalDateTime startTime, LocalDateTime endTime) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Todo> todos = todoRepository.searchTodos(keyword, startTime, endTime, pageable);


        // Todo 엔티티를 TodoResponse로 변환
        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }


    public TodoResponse getTodo(long todoId) {
        Todo todo = todoFindByIdWithUserRepository.findByIdWithUser(todoId);


        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }
}
