package com.back.controller;

import com.back.dto.Response;
import com.back.get.GetSupportData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/getdata")
@CrossOrigin(origins = "*")
public class GetSupportCountController {
    @Resource
    private GetSupportData getSupportCount;
    @GetMapping("/getsupportCount")
    public Response getSupportCount(@RequestParam int articleId){
        getSupportCount.getSupportCounts(articleId);
        return Response.success();
    }

}
