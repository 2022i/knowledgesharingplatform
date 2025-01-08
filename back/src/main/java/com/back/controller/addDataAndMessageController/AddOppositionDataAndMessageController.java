package com.back.controller.addDataAndMessageController;

import com.back.dto.Response;
import com.back.add.AddOppositionDataAndMessage;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/addDataAndSentMessage")
public class AddOppositionDataAndMessageController {
    @Resource
    private AddOppositionDataAndMessage addOppositionDataAndMessage;
    @PutMapping("/oppositionData")
    public Response addArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addOppositionDataAndMessage.addDataAndSentMessage(articleId, userId);
        return Response.successAddDataAndSentMessage();
    }
}
