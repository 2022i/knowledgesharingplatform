package com.back.controller.addDataAndMessageController;

import com.back.dto.Response;
import com.back.add.AddCollectionDataAndMessage;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/addData")
public class AddCollectionDataAndMessageController {
    @Resource
    private AddCollectionDataAndMessage addCollectionData;
    @PutMapping("/addCollectionData")
    public Response addArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addCollectionData.addData(articleId, userId);
        return Response.success();
    }
}
