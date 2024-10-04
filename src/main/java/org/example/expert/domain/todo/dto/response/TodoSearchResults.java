package org.example.expert.domain.todo.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.repository.Query;

@Getter
public class TodoSearchResults {
    private String title;
    private Integer managersCount;
    private Integer commentCount;

    @QueryProjection
    public TodoSearchResults (String title, Integer managersCount, Integer commentCount) {
        this.title = title;
        this.managersCount = managersCount;
        this.commentCount = commentCount;
    }
}
