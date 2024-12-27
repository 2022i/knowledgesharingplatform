package com.back.controller;

import com.back.dto.Response;
import com.back.update.AddOppositionData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/addData")
public class AddOppositionDataController {
    @Resource
    private AddOppositionData addOppositionData;
    @PutMapping("/addOppositionData")
    public Response updateArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addOppositionData.updateData(articleId, userId);
        return Response.success();
    }
}
