package com.back.controller.auditorController.checkArticlesController;

import com.back.sentSystemMessage.SentRejectArticleMessage;
import com.back.auditor.ArticleCheck;
import com.back.dto.Response;
import com.back.dto.request.AuditorRequest;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auditor")
public class RejectArticleController {
    @Resource
    private ArticleCheck articleCheck;
    @Resource
    private SentRejectArticleMessage sentRejectArticleMessage;
    @Description("审核员审核待发布的文章，审核员点击“不通过”按钮，调用此接口，该接口只更新数据库中的数据，待审核列表需要调用http://localhost:8081/server/auditor/getUnCheckArticles接口获得未审核的文章数据，重新渲染")
    @PutMapping("/rejectArticle")
    public Response rejectArticle(@RequestBody AuditorRequest auditorRequest) {
        sentRejectArticleMessage.sentMessage(auditorRequest);
        return articleCheck.rejectArticle(auditorRequest.getArticleId());
    }
}
