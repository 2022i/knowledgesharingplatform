package com.back.get;

import com.back.dto.RenderedComment;
import com.back.index.Comment;
import com.back.repository.CommentRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CommentRenderingPreparation {
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private UserDataRepository userDataRepository;

    public RenderedComment getRenderedComment(Comment comment){
        RenderedComment renderedComment = new RenderedComment();
        renderedComment.setId(comment.getId());
        renderedComment.setFatherId(comment.getFatherId());
        renderedComment.setUserId(comment.getUserId());
        renderedComment.setUsername(getUsername(comment.getId()));
        renderedComment.setContent(comment.getContent());
        renderedComment.setReplyCount(commentRepository.findCommentsByFatherId(comment.getId()).size());
        renderedComment.setCreateTime(comment.getCreateTime());
        return renderedComment;
    }
    private String getUsername(int commentId){
        int userId = commentRepository.findCommentById(commentId).getUserId();
        return userDataRepository.findUserDataById(userId).getUsername();
    }
}
