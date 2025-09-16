package gc.board.article.service;

import gc.board.article.domain.Article;
import gc.board.article.dto.ArticleCreateRequest;
import gc.board.article.dto.ArticleResponse;
import gc.board.article.dto.ArticleUpdateRequest;
import gc.board.article.repository.ArticleRepository;
import gc.board.article.service.response.ArticlePageResponse;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final Snowflake snowflake;

    @Transactional
    public ArticleResponse create(ArticleCreateRequest request) {
        Long id = snowflake.nextId();
        Article article = Article.create(id, request.getTitle(), request.getContent(),
                request.getBoardId(), request.getWriterId());
        articleRepository.save(article);
        return ArticleResponse.from(article);
    }

    public ArticleResponse read(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + articleId));
        return ArticleResponse.from(article);
    }

    public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize) {
        Long offset = (page - 1) * pageSize;
        Long limit = PageLimitCalculator.calculatePageLimit(page, pageSize, 10L);

        List<Article> articles = articleRepository.findAll(boardId, offset, limit);
        Long articleCount = articleRepository.count(boardId, limit);

        List<ArticleResponse> articleResponses = articles.stream()
                .map(ArticleResponse::from)
                .toList();

        return ArticlePageResponse.of(articleResponses, articleCount);
    }

    @Transactional
    public ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + articleId));
        article.update(request.getTitle(), request.getContent());
        return ArticleResponse.from(article);
    }

    @Transactional
    public void delete(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public List<ArticleResponse> readAllInfiniteScroll(Long boardId, Long pageSize, Long lastArticleId) {
        List<Article> articleList;
        if (lastArticleId == null) {
            articleList = articleRepository.findAllInfiniteScroll(boardId, pageSize);
        } else {
            articleList = articleRepository.findAllInfiniteScroll(boardId, pageSize, lastArticleId);
        }

        return articleList.stream().map(ArticleResponse::from).toList();
    }
}
