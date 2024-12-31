package com.back.controller.addDataAndMessageController;

import com.back.dto.Response;
import com.back.add.AddSupportDataAndMessage;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping ("/addData")
public class AddSupportDataAndMessageController {
    @Resource
    private AddSupportDataAndMessage addSupportData;
    @PutMapping("/addSupportData")
    public Response addArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addSupportData.addData(articleId, userId);
        return Response.success();
    }
}
