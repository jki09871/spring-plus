# SPRING PLUS 과제

-----------

# 필수 기능

## LEVEL1

### 1. 코드 개선 퀴즈 - @Transactional의 이해

- 할 일 저장 기능을 구현한 API(`/todos`)를 호출할 때, 아래와 같은 에러가 발생하고 있어요.

    - 에러 로그 원문 보기

      jakarta.servlet.ServletException: Request processing failed: org.springframework.orm.jpa.JpaSystemException: could
      not execute
      statement [Connection is read-only. Queries leading to data modification are not allowed] [insert into todos (contents,created_at,modified_at,title,user_id,weather) values (?,?,?,?,?,?)]

- 에러가 발생하지 않고 정상적으로 할 일을 저장 할 수 있도록 코드를 수정해주세요.

```
코드 수정 내용
클래스에 @Transactional(readOnly = true)가 적용되어 있는데 데이터베이스 연결이 읽기 전용으로 설정되었는데 데이터를 삽입하려고 시도했기 때문에 발생한 것이다.
메서드에 따로 @Transactional을 붙여 주었다
```

### 2. 코드 추가 퀴즈 - JWT의 이해

🚨 기획자의 긴급 요청이 왔어요!
아래의 요구사항에 맞춰 기획 요건에 대응할 수 있는 코드를 작성해주세요.

- User의 정보에 nickname이 필요해졌어요.
    - User 테이블에 nickname 컬럼을 추가해주세요.
    - nickname은 중복 가능합니다.
- 프론트엔드 개발자가 JWT에서 유저의 닉네임을 꺼내 화면에 보여주길 원하고 있어요.

````
코드 수정 내용
User Entity, SignupRequest, AuthService, AuthUser, JwtUtil, AuthUserArgumentResolver 클래스에 
nickname 필드를 추가해 주었다.
````

### 3. 코드 개선 퀴즈 - AOP의 이해

😱 AOP가 잘못 동작하고 있어요!

- `UserAdminController` 클래스의 `changeUserRole()` 메소드가 실행 전 동작해야해요.
- `AdminAccessLoggingAspect` 클래스에 있는 AOP가 개발 의도에 맞도록 코드를 수정해주세요.

```
AdminAccessLoggingAspect 클래스에 있는 
    @After("execution(* org.example.expert.domain.user.controller.UserController.getUser(..))")
    public void logAfterChangeUserRole(JoinPoint joinPoint) 
    를
    @Before("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public void logAfterChangeUserRole(JoinPoint joinPoint)
    으로 변경 했다.
```

### 차이점 설명

@Before:

- 대상 메서드가 실행되기 전에 실행되는 코드이다.예를 들어, 메서드가 실행되기 전에 로그를 기록하거나, 인증, 권한 체크 등의 작업을 하고 싶을 때 사용한다.

@After:

- 대상 메서드가 실행된 후에 실행되는 코드이다. 예를 들어, 메서드 실행 후 리소스를 해제하거나, 로그를 남기고 싶은 경우에 사용한다

### 4. 테스트 코드 퀴즈 - 컨트롤러 테스트의 이해

- 테스트 패키지 `org.example.expert.domain.todo.controller`의
  `todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다()` 테스트가 실패하고 있어요.

- 테스트가 정상적으로 수행되어 통과할 수 있도록 테스트 코드를 수정해주세요.

Todo 단건 조회 시 예외 처리 테스트 변경 내역
실패한 테스트 코드:

아래의 테스트는 Todo가 존재하지 않을 때, 예외를 처리하는 코드로, 예상되는 HTTP 상태 코드와 응답 내용이 잘못 설정되어 테스트가 실패했다. 이 코드는 HTTP 상태 코드를 200 OK로 기대하고 있으나,
실제로는 400 Bad Request가 반환되어야 한다.

```
@Test
void todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다() throws Exception {

   ....기타 코드
   
    mockMvc.perform(get("/todos/{todoId}", todoId))
            .andExpect(status().isOk()) // 실패 원인: 200 OK 상태를 기대함
            .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
            .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.message").value("Todo not found"));
}
```

실패 원인:

예외 발생 시 올바른 상태 코드로 400 Bad Request를 반환해야 하지만, 테스트는 200 OK를 기대하고 있어 테스트가 실패했다.

성공한 테스트 코드:

아래는 수정 후의 코드로, Todo가 존재하지 않을 때, 예외 발생 시 올바르게 400 Bad Request 상태 코드와 함께 적절한 메시지가 반환되도록 수정하여 테스트가 성공했다.

```
@Test
void todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다() throws Exception {

   ....기타 코드

    // then
    mockMvc.perform(get("/todos/{todoId}", todoId))
            .andExpect(status().isBadRequest()) // 수정된 부분: 400 Bad Request 상태를 기대함
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.message").value("Todo not found"));
}
```
변경 사항:

상태 코드 기대값을 200 OK에서 **400 Bad Request**로 수정.
테스트가 올바르게 상태 코드와 응답 내용을 확인하도록 변경하여 테스트가 성공하게 됨.

요약:

처음에는 예외 처리 시 올바른 상태 코드(400 Bad Request)를 반환하지 않고 200 OK 상태를 기대하여 테스트가 실패했으나, 상태 코드 및 응답 값을 정확히 수정한 후에는 테스트가 성공하였다.

### **5. 코드 개선 퀴즈 -  JPA의 이해**

🚨 기획자의 긴급 요청이 왔어요!
아래의 요구사항에 맞춰 기획 요건에 대응할 수 있는 코드를 작성해주세요.


- 할 일 검색 시 `weather` 조건으로도 검색할 수 있어야해요.
    - `weather` 조건은 있을 수도 있고, 없을 수도 있어요!
- 할 일 검색 시 수정일 기준으로 기간 검색이 가능해야해요.
    - 기간의 시작과 끝 조건은 있을 수도 있고, 없을 수도 있어요!
- JPQL을 사용하고, 쿼리 메소드명은 자유롭게 지정하되 너무 길지 않게 해주세요.

💡 필요할 시, 서비스 단에서 if문을 사용해 여러 개의 쿼리(JPQL)를 사용하셔도 좋습니다.

```
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u " +
            "WHERE (:weather IS NULL OR t.weather = :weather) " +
            "AND (:startDate IS NULL OR t.modifiedAt >= :startDate) " +
            "AND (:endDate IS NULL OR t.modifiedAt <= :endDate) " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> searchTodos(@Param("weather") String weather,
                           @Param("startDate") LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate,
                           Pageable pageable);
```
설명 :

Todo 엔티티를 검색하기 위한 JPA 쿼리 메서드이다. @Query 애너테이션을 사용하여 동적으로 쿼리를 작성하고, 조건에 맞는 Todo 객체들을 조회한다. Pageable 파라미터를 통해 페이징 처리를 하며, Page<Todo>로 결과를 반환한다.

쿼리 설명 :

LEFT JOIN FETCH:
Todo 엔티티와 user 엔티티를 지연 로딩을 회피하고, 함께 가져오기 위해 LEFT JOIN FETCH를 사용한다.
이를 통해 Todo 엔티티를 조회할 때 연관된 user 정보를 같이 가져올 수 있다.

동적 조건 처리:
(:weather IS NULL OR t.weather = :weather): weather 값이 주어지지 않으면(NULL), 해당 조건을 무시하고, 값이 주어지면 weather 컬럼과 일치하는 결과만 가져온다.
(:startDate IS NULL OR t.modifiedAt >= :startDate): startDate가 주어지면 해당 날짜 이후(>=)의 Todo 항목만 조회한다. 주어지지 않으면 이 조건을 무시한다.
(:endDate IS NULL OR t.modifiedAt <= :endDate): endDate가 주어지면 해당 날짜 이전(<=)의 Todo 항목만 조회한다. 주어지지 않으면 이 조건을 무시한다.

ORDER BY:
t.modifiedAt DESC: Todo 항목을 수정된 날짜 기준으로 내림차순 정렬하여 가장 최근에 수정된 항목이 먼저 나오도록 한다.

페이징 처리:
Pageable pageable: 쿼리 결과에 대해 페이징 처리한다. 이는 요청하는 페이지 수, 페이지 크기, 정렬 방식 등을 지정할 수 있도록 한다.
반환 타입은 Page<Todo>로, 페이징된 결과를 제공한다.

---

## LEVEL2

### 6. JPA Cascade

🤔 앗❗ 실수로 코드를 지웠어요!

- 할 일을 새로 저장할 시, 할 일을 생성한 유저는 담당자로 자동 등록되어야 합니다.
- JPA의 `cascade` 기능을 활용해 할 일을 생성한 유저가 담당자로 등록될 수 있게 해주세요.

```
 수정 전 : @OneToMany(mappedBy = "todo")
 수정 후 : @OneToMany(mappedBy = "todo", cascade = CascadeType.PERSIST) 
```
설명 :
@OneToMany(mappedBy = "todo", cascade = CascadeType.PERSIST)는 할 일을 저장할 때 담당자(User)도 자동으로 저장되도록 설정한다.

### 7. N+1


- `CommentController` 클래스의 `getComments()` API를 호출할 때 N+1 문제가 발생하고 있어요. 
N+1 문제란, 데이터베이스 쿼리 성능 저하를 일으키는 대표적인 문제 중 하나로, 특히 연관된 엔티티를 조회할 때 발생해요.
- 해당 문제가 발생하지 않도록 코드를 수정해주세요.
```
CommentRepository에 작성 되어 있는 findByTodoIdWithUser메서드에 @Query
수정 전 : @Query("SELECT c FROM Comment c JOIN c.user WHERE c.todo.id = :todoId")
수정 후 : @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.todo.id = :todoId")
```
설명 :
JOIN FETCH를 사용하면 지연 로딩을 피하고, 연관된 엔티티를 한 번에 가져오게 된다. 이렇게 하면 데이터베이스 쿼리가 한 번만 실행되며, Comment와 연관된 User가 한 번에 모두 조회된다.
즉, N+1 문제가 발생하지 않게 되고, 성능이 개선된다.

### 8. QueryDSL

TodoService.getTodo 메소드

- JPQL로 작성된 `findByIdWithUser` 를 QueryDSL로 변경합니다.
- 7번과 마찬가지로 N+1 문제가 발생하지 않도록 유의해 주세요!

```
변경 전 코드 :     
    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
    

변경 후 코드는 JPQL에서 QueryDSL로 변환
@Repository
@RequiredArgsConstructor
public class TodoFindByIdWithUserRepositoryImpl implements TodoFindByIdWithUserRepository{

    private final JPAQueryFactory q;  // QueryDSL을 사용한 쿼리 작성

    @Override
    public Todo findByIdWithUser(long todoId) {
        return q
                .select(todo)  // 조회할 엔티티 선택
                .from(todo)    // Todo 엔티티로부터 조회
                .leftJoin(todo.user).fetchJoin()  // User 엔티티와 fetch join (N+1 문제 해결)
                .where(
                        todoIdEq(todoId)  // 조건 추가
                ).fetchOne();  // 결과를 단일 엔티티로 가져옴
    }

    // todoId가 null이 아닌 경우에만 조건을 추가
    private BooleanExpression todoIdEq(Long todoId) {
        return todoId != null ? todo.id.eq(todoId) : null;
    }
}
```
QueryDSL 코드에서 중요한 점

fetchJoin(): N+1 문제를 해결하기 위해 사용된다. 
이 방식으로 연관된 엔티티를 즉시 로딩하여 한 번의 쿼리로 모두 가져올 수 있다.

fetchOne(): 단일 결과를 반환할 때 사용하며, 
쿼리가 정확히 하나의 Todo를 반환할 것으로 예상될 때 적합하다.

### **9. Spring  Security**


⚙️ Spring Security를 도입하기로 결정했어요!

- 기존 `Filter`와 `Argument Resolver`를 사용하던 코드들을 Spring Security로 변경해주세요.
    - 접근 권한 및 유저 권한 기능은 그대로 유지해주세요.
    - 권한은 Spring Security의 기능을 사용해주세요.
- 토큰 기반 인증 방식은 유지할 거예요. JWT는 그대로 사용해주세요.

```
Spring Security 적용 코드 변경 사항
기존 코드에서는 Filter와 Argument Resolver를 통해 사용자의 권한과 인증을 처리했지만, 
이를 Spring Security로 변경하여 더 효율적이고 표준적인 방법으로 인증과 권한 관리를 하게 되었다. 
이번 변경은 JWT 기반 인증 방식을 유지하면서 Spring Security의 권한 관리를 도입하는 것에 중점을 두고 있다.
```
주요 변경 사항
JwtAuthenticationToken:
JwtAuthenticationToken 클래스는 JWT 기반 인증 정보를 보유하는 토큰으로, 
Spring Security의 AbstractAuthenticationToken을 상속받아 사용자를 인증하는데 사용된다. 
AuthUser 객체를 토큰에 포함하여 인증된 사용자의 정보를 담는다.

SecurityConfig:
Spring Security를 설정하는 SecurityConfig 클래스는 세션을 사용하지 않는 무상태 방식을 적용했으며, 
JWT 필터를 Spring Security 필터 체인에 추가했다. 또한, csrf, formLogin, httpBasic 등 
기존의 인증 방식을 비활성화하고, JWT 기반 인증만 처리하도록 설정했다.

@AuthenticationPrincipal:
Spring Security에서 인증된 사용자 정보를 컨트롤러에서 쉽게 가져오기 위해 
@AuthenticationPrincipal을 사용했다. 이 애너테이션은 현재 인증된 사용자를 AuthUser로 변환하여 전달한다.
---

# 도전 기능

## Level. 3

### 10. QueryDSL 을 사용하여 검색 기능 만들기

👉 일정을 검색하는 기능을 만들고 싶어요!
검색 기능의 성능 및 사용성을 높이기 위해 QueryDSL을 활용한 쿼리 최적화를 해보세요.
❗Projections를 활용해서 필요한 필드만 반환할 수 있도록 해주세요❗


- 새로운 API로 만들어주세요.
- 검색 조건은 다음과 같아요.
    - 검색 키워드로 일정의 제목을 검색할 수 있어요.
        - 제목은 부분적으로 일치해도 검색이 가능해요.
    - 일정의 생성일 범위로 검색할 수 있어요.
        - 일정을 생성일 최신순으로 정렬해주세요.
    - 담당자의 닉네임으로도 검색이 가능해요.
        - 닉네임은 부분적으로 일치해도 검색이 가능해요.
- 다음의 내용을 포함해서 검색 결과를 반환해주세요.
    - 일정에 대한 모든 정보가 아닌, 제목만 넣어주세요.
    - 해당 일정의 담당자 수를 넣어주세요.
    - 해당 일정의 총 댓글 개수를 넣어주세요.
- 검색 결과는 페이징 처리되어 반환되도록 합니다.

구현된 기능 요약:

검색 조건:

일정 제목: 부분적으로 일치하는 제목을 검색.

담당자 닉네임: 부분적으로 일치하는 닉네임을 검색.

생성일: 일정 생성일 범위로 검색.

정렬: 생성일을 기준으로 최신순으로 정렬.

Projections: 일정의 제목, 담당자 수, 댓글 개수만 반환.

페이징 처리: 검색 결과는 Pageable을 통해 페이징 처리되어 반환된다.

### 코드 설명
1. TodoSearchResultsRepositoryImpl
```
@RequiredArgsConstructor
@Repository
public class TodoSearchResultsRepositoryImpl implements TodoSearchResultsRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TodoSearchResults> searchTodos(Pageable pageable, String title, LocalDateTime startTime, LocalDateTime endTime, String nickname) {
        // 메인 쿼리 - 검색 조건 및 페이징 처리
        List<TodoSearchResults> results = queryFactory
                .select(new QTodoSearchResults(
                        todo.title,                 // 제목
                        todo.managers.size(),       // 담당자 수
                        todo.comments.size()        // 댓글 수
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)  // 담당자 조인
                .leftJoin(todo.comments, comment)  // 댓글 조인
                .leftJoin(todo.user, user)         // 유저 조인 (닉네임 검색)
                .where(
                        titleContains(title),       // 제목 검색 조건
                        managerNicknameContains(nickname),  // 닉네임 검색 조건
                        createdDateBetween(startTime, endTime)  // 생성일 범위
                )
                .orderBy(todo.createdAt.desc())  // 최신순 정렬
                .offset(pageable.getOffset())    // 페이징 처리
                .limit(pageable.getPageSize())   // 페이징 처리
                .fetch();                        // 결과 리스트로 가져오기

        // 총 개수 쿼리
        long total = queryFactory
                .select(todo.count())
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .leftJoin(todo.user, user)
                .where(
                        titleContains(title),
                        managerNicknameContains(nickname),
                        createdDateBetween(startTime, endTime)
                )
                .fetchOne();

        // 결과를 페이지로 반환
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
```
2. Projections 클래스 (QTodoSearchResults)
   QTodoSearchResults는 Projections을 활용해 쿼리에서 필요한 필드만 가져오는 부분이다. 이 클래스를 사용해 필요한 값만 선택적으로 가져올 수 있다.
```

@Getter
public class TodoSearchResults {

    private final String title;
    private final long managerCount;
    private final long commentCount;

    @QueryProjection
    public TodoSearchResults(String title, long managerCount, long commentCount) {
        this.title = title;
        this.managerCount = managerCount;
        this.commentCount = commentCount;
    }
}
```
설명
검색 조건 처리:

titleContains: 제목 검색을 위한 조건. containsIgnoreCase()를 사용하여 대소문자 구분 없이 부분 검색이 가능하다.

managerNicknameContains: 담당자 닉네임 검색 조건. 마찬가지로 대소문자를 구분하지 않고 닉네임을 검색할 수 있다.

createdDateBetween: 생성일 범위를 설정하는 조건. 시작일과 종료일을 기반으로 일정 범위를 검색한다.

Projections 사용:
QTodoSearchResults: 쿼리 결과에서 필요한 필드만을 선택적으로 반환한다. 여기서는 제목, 담당자 수, 댓글 개수만 반환한다.

페이징 처리:
offset()과 limit()을 사용하여 쿼리 결과에 대해 페이징 처리를 한다. Pageable을 사용하여 클라이언트 요청에 맞게 적절한 페이지 크기로 결과를 반환한다.

쿼리 최적화:
leftJoin()을 사용하여 일정에 연결된 담당자, 댓글, 유저(닉네임) 정보를 가져오면서도, 필요한 부분만 Projections으로 선택해 가져오므로 성능 최적화가 가능하다.

### 11. Transaction 심화

👉 매니저 등록 요청 시 로그를 남기고 싶어요!
`@Transactional`의 옵션 중 하나를 활용하여 매니저 등록과 로그 기록이 각각 독립적으로 처리될 수 있도록 해봅시다.

- 매니저 등록 요청을 기록하는 로그 테이블을 만들어주세요.
    - DB 테이블명: `log`
- 매니저 등록과는 별개로 로그 테이블에는 항상 요청 로그가 남아야 해요.
    - 매니저 등록은 실패할 수 있지만, 로그는 반드시 저장되어야 합니다.
    - 로그 생성 시간은 반드시 필요합니다.
    - 그 외 로그에 들어가는 내용은 원하는 정보를 자유롭게 넣어주세요.

핵심 변경 사항 및 구현 사항:

log 테이블에 대한 로그 엔티티를 생성하여 요청 시 로그를 남긴다.
매니저 등록 로직에서 발생하는 정상 및 에러 로그를 남기기 위해 LogService에서 **@Transactional(propagation = Propagation.REQUIRES_NEW)**를 사용했다. 이를 통해 매니저 등록 트랜잭션이 실패해도 로그 저장 트랜잭션은 성공적으로 처리된다.

로그 테이블의 필드:

ip: 요청을 보낸 사용자의 IP 주소.
createdAt: 로그 생성 시간.
resultMessage: 로그 메시지(성공 또는 실패 메시지).
코드 설명
1. Log 엔티티
   log 테이블의 구조를 정의하는 Log 엔티티이다. IP 주소, 생성 시간, 결과 메시지를 저장한다.
```

@Getter
@Entity
@NoArgsConstructor
@Table(name = "log")
public class Log {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String ip;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "message")
    private String resultMessage;

    private Log(String ip, LocalDateTime createdAt, String resultMessage) {
        this.ip = ip;
        this.createdAt = createdAt;
        this.resultMessage = resultMessage;
    }

    public static Log of(String ip, LocalDateTime createdAt, String resultMessage) {
        return new Log(ip, createdAt, resultMessage);
    }
}
```

2. LogService
   LogService는 로그를 저장하는 역할을 한다. 성공적인 매니저 등록과 에러 발생 시 각각 다른 로그를 남기며, saveLogWithError 메서드는 새로운 트랜잭션을 통해 에러 로그를 저장하도록 설정되어 있다.

```

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    // 정상 로그 저장
    public void saveLog(String message) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String ip = request.getRemoteAddr();
        logRepository.save(Log.of(ip, LocalDateTime.now(), message));
    }

    // 에러 로그 저장 - 새로운 트랜잭션으로 처리
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLogWithError(String errorMessage) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String ip = request.getRemoteAddr();
        logRepository.save(Log.of(ip, LocalDateTime.now(), "Error: " + errorMessage));
    }
}
```
3. 매니저 등록 서비스
   매니저 등록을 처리하는 서비스 로직이다. 매니저 등록이 성공하면 정상 로그를 남기고, 에러가 발생하면 별도의 트랜잭션에서 에러 로그를 저장하고 예외를 다시 던진다.

```
@Transactional
public ManagerSaveResponse saveManager(AuthUser authUser, long todoId, ManagerSaveRequest managerSaveRequest) {

    try {
        // 정상 로그 저장
        logService.saveLog("저장적으로 매니저가 등록 되었습니다.");

        ~ 기타 코드 ~
        
    } catch (Exception e) {
        // 에러가 발생하면 새로운 트랜잭션에서 에러 로그 저장
        logService.saveLogWithError("Manager registration failed: " + e.getMessage());
        throw e; // 예외를 다시 던져서 상위 로직에서 처리
    }
}

```

핵심 포인트: 

@Transactional(propagation = Propagation.REQUIRES_NEW)
Propagation.REQUIRES_NEW: 이 옵션을 사용하면 현재 트랜잭션과는 별도로 새로운 트랜잭션을 생성하여 로그를 저장할 수 있다. 즉, 매니저 등록 트랜잭션이 실패하더라도 로그 트랜잭션은 영향을 받지 않고 독립적으로 커밋된다.
이를 통해 매니저 등록이 실패하더라도 로그는 항상 남도록 설계할 수 있다.

요약:

@Transactional(propagation = Propagation.REQUIRES_NEW)를 사용해 매니저 등록과 로그 기록을 독립적으로 처리한다.
매니저 등록은 실패할 수 있지만, 로그는 항상 저장된다.
로그에는 IP 주소, 로그 생성 시간, 결과 메시지가 포함된다.
