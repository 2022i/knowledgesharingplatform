package com.back.controller.auditorController.checkArticlesController;

import com.back.sentSystemMessage.SentDeleteArticleFailMessage;
import com.back.auditor.ArticleCheck;
import com.back.dto.Response;
import com.back.dto.request.AuditorRequest;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auditor")
public class DeleteArticleFailController {
    @Resource
    private ArticleCheck articleCheck;
    @Resource
    private SentDeleteArticleFailMessage sentDeleteArticleFailMessage;
    @PutMapping("/deleteArticleFail")
    public Response deleteArticleFail(@RequestBody AuditorRequest auditorRequest) {
        sentDeleteArticleFailMessage.sentMessage(auditorRequest);
        return articleCheck.deleteArticleFail(auditorRequest.getArticleId());
    }
}
