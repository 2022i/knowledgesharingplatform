package com.back.controller.addDataAndMessageController;

import com.back.add.AddShareDataAndMessage;
import com.back.dto.Response;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/addDataAndSentMessage")
public class AddShareDataAndMessageController {
    @Resource
    private AddShareDataAndMessage addShareDataAndMessage;
    @Description("用户点击“分享”文章按钮，需要调用此接口，该接口只更新数据库中的数据，文章需要重新调用http://localhost:8081/server/getRenderedArticle接口，获得该文章数据，重新渲染")
    @PutMapping("/shareData")
    public Response addArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addShareDataAndMessage.addDataAndSentMessage(articleId, userId);
        return Response.successAddDataAndSentMessage();
    }
}
