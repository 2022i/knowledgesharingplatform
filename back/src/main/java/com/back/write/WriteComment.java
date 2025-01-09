package com.back.write;

import com.back.get.LastIdOperation;
import com.back.dto.request.WriteCommentRequest;
import com.back.index.Comment;
import com.back.repository.CommentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class WriteComment {
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private LastIdOperation lastIdOperation;
    public void writeComment(WriteCommentRequest writeCommentRequest){
        Comment comment = new Comment();
        comment.setId(lastIdOperation.getCommentId());
        comment.setFatherId(writeCommentRequest.getFatherId());
        comment.setUserId(writeCommentRequest.getUserId());
        comment.setContent(writeCommentRequest.getContent());
        comment.setArticleId(writeCommentRequest.getArticleId());
        comment.setCreateTime(LocalDateTime.now());
        commentRepository.save(comment);
    }
}
