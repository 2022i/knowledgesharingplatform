package com.back.get.preparation;

import com.back.dto.RenderedComment;
import com.back.index.Comment;
import com.back.repository.CommentRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentRenderingPreparation {
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private UserDataRepository userDataRepository;
    public List<RenderedComment> getRenderedComments(List<Comment> comments){
        List<RenderedComment> renderedComments = new ArrayList<>();
        for (Comment comment : comments) {
            renderedComments.add(getRenderedComments(comment));
        }
        return renderedComments;
    }
    public RenderedComment getRenderedComments(Comment comment){
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
