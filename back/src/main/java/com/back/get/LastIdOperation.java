package com.back.get;

import com.back.index.LastId;
import com.back.repository.LastIdRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LastIdOperation {
    @Resource
    private LastIdRepository lastIdRepository;
    public  int getUserId() {
        int userId= lastIdRepository.findLastIdById(1).getUserId();
        LastId lastId = lastIdRepository.findLastIdById(1);
        lastId.setUserId(userId+1);
        lastIdRepository.save(lastId);
        return userId;
    }
    public int getCommentId() {
        int commentId= lastIdRepository.findLastIdById(1).getCommentId();
        LastId lastId = lastIdRepository.findLastIdById(1);
        lastId.setCommentId(commentId+1);
        lastIdRepository.save(lastId);
        return commentId;
    }
    public  int getArticleId() {
        int articleId= lastIdRepository.findLastIdById(1).getArticleId();
        LastId lastId = lastIdRepository.findLastIdById(1);
        lastId.setArticleId(articleId+1);
        lastIdRepository.save(lastId);
        return articleId;
    }
    public int getMessageId() {
        int messageId= lastIdRepository.findLastIdById(1).getMessageId();
        LastId lastId = lastIdRepository.findLastIdById(1);
        lastId.setMessageId(messageId+1);
        lastIdRepository.save(lastId);
        return messageId;
    }
    public int getViewArticleId() {
        int viewArticleId= lastIdRepository.findLastIdById(1).getViewArticleId();
        LastId lastId = lastIdRepository.findLastIdById(1);
        lastId.setViewArticleId(viewArticleId+1);
        lastIdRepository.save(lastId);
        return viewArticleId;
    }
}
