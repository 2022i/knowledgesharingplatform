package com.back.controller.writeCommentController;

import com.back.add.SentArticleCommentMessage;
import com.back.dto.Response;
import com.back.dto.WriteCommentRequest;
import com.back.write.WriteComment;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/write")
public class WriteArticleCommentController {
    @Resource
    private WriteComment writeComment;
    @Resource
    private SentArticleCommentMessage sentArticleCommentMessage;
    @PostMapping("/articleComment")
    public Response writeArticleComment(@RequestBody WriteCommentRequest writeCommentRequest){
        writeComment.writeComment(writeCommentRequest);
        sentArticleCommentMessage.sentMessage(writeCommentRequest.getArticleId(),writeCommentRequest.getUserId());
        return Response.successComment();
    }
}
