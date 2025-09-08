package gc.board.article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class ArticleApiTest {
    RestClient client = RestClient.create("http://localhost:9001");

    // ================= 요청/응답 DTO =================
    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long boardId;
        private Long writerId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleResponse {
        private Long id;
        private String title;
        private String content;
        private Long boardId;
        private Long writerId;
    }

    // ================= HTTP 통신 메서드 =================
    private ArticleResponse create(ArticleCreateRequest request) {
        return client.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    private ArticleResponse read(Long articleId) {
        return client.get()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }

    private void update(Long articleId, ArticleUpdateRequest request) {
        client.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(request)
                .retrieve()
                .body(Void.class);
    }

    private void delete(Long articleId) {
        client.delete()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(Void.class);
    }

    // ================= 테스트 메서드 =================
    @Test
    void createTest() {
        ArticleCreateRequest request = new ArticleCreateRequest("hi", "my content", 1L, 1L);
        create(request);
        System.out.println("post 요청 성공!");
    }

    @Test
    void readTest() {
        Long articleId = 1L; // 실제 존재하는 ID로 변경
        read(articleId);
        System.out.println("get 요청 성공!");
    }

    @Test
    void updateTest() {
        Long articleId = 1L; // 실제 존재하는 ID로 변경
        ArticleUpdateRequest updateRequest = new ArticleUpdateRequest("hi 2", "my content 2");
        update(articleId, updateRequest);
        System.out.println("put 요청 성공!");
    }

    @Test
    void deleteTest() {
        Long articleId = 1L;
        delete(articleId);
        System.out.println("delete 요청 성공!");
    }
}
