package com.back.write;

import com.back.dto.LastId;
import com.back.dto.WriteCommentRequest;
import com.back.index.Comment;
import com.back.repository.CommentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WriteComment {
    @Resource
    private CommentRepository commentRepository;
    public void writeComment(WriteCommentRequest writeCommentRequest){
        Comment comment = new Comment();
        comment.setId(LastId.getCommentId());
        comment.setFatherId(writeCommentRequest.getFatherId());
        comment.setUserId(writeCommentRequest.getUserId());
        comment.setContent(writeCommentRequest.getContent());
        comment.setArticleId(writeCommentRequest.getArticleId());
        comment.setCreateTime(LocalDateTime.now());
        commentRepository.save(comment);
    }
}
