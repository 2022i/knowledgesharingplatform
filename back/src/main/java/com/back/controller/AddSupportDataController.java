package com.back.controller;

import com.back.dto.Response;
import com.back.update.AddSupportData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping ("/addData")
public class AddSupportDataController {
    @Resource
    private AddSupportData addSupportData;
    @PutMapping("/addSupportData")
    public Response updateArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addSupportData.updateData(articleId, userId);
        return Response.success();
    }
}
