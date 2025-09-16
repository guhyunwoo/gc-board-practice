package gc.board.article.api;

import gc.board.article.service.response.ArticlePageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
public class ArticleApiTest {
    RestClient client = RestClient.create("http://localhost:9001");
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

    private ArticlePageResponse readAll(Long boardId, Long page, Long pageSize) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/articles")
                        .queryParam("boardId", boardId)
                        .queryParam("page", page)
                        .queryParam("pageSize", pageSize)
                        .build()
                )
                .retrieve()
                .body(ArticlePageResponse.class);
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

    private List<ArticleResponse> readAllInfiniteScroll(Long boardId, Long pageSize) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/articles/infinite")
                        .queryParam("boardId", boardId)
                        .queryParam("pageSize", pageSize)
                        .build()
                )
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {});
    }

    private List<ArticleResponse> readAllInfiniteScroll(Long boardId, Long pageSize, Long lastArticleId) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/articles/infinite")
                        .queryParam("boardId", boardId)
                        .queryParam("pageSize", pageSize)
                        .queryParam("lastArticleId", lastArticleId)
                        .build()
                )
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {});
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
        Long articleId = 223749490225635328L; // 실제 존재하는 ID로 변경
        read(articleId);
        System.out.println("get 요청 성공!");
    }

    @Test
    void readAllTest() {
        Long boardId = 1L;
        Long page = 1L;
        Long pageSize = 30L;
        ArticlePageResponse response = readAll(boardId, page, pageSize);
        System.out.println("articleCount : " + response.getArticlesCount());
        response.getArticles()
                .forEach(article -> System.out.println(article.getArticleId()));
    }

    @Test
    void updateTest() {
        Long articleId = 223749490225635328L; // 실제 존재하는 ID로 변경
        ArticleUpdateRequest updateRequest = new ArticleUpdateRequest("hi 2", "my content 2");
        update(articleId, updateRequest);
        System.out.println("put 요청 성공!");
    }

    @Test
    void deleteTest() {
        Long articleId = 223749490225635328L;
        delete(articleId);
        System.out.println("delete 요청 성공!");
    }

    @Test
    void readAllInfiniteScrollTest() {
        Long boardId = 1L;
        Long pageSize = 30L;

        List<ArticleResponse> articleResponseList = readAllInfiniteScroll(boardId, pageSize);

        log.info("첫 페이지 articleId : {}",
                    articleResponseList
                            .stream()
                            .map(ArticleResponse::getArticleId)
                            .toList()
                );


        Long lastIdFromFirstPage = articleResponseList.getLast().getArticleId();

        List<ArticleResponse> secondArticleResponseList =  readAllInfiniteScroll(boardId, pageSize, lastIdFromFirstPage);

        Long firstIdFromSecondPage = secondArticleResponseList.getFirst().getArticleId();

        assert lastIdFromFirstPage > firstIdFromSecondPage : "무한스크롤 연속성이 깨졌습니다!";
    }

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
        private Long articleId;
        private String title;
        private String content;
        private Long boardId;
        private Long writerId;
    }
}
