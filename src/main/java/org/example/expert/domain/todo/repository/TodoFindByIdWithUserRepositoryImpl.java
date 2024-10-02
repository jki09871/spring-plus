package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import static org.example.expert.domain.todo.entity.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class TodoFindByIdWithUserRepositoryImpl implements TodoFindByIdWithUserRepository{

    private final JPAQueryFactory q;

    @Override
    public Todo findByIdWithUser(long todoId) {
        return q
                .select(todo)
                .from(todo)
                .leftJoin(todo.user).fetchJoin()
                .where(
                        todoIdEq(todoId)
                ).fetchOne();
    }

    private BooleanExpression todoIdEq(Long todoId) {
        return todoId != null ? todo.id.eq(todoId) : null;
    }
}
