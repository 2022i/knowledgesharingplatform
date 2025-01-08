package com.back.get.CommentsList;

import com.back.dto.RenderedComment;
import com.back.get.preparation.CommentRenderingPreparation;
import com.back.index.Comment;
import com.back.repository.CommentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCommentInArticle {
    @Resource
    private CommentRenderingPreparation commentRenderingPreparation;
    @Resource
    private CommentRepository commentRepository;
    public List<RenderedComment> getUserCommentInArticle(int userId, int articleId) {
        List<Comment> comments = commentRepository.findCommentsByArticleIdAndUserId(articleId, userId);
        return commentRenderingPreparation.getRenderedComments(comments);
    }
}
