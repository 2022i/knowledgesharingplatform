package com.back.controller;

import com.back.dto.Response;
import com.back.dto.WriteCommentRequest;
import com.back.write.WriteComment;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/write")
public class WriteCommentController {
    @Resource
    private WriteComment writeComment;
    @PostMapping("/writeComment")
    public Response writeComment(@RequestBody WriteCommentRequest writeCommentRequest){
        writeComment.writeComment(writeCommentRequest);
        return Response.successComment();
    }
}
