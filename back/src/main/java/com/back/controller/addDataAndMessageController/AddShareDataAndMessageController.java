package com.back.controller.addDataAndMessageController;

import com.back.add.AddShareDataAndMessage;
import com.back.dto.Response;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/addDataAndSentMessage")
public class AddShareDataAndMessageController {
    @Resource
    private AddShareDataAndMessage addShareDataAndMessage;
    @PutMapping("/shareData")
    public Response addArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addShareDataAndMessage.addDataAndSentMessage(articleId, userId);
        return Response.successAddDataAndSentMessage();
    }
}
