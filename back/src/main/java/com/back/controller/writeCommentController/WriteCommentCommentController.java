package com.back.controller.writeCommentController;

import com.back.add.SentCommentCommentMessage;
import com.back.dto.Response;
import com.back.dto.WriteCommentRequest;
import com.back.write.WriteComment;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/write")
public class WriteCommentCommentController {
    @Resource
    private WriteComment writeComment;
    @Resource
    private SentCommentCommentMessage sentCommentCommentMessage;
    @PostMapping("/writeCommentComment")
    public Response writeCommentComment(@RequestBody WriteCommentRequest writeCommentRequest){
        writeComment.writeComment(writeCommentRequest);
        sentCommentCommentMessage.sentMessage(writeCommentRequest.getArticleId(),writeCommentRequest.getUserId());
        return Response.successComment();
    }
}
