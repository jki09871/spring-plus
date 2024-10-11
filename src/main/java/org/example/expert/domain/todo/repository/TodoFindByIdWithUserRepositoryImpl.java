package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class TodoFindByIdWithUserRepositoryImpl implements TodoFindByIdWithUserRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(long todoId) {
        Todo result = jpaQueryFactory
                .select(todo)
                .from(todo)
                .leftJoin(todo.user).fetchJoin()
                .where(
                        todoIdEq(todoId)
                ).fetchOne();

        return Optional.ofNullable(result);
    }

    private BooleanExpression todoIdEq(Long todoId) {
        return todoId != null ? todo.id.eq(todoId) : null;
    }
}
