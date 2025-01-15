package com.back.sentSystemMessage;

import com.back.dto.request.WriteCommentRequest;
import com.back.get.LastIdOperation;
import com.back.index.UserData;
import com.back.repository.CommentRepository;
import com.back.repository.MessageRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SentCommentCommentMessage extends SentMessage{
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private LastIdOperation lastIdOperation;
    @Resource
    private MessageRepository messageRepository;
    public void sentMessage(WriteCommentRequest writeCommentRequest) {
        message.setTitle("CommentComment");
        message.setId(lastIdOperation.getMessageId());
        int messageRecipientId = findUserIdByCommentId(writeCommentRequest.getFatherId());
        message.setMessageRecipientId(messageRecipientId);
        message.setCreateTime(LocalDateTime.now());
        int messageGeneratorId = writeCommentRequest.getUserId();
        message.setMessageGeneratorId(messageGeneratorId);
        message.setMessageGeneratorName(userDataRepository.findUserDataById(messageGeneratorId).getUsername());
        message.setRead(false);
        message.setContent(writeCommentRequest.getContent());
        messageRepository.save(message);
        UserData userData=userDataRepository.findUserDataById(messageRecipientId);
        List<Integer> messageList= userData.getMessageIds();
        messageList.add(message.getId());
        userData.setMessageIds(messageList);
        userDataRepository.save(userData);
    }
    @Override
    protected void setMessageTitle() {
        message.setTitle("CommentComment");
    }
    private int findUserIdByCommentId(int fatherCommentId) {
        return commentRepository.findCommentById(fatherCommentId).getUserId();
    }
}
