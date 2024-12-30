package com.back.controller.addDataController;

import com.back.dto.Response;
import com.back.add.AddSupportData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping ("/addData")
public class AddSupportDataController {
    @Resource
    private AddSupportData addSupportData;
    @PutMapping("/addSupportData")
    public Response addArticleSupportData(@RequestParam int articleId, @RequestParam int userId) {
        addSupportData.addData(articleId, userId);
        return Response.success();
    }
}
