package com.back.controller;

import com.back.dto.Response;
import com.back.update.UpdateSupportData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping ("/updateSupportData")
public class UpdateSupportDataController {
    @Resource
    private UpdateSupportData updateSupportData;
    @PutMapping("/updateArticleSupportData")
    public Response updateArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        updateSupportData.updateArticleData(articleId, userId);
        return Response.success();
    }
    @PutMapping("/updateUserSupportData")
    public Response updateUserSupportData(@RequestParam int articleId, @RequestParam int userId) {
        updateSupportData.updateUserData(articleId, userId);
        return Response.success();
    }
}
