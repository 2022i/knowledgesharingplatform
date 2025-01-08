package com.back.controller.auditorController.checkArticlesController;

import com.back.sentSystemMessage.SentDeleteArticleSuccessMessage;
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
public class DeleteArticleSuccessController {
    @Resource
    private ArticleCheck articleCheck;
    @Resource
    private SentDeleteArticleSuccessMessage sentDeleteArticleSuccessMessage;
    @PutMapping("/deleteArticleSuccess")
    public Response deleteArticle(int articleId, int auditorId) {
        sentDeleteArticleSuccessMessage.sentMessage(articleId,auditorId);
        return articleCheck.deleteArticleSuccess(articleId);
    }
}
