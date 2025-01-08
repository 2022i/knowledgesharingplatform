package com.back.SentSystemMessage;

import com.back.index.UserData;
import com.back.repository.CommentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SentCommentCommentMessage extends SentMessage{
    @Resource
    private CommentRepository commentRepository;
    public void sentMessage(int fatherCommentId, int messageGeneratorId){
        setMessage(findArticleIdByCommentId(fatherCommentId), messageGeneratorId);
//        findUserDataByCommentId(fatherCommentId).getMessageIds().add(message);
    }
    @Override
    protected void setMessageTitle() {
        message.setTitle("CommentComment");
    }
    private UserData findUserDataByCommentId(int fatherCommentId) {
        int uerId = commentRepository.findCommentById(fatherCommentId).getUserId();
        return userDataRepository.findUserDataById(uerId);
    }
    private int findArticleIdByCommentId(int fatherCommentId) {
        return commentRepository.findCommentById(fatherCommentId).getArticleId();
    }
}
