package com.back.controller.addDataController;

import com.back.dto.Response;
import com.back.add.AddOppositionData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/addData")
public class AddOppositionDataController {
    @Resource
    private AddOppositionData addOppositionData;
    @PutMapping("/addOppositionData")
    public Response addArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addOppositionData.addData(articleId, userId);
        return Response.success();
    }
}
