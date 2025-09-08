package gc.board.article.dto;

import gc.board.article.domain.Article;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ArticleResponse {
    private final Long articleId;
    private final String title;
    private final String content;
    private final Long boardId;
    private final Long writerId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    private ArticleResponse(Long articleId, String title, String content, Long boardId, Long writerId,
                            LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.boardId = boardId;
        this.writerId = writerId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getArticleId(),
                article.getTitle(),
                article.getContent(),
                article.getBoardId(),
                article.getWriterId(),
                article.getCreatedAt(),
                article.getModifiedAt()
        );
    }
}
