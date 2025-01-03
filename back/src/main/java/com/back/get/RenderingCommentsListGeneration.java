package com.back.get;

import com.back.dto.RenderingComment;
import com.back.get.CommentsList.ArticleCommentsList;
import com.back.index.Comment;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RenderingCommentsListGeneration {
    @Resource
    private RenderingCommentPreparation renderingCommentPreparation;
    @Resource
    private ArticleCommentsList articleCommentsList;
    public List<RenderingComment> getArticleRootRenderingCommentList(int articleId){
        List<Comment> comments = articleCommentsList.getCommentsList(articleId);
        List<RenderingComment> renderingComments = new ArrayList<>();
        for (Comment comment : comments) {
            if(comment.getFatherId() == 0){
                renderingComments.add(renderingCommentPreparation.getRenderingComment(comment));
            }
        }
        return renderingComments;
    }
    public List<RenderingComment> getUserRenderingCommentedList(int commentId, int articleId){
        List<RenderingComment> renderingComments = getArticleRenderingCommentList(articleId);
        List<RenderingComment> userRenderingComments = new ArrayList<>();
        for (RenderingComment renderingComment : renderingComments) {
            if(renderingComment.getFatherId() == commentId){
                userRenderingComments.add(renderingComment);
            }
        }
        return userRenderingComments;
    }
    public List<RenderingComment> getUserRenderingCommentList(int userId, int articleId){
        List<RenderingComment> renderingComments = getArticleRenderingCommentList(articleId);
        List<RenderingComment> userRenderingComments = new ArrayList<>();
        for (RenderingComment renderingComment : renderingComments) {
            if(renderingComment.getUserId() == userId){
                userRenderingComments.add(renderingComment);
            }
        }
        return userRenderingComments;
    }
    public List<RenderingComment> getArticleRenderingCommentList(int articleId){
        List<Comment> comments = articleCommentsList.getCommentsList(articleId);
        List<RenderingComment> renderingComments = new ArrayList<>();
        for (Comment comment : comments) {
            renderingComments.add(renderingCommentPreparation.getRenderingComment(comment));
        }
        return renderingComments;
    }
}
