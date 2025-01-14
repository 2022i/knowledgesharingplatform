package com.back.get.preparation;

import com.back.dto.article.RenderedArticle;
import com.back.index.Article;
import com.back.repository.CommentRepository;
import com.back.repository.ThemeRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleRenderingPreparation {
    @Resource
    private UserDataRepository userDataRepository;
    @Resource
    private ThemeRepository themeRepository;
    @Resource
    UserRenderingPreparation userRenderingPreparation;
    @Resource
    private CommentRepository commentRepository;
    public List<RenderedArticle>getRenderedArticles(List<Article> articles){
        List<RenderedArticle> renderedArticles = new ArrayList<>();
        for (Article article : articles) {
            renderedArticles.add(getRenderedArticle(article));
        }
        return renderedArticles;
    }
    public RenderedArticle getRenderedArticle(Article article){
        RenderedArticle renderedArticle = new RenderedArticle();
        renderedArticle.setId(article.getId());
        renderedArticle.setTitle(article.getTitle());
        renderedArticle.setAuthor(userRenderingPreparation.getArticleAuthor(userDataRepository.findUserDataById(article.getAuthorId()),article.getAuthorId()));
        renderedArticle.setTheme(themeRepository.findThemeById(article.getThemeId()).getName());
        renderedArticle.setTitle(article.getTitle());
        renderedArticle.setSummary(article.getSummary());
        renderedArticle.setContent(article.getContent());
        renderedArticle.setRelatedKnowledge(article.getRelatedKnowledge());
        renderedArticle.setCreateTime(article.getCreateTime());
        renderedArticle.setViewUserCount(article.getViewUserIds().size());
        renderedArticle.setSupportUserCount(article.getSupportUserIds().size());
        renderedArticle.setOpposeUserCount(article.getOpposeUserIds().size());
        renderedArticle.setCommentCount(commentRepository.findCommentsByArticleId(article.getId()).size());
        renderedArticle.setCollectionUserCount(article.getCollectUserIds().size());
        renderedArticle.setShareUserCount(article.getShareUserIds().size());
        return renderedArticle;
    }
}
