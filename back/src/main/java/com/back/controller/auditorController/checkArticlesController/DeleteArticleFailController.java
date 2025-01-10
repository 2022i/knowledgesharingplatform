package com.back.controller.auditorController.checkArticlesController;

import com.back.sentSystemMessage.SentDeleteArticleFailMessage;
import com.back.auditor.ArticleCheck;
import com.back.dto.Response;
import com.back.dto.request.AuditorRequest;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auditor")
public class DeleteArticleFailController {
    @Resource
    private ArticleCheck articleCheck;
    @Resource
    private SentDeleteArticleFailMessage sentDeleteArticleFailMessage;
    @Description("审核员审核待删除的文章，审核员点击“不删除”按钮，调用此接口，该接口只更新数据库中的数据，待删除文章列表需要调用http://localhost:8081/server/auditor/getDeletingArticlesList接口获得待删除的文章数据，重新渲染")
    @PutMapping("/deleteArticleFail")
    public Response deleteArticleFail(@RequestBody AuditorRequest auditorRequest) {
        sentDeleteArticleFailMessage.sentMessage(auditorRequest);
        return articleCheck.deleteArticleFail(auditorRequest.getArticleId());
    }
}
