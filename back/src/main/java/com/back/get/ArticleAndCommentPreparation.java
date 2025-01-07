package com.back.get;

import com.back.dto.article.ArticleAndComment;
import com.back.dto.article.RenderedArticle;
import com.back.get.ArticleIdsList.CommentArticleIdsList;
import com.back.index.Comment;
import com.back.repository.ArticleRepository;
import com.back.repository.CommentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleAndCommentPreparation {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private ArticleRenderingPreparation articleRenderingPreparation;
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private CommentArticleIdsList commentArticleIdsList;
    public List<ArticleAndComment> getArticleAndComments(int userId) {
        List<Integer> commentArticleIds =commentArticleIdsList.getIdsList(userId);
        return getArticleAndComments(commentArticleIds, userId);
    }
    public List<ArticleAndComment> getArticleAndComments(List<Integer> commentArticleIds, int userId) {
        List<ArticleAndComment> articleAndComments =new ArrayList<>();
        for (Integer commentArticleId : commentArticleIds) {
            ArticleAndComment articleAndComment = new ArticleAndComment();
            List<String> comments = getCommentContentList(commentArticleId, userId);
            articleAndComment.setComments(comments);
            articleAndComment.setArticle(getRenderedArticle(commentArticleId));
            articleAndComments.add(articleAndComment);
        }
        return articleAndComments;
    }
    private List<String> getCommentContentList(int articleId, int userId) {
        List<String> comments = new ArrayList<>();
        List<Comment> commentList = commentRepository.findCommentsByArticleIdAndUserId(articleId, userId);
        for (Comment comment : commentList) {
            comments.add(comment.getContent());
        }
        return comments;
    }
    private RenderedArticle getRenderedArticle(int articleId) {
        return articleRenderingPreparation.getRenderedArticle(articleRepository.findArticleById(articleId));
    }
}
