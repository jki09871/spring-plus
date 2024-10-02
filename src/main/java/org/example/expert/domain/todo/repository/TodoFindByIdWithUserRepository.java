package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.stereotype.Repository;

public interface TodoFindByIdWithUserRepository {
    Todo findByIdWithUser(long todoId);
}
