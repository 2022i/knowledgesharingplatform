package com.back.controller.writeCommentController;

import com.back.sentSystemMessage.SentArticleCommentMessage;
import com.back.dto.Response;
import com.back.dto.request.WriteCommentRequest;
import com.back.write.WriteComment;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/write")
public class WriteArticleCommentController {
    @Resource
    private WriteComment writeComment;
    @Resource
    private SentArticleCommentMessage sentArticleCommentMessage;
    @Description("用户对文章进行评论时即主评论，rootComment，调用此接口，获得用于渲染的数据需要调用http://localhost:8081/server/articleComment/getArticleRootComments?接口获得文章的根评论即主评论")
    @PostMapping("/articleComment")
    public Response writeArticleComment(@RequestBody WriteCommentRequest writeCommentRequest){
        writeComment.writeComment(writeCommentRequest);
        sentArticleCommentMessage.sentMessage(writeCommentRequest.getArticleId(),writeCommentRequest.getUserId());
        return Response.successComment();
    }
}
