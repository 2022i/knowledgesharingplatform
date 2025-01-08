package com.back.controller.AuditorController.checkArticlesController;

import com.back.SentSystemMessage.SentApproveArticleMessage;
import com.back.auditor.ArticleCheck;
import com.back.dto.Response;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/auditor")
public class ApproveArticleController {
    @Resource
    private ArticleCheck articleCheck;
    @Resource
    private SentApproveArticleMessage sentApproveArticleMessage;
    @PutMapping("/approveArticle")
    public Response checkArticle(int articleId, int auditorId) {
        sentApproveArticleMessage.sentMessage(articleId,auditorId);
        return articleCheck.approveArticle(articleId);
    }
}
