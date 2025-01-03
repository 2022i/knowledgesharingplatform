package com.back.get;

import com.back.dto.RenderingComment;
import com.back.index.Comment;
import com.back.repository.CommentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RenderingCommentsListGeneration {
    @Resource
    private RenderingCommentPreparation renderingCommentPreparation;
    @Resource
    private CommentRepository commentRepository;
    public List<RenderingComment> getArticleRootRenderingCommentList(int articleId){
        List<Comment> comments = commentRepository.findCommentsByArticleIdAndFatherId(articleId,0);
        List<RenderingComment> renderingComments = new ArrayList<>();
        for (Comment comment : comments) {
            if(comment.getFatherId() == 0){
                renderingComments.add(renderingCommentPreparation.getRenderingComment(comment));
            }
        }
        return renderingComments;
    }
    public List<RenderingComment> getUserRenderingCommentedList(int commentId){
        List<Comment> comments = commentRepository.findCommentsByFatherId(commentId);
        List<RenderingComment> renderingComments = new ArrayList<>();
        for (Comment comment : comments) {
            renderingComments.add(renderingCommentPreparation.getRenderingComment(comment));
        }
        return renderingComments;
    }
    public List<RenderingComment> getArticleRenderingCommentList(int articleId){
        List<Comment> comments = commentRepository.findCommentsByArticleId(articleId);
        List<RenderingComment> renderingComments = new ArrayList<>();
        for (Comment comment : comments) {
            renderingComments.add(renderingCommentPreparation.getRenderingComment(comment));
        }
        return renderingComments;
    }
}
