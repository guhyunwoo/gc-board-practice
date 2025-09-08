package gc.board.article.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "article")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Article {

    @Id
    private Long articleId;

    private String title;
    private String content;
    private Long boardId;
    private Long writerId;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Article create(Long articleId, String title, String content, Long boardId, Long writerId) {
        Article article = new Article();
        article.articleId = articleId;
        article.title = title;
        article.content = content;
        article.boardId = boardId;
        article.writerId = writerId;
        article.createdAt = LocalDateTime.now();
        article.modifiedAt = LocalDateTime.now();
        return article;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
    }
}
