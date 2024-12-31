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
    private UserDataRepository userDataRepository;
    @Resource
    private CommentRepository commentRepository;
    public List<RenderingComment> getRenderingCommentList(int articleId){
        List<RenderingComment> renderingComments=new ArrayList<>();
        List<Comment> comments = commentsList.getCommentsList(articleId);
        for (Comment comment : comments) {
            renderingComments.add(setRenderingComment(comment));
        }
        return renderingComments;
    }
    private RenderingComment setRenderingComment(Comment comment){
        RenderingComment renderingComment = new RenderingComment();
        renderingComment.setId(comment.getId());
        renderingComment.setUserId(comment.getUserId());
        renderingComment.setUsername(getUsernameByCommentId(comment.getId()));
        renderingComment.setContent(comment.getContent());
        renderingComment.setCreateTime(comment.getCreateTime());
        return renderingComment;
    }
    private String getUsernameByCommentId(int commentId){
        return userDataRepository.findUserDataById(commentRepository.findCommentById(commentId).getUserId()).getUsername();
    }
}
