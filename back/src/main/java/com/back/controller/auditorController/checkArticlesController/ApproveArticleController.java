package com.back.controller.auditorController.checkArticlesController;

import com.back.sentSystemMessage.SentApproveArticleMessage;
import com.back.auditor.ArticleCheck;
import com.back.dto.Response;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
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
    @Description("审核员审核待发布的文章，审核员点击“通过”按钮，调用此接口，该接口只更新数据库中的数据，待审核列表需要调用http://localhost:8081/server/auditor/getUnCheckArticles接口获得未审核的文章数据，重新渲染")
    @PutMapping("/approveArticle")
    public Response checkArticle(int articleId, int auditorId) {
        sentApproveArticleMessage.sentMessage(articleId,auditorId);
        return articleCheck.approveArticle(articleId);
    }
}
