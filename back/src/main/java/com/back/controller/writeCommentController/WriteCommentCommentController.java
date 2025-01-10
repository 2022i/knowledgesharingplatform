package com.back.controller.writeCommentController;

import com.back.sentSystemMessage.SentCommentCommentMessage;
import com.back.dto.Response;
import com.back.dto.request.WriteCommentRequest;
import com.back.write.WriteComment;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/write")
public class WriteCommentCommentController {
    @Resource
    private WriteComment writeComment;
    @Resource
    private SentCommentCommentMessage sentCommentCommentMessage;
    @Description("用户对文章的评论进行评论时调用此接口，用于更新数据库中的数据，获得用于渲染的数据需要调用http://localhost:8081/server/articleComment/getUserCommentedList?接口获得文章的根评论的评论")
    @PostMapping("/commentComment")
    public Response writeCommentComment(@RequestBody WriteCommentRequest writeCommentRequest){
        writeComment.writeComment(writeCommentRequest);
        sentCommentCommentMessage.sentMessage(writeCommentRequest.getArticleId(),writeCommentRequest.getUserId());
        return Response.successComment();
    }
}
