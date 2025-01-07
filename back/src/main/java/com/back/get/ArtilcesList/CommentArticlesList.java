package com.back.get.ArtilcesList;

import com.back.dto.Article.ArticleAndComment;
import com.back.get.ArticleIdsList.CommentArticleIdsList;
import com.back.get.ArticleAndCommentPreparation;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentArticlesList extends ArticlesList{
    @Resource
    private ArticleAndCommentPreparation articleAndCommentPreparation;
    @Resource
    private CommentArticleIdsList commentArticleIdsList;
    @Override
    public List<Article> getArticlesList(int userId) {
        List<Integer> articleIds = commentArticleIdsList.getIdsList(userId);
        return getArticlesList(articleIds);
    }
    public List<ArticleAndComment> getCommentAndArticleList(int userId) {
        return articleAndCommentPreparation.getArticleAndComments(userId);
    }
}
