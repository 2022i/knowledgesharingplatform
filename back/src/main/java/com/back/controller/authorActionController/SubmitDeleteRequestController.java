package com.back.controller.authorActionController;

import com.back.dto.Response;
import com.back.userAction.SubmitDeleteRequest;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("")
public class SubmitDeleteRequestController {
    @Resource
    private SubmitDeleteRequest submitDeleteRequest;
    @Description("用户提交删除自己创作的文章请求，待审核，通过后删除")
    @PutMapping ("/submitDeleteRequest")
    public Response submitDeleteRequest(@RequestParam int articleId){
        return submitDeleteRequest.submitDeleteRequest(articleId);
    }
}
