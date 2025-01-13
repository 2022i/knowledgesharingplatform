package com.back.controller.authorActionController;

import com.back.dto.Response;
import com.back.userAction.UndoSubmitDeleteRequest;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("")
public class UndoIndirectDeletionController {
    @Resource
    private UndoSubmitDeleteRequest undoSubmitDeleteRequest;
    @Description("用户撤销删除自己创作的文章请求")
    @PutMapping("/undoDeleteRequest")
    public Response undoDeleteRequest(@RequestParam int articleId){
        return undoSubmitDeleteRequest.undoSubmitDeleteRequest(articleId);
    }
}
