package gc.board.article.presentation;

import gc.board.article.dto.ArticleCreateRequest;
import gc.board.article.dto.ArticleResponse;
import gc.board.article.dto.ArticleUpdateRequest;
import gc.board.article.service.ArticleService;
import gc.board.article.service.response.ArticlePageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/v1/articles/{articleId}")
    public ArticleResponse getArticle(@PathVariable Long articleId) {
        return articleService.read(articleId);
    }

    @GetMapping("/v1/articles")
    public ArticlePageResponse getArticles(
            @RequestParam Long boardId,
            @RequestParam Long page,
            @RequestParam Long pageSize
    ) {
        return articleService.readAll(boardId, page, pageSize);
    }

    @PostMapping("/v1/articles")
    public ArticleResponse createArticle(@RequestBody ArticleCreateRequest request) {
        return articleService.create(request);
    }

    @PutMapping("/v1/articles/{articleId}")
    public ArticleResponse updateArticle(@PathVariable Long articleId,
                                         @RequestBody ArticleUpdateRequest request) {
        return articleService.update(articleId, request);
    }

    @DeleteMapping("/v1/articles/{articleId}")
    public void deleteArticle(@PathVariable Long articleId) {
        articleService.delete(articleId);
    }
}
