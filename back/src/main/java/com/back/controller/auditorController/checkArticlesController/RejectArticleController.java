package com.back.controller.auditorController.checkArticlesController;

import com.back.sentSystemMessage.SentRejectArticleMessage;
import com.back.auditor.ArticleCheck;
import com.back.dto.Response;
import com.back.dto.request.AuditorRequest;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auditor")
public class RejectArticleController {
    @Resource
    private ArticleCheck articleCheck;
    @Resource
    private SentRejectArticleMessage sentRejectArticleMessage;
    @PutMapping("/rejectArticle")
    public Response rejectArticle(@RequestBody AuditorRequest auditorRequest) {
        sentRejectArticleMessage.sentMessage(auditorRequest);
        return articleCheck.rejectArticle(auditorRequest.getArticleId());
    }
}
