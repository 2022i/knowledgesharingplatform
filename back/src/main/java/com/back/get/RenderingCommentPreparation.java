package com.back.get;

import com.back.dto.RenderingComment;
import com.back.index.Comment;
import com.back.repository.CommentRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RenderingCommentPreparation {
    @Resource
    private CommentsList commentsList;
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private UserDataRepository userDataRepository;
    public List<RenderingComment> getRenderingCommentList(int articleId){
        List<Comment> comments = commentsList.getCommentsList(articleId);
        List<RenderingComment> renderingComments = new ArrayList<>();
        for (Comment comment : comments) {
            renderingComments.add(getRenderingComment(comment));
        }
        return renderingComments;
    }
    private RenderingComment getRenderingComment(Comment comment){
        RenderingComment renderingComment = new RenderingComment();
        renderingComment.setId(comment.getId());
        renderingComment.setFatherId(comment.getFatherId());
        renderingComment.setUserId(comment.getUserId());
        renderingComment.setUsername(getUsername(comment.getId()));
        renderingComment.setContent(comment.getContent());
        renderingComment.setCreateTime(comment.getCreateTime());
        return renderingComment;
    }
    private String getUsername(int commentId){
        int userId = commentRepository.findCommentById(commentId).getUserId();
        return userDataRepository.findUserDataById(userId).getUsername();
    }
}
