package com.back.get;

import com.back.dto.RenderedComment;
import com.back.index.Comment;
import com.back.repository.CommentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RenderedCommentsListGeneration {
    @Resource
    private CommentRenderingPreparation commentRenderingPreparation;
    @Resource
    private CommentRepository commentRepository;
    public List<RenderedComment> getArticleRootRenderingCommentList(int articleId){
        List<Comment> comments = commentRepository.findCommentsByArticleIdAndFatherId(articleId,0);
        return commentRenderingPreparation.getRenderedComment(comments);
    }
    public List<RenderedComment> getUserRenderingCommentedList(int commentId){
        List<Comment> comments = commentRepository.findCommentsByFatherId(commentId);
        return commentRenderingPreparation.getRenderedComment(comments);
    }
    public List<RenderedComment> getArticleRenderingCommentList(int articleId){
        List<Comment> comments = commentRepository.findCommentsByArticleId(articleId);
        return commentRenderingPreparation.getRenderedComment(comments);
    }
}
