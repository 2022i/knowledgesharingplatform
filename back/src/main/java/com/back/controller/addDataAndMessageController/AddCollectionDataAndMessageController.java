package com.back.controller.addDataAndMessageController;

import com.back.dto.Response;
import com.back.add.AddCollectionDataAndMessage;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/addDataAndSentMessage")
public class AddCollectionDataAndMessageController {
    @Resource
    private AddCollectionDataAndMessage addCollectionDataAndMessage;
    @Description("用户点击“收藏”文章按钮，需要调用此接口，该接口只更新数据库中的数据，文章需要重新调用http://localhost:8081/server/getRenderedArticle接口获得该文章数据，重新渲染")
    @PutMapping("/collectionData")
    public Response addArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addCollectionDataAndMessage.addDataAndSentMessage(articleId, userId);
        return Response.successAddDataAndSentMessage();
    }
}
