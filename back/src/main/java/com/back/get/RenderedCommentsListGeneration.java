package com.back.get;

import com.back.dto.RenderedComment;
import com.back.index.Comment;
import com.back.repository.CommentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RenderedCommentsListGeneration {
    @Resource
    private CommentRenderingPreparation commentRenderingPreparation;
    @Resource
    private CommentRepository commentRepository;
    public List<RenderedComment> getArticleRootRenderingCommentList(int articleId){
        List<Comment> comments = commentRepository.findCommentsByArticleIdAndFatherId(articleId,0);
        List<RenderedComment> renderedComments = new ArrayList<>();
        for (Comment comment : comments) {
            if(comment.getFatherId() == 0){
                renderedComments.add(commentRenderingPreparation.getRenderedComment(comment));
            }
        }
        return renderedComments;
    }
    public List<RenderedComment> getUserRenderingCommentedList(int commentId){
        List<Comment> comments = commentRepository.findCommentsByFatherId(commentId);
        List<RenderedComment> renderedComments = new ArrayList<>();
        for (Comment comment : comments) {
            renderedComments.add(commentRenderingPreparation.getRenderedComment(comment));
        }
        return renderedComments;
    }
    public List<RenderedComment> getArticleRenderingCommentList(int articleId){
        List<Comment> comments = commentRepository.findCommentsByArticleId(articleId);
        List<RenderedComment> renderedComments = new ArrayList<>();
        for (Comment comment : comments) {
            renderedComments.add(commentRenderingPreparation.getRenderedComment(comment));
        }
        return renderedComments;
    }
}
