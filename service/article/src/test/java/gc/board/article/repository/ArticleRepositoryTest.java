package gc.board.article.repository;

import gc.board.article.domain.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void findAllTest() {
        List<Article> articles = articleRepository.findAll(1L, 1499970L, 30L);

        log.info("찾은 아티클 수 : {}", articles.size());
        articles.forEach(article -> log.info("아티클 정보 : {}", article));
    }

    @Test
    void countTest() {
        Long articleCount = articleRepository.count(1L, 10000L);

        log.info(articleCount.toString());
    }

    @Test
    void findAllInfiniteScroll() {
        List<Article> firstPage = articleRepository.findAllInfiniteScroll(1L, 30L);

        log.info("첫 페이지 articleIds: {}",
                firstPage.stream().map(Article::getArticleId).toList());

        Long lastIdFromFirstPage = firstPage.getLast().getArticleId();

        List<Article> secondPage =  articleRepository.findAllInfiniteScroll(1L, 30L, lastIdFromFirstPage);

        log.info("두 번째 페이지 articleIds: {}",
                secondPage.stream().map(Article::getArticleId).toList());

        Long firstIdFromSecondPage = secondPage.getFirst().getArticleId();
        assert lastIdFromFirstPage > firstIdFromSecondPage : "무한스크롤 연속성이 깨졌습니다!";
    }
}