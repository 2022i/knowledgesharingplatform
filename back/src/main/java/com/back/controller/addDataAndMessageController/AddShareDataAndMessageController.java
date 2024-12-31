package com.back.controller.addDataAndMessageController;

import com.back.add.AddShareDataAndMessage;
import com.back.dto.Response;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/addData")
public class AddShareDataAndMessageController {
    @Resource
    private AddShareDataAndMessage addShareData;
    @PutMapping("/addShareData")
    public Response addArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addShareData.addData(articleId, userId);
        return Response.success();
    }
}
