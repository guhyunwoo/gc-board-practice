package gc.board.article.service;

import gc.board.article.domain.Article;
import gc.board.article.dto.ArticleCreateRequest;
import gc.board.article.dto.ArticleResponse;
import gc.board.article.dto.ArticleUpdateRequest;
import gc.board.article.repository.ArticleRepository;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
