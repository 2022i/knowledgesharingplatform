package com.back.controller;

import com.back.dto.Response;
import com.back.update.AddCollectionData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/addData")
public class AddCollectionDataController {
    @Resource
    private AddCollectionData addCollectionData;
    @PutMapping("/addCollectionData")
    public Response updateArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addCollectionData.updateData(articleId, userId);
        return Response.success();
    }
}
