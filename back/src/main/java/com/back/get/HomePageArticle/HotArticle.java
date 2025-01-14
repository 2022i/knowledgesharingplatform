package com.back.get.HomePageArticle;


import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotArticle {
    @Resource
    private ArticleRepository articleRepository;

    public List<Article> getHotArticles() {
        int limit = 10;
        List<Article> articles = articleRepository.findArticlesByCheckAndDeleteAndReject(true, false, false);

        List<ArticleScore> articleScores = articles.stream().map(article -> {
            int supportCount = article.getSupportUserIds().size();
            int collectCount = article.getCollectUserIds().size();
            int viewCount = article.getViewUserIds().size();
            int opposeCount = article.getOpposeUserIds().size();

            int score = supportCount * 3 + collectCount * 2 + viewCount - opposeCount * 2;

            return new ArticleScore(article, score);
        }).toList();
        return articleScores.stream()
                .sorted(Comparator.comparingInt(ArticleScore::score).reversed()
                        .thenComparing(a -> a.article().getCreateTime(), Comparator.reverseOrder()))
                .limit(limit)
                .map(ArticleScore::article)
                .collect(Collectors.toList());
    }
     private record ArticleScore(Article article, int score) {
    }
}
