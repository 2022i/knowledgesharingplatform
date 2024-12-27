package com.back.controller;

import com.back.dto.Response;
import com.back.update.UpdateSupportData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping ("/updateData")
public class UpdateSupportDataController {
    @Resource
    private UpdateSupportData updateSupportData;
    @PutMapping("/updateSupportData")
    public Response updateArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        updateSupportData.updateData(articleId, userId);
        return Response.success();
    }
}
