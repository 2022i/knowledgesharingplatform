package com.back.controller.addDataAndMessageController;

import com.back.dto.Response;
import com.back.add.AddSupportDataAndMessage;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping ("/addDataAndSentMessage")
public class AddSupportDataAndMessageController {
    @Resource
    private AddSupportDataAndMessage addSupportDataAndMessage;
    @PutMapping("/supportData")
    public Response addArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addSupportDataAndMessage.addDataAndSentMessage(articleId, userId);
        return Response.successAddDataAndSentMessage();
    }
}
