package com.back.controller.AuditorController;

import com.back.add.SentFailedAuditMessage;
import com.back.add.SentSuccessAuditMessage;
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
public class CheckArticlesController {
    @Resource
    private ArticleCheck articleCheck;
    @Resource
    private SentFailedAuditMessage sentFailedAuditMessage;
    @Resource
    private SentSuccessAuditMessage sentSuccessAuditMessage;
    @PutMapping("/approveArticle")
    public Response checkArticle(int articleId,int auditorId) {
        sentSuccessAuditMessage.sentMessage(articleId,auditorId);
        return articleCheck.approveArticle(articleId);
    }
    @PutMapping("/rejectArticle")
    public Response rejectArticle(int articleId, int auditorId) {
        sentFailedAuditMessage.sentMessage(articleId,auditorId);
        return articleCheck.rejectArticle(articleId);
    }
    @PutMapping("/deleteArticle")
    public Response deleteArticle(int articleId,int auditorId) {
        sentFailedAuditMessage.sentMessage(articleId,auditorId);
        return articleCheck.rejectArticle(articleId);
    }
}
