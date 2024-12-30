package com.back.controller.addDataController;

import com.back.dto.Response;
import com.back.add.AddCollectionData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/addData")
public class AddCollectionDataController {
    @Resource
    private AddCollectionData addCollectionData;
    @PutMapping("/addCollectionData")
    public Response addArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addCollectionData.addData(articleId, userId);
        return Response.success();
    }
}
