package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoSearchResults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TodoSearchResultsRepository {

    Page<TodoSearchResults> searchTodos(Pageable pageable, String title, LocalDateTime startTime, LocalDateTime endTime, String nickname);
}
