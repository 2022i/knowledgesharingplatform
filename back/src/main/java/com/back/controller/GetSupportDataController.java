package com.back.controller;

import com.back.dto.Response;
import com.back.get.GetSupportData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/getList")
@CrossOrigin(origins = "*")
public class GetSupportDataController {
    @Resource
    private GetSupportData getSupportData;
    @GetMapping("/getsupportIDList")
    public Response getArticleIDList(@RequestParam int userId){
        getSupportData.getList(userId);
        return Response.success();
    }

    @GetMapping("/getSupportList")
    public Response getArticleList(@RequestParam int userId){
        getSupportData.getArticleList(userId);
        return Response.success();
    }
}
