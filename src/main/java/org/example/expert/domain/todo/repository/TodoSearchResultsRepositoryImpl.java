package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.QTodoSearchResults;
import org.example.expert.domain.todo.dto.response.TodoSearchResults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class TodoSearchResultsRepositoryImpl implements TodoSearchResultsRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TodoSearchResults> searchTodos(Pageable pageable, String title, LocalDateTime startTime, LocalDateTime endTime, String nickname) {
        // 메인 쿼리 - 검색 조건 및 페이징 처리
        List<TodoSearchResults> results = queryFactory
                .select(new QTodoSearchResults(
                        todo.title,
                        todo.managers.size(),
                        todo.comments.size()
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .leftJoin(todo.user, user)
                .where(
                        titleContains(title),
                        managerNicknameContains(nickname),
                        createdDateBetween(startTime, endTime)
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())  // 최신순 정렬
                .offset(pageable.getOffset())    // 페이징 처리
                .limit(pageable.getPageSize())   // 페이징 처리
                .fetch();                        // 결과 리스트로 가져오기

        long total = queryFactory
                .select(Wildcard.count)
                .from(todo)
                .where(
                        titleContains(title),
                        managerNicknameContains(nickname),
                        createdDateBetween(startTime, endTime)
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    // 제목 검색 조건
    private BooleanExpression titleContains(String titleKeyword) {
        return titleKeyword != null ? todo.title.containsIgnoreCase(titleKeyword) : null;
    }

    // 담당자 닉네임 검색 조건
    private BooleanExpression managerNicknameContains(String nickname) {
        return nickname != null ? user.nickname.containsIgnoreCase(nickname) : null;
    }

    // 생성일 범위 검색 조건
    private BooleanExpression createdDateBetween(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null) {
            return todo.createdAt.between(from, to);
        } else if (from != null) {
            return todo.createdAt.goe(from);
        } else if (to != null) {
            return todo.createdAt.loe(to);
        } else {
            return null;
        }
    }
}
