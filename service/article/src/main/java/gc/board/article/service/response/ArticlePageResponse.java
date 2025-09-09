package gc.board.article.service.response;

import gc.board.article.dto.ArticleResponse;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ArticlePageResponse {
    List<ArticleResponse> articles;
    Long articlesCount;

    public ArticlePageResponse() { }

    public ArticlePageResponse(List<ArticleResponse> articles, Long articlesCount) {
        this.articlesCount = articlesCount;
        this.articles = articles;
    }

    public static ArticlePageResponse of(List<ArticleResponse> articles, Long articlesCount) {
        return new ArticlePageResponse(
                articles,
                articlesCount
        );
    }
}
