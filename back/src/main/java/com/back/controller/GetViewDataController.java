package com.back.controller;


import com.back.dto.ViewArticle;
import com.back.get.GetViewData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/getdata")
@CrossOrigin(origins = "*")
public class GetViewDataController {
    @Resource
    private GetViewData getViewData;

    @GetMapping("/getViewList")
    public List<ViewArticle> getViewList(@RequestParam int userId){
        return getViewData.getViewList(userId);
    }

    @GetMapping("/getCount")
    public int getCount(@RequestParam int userId){
        return getViewData.getCount(userId);
    }

    @GetMapping("/getViewIDList")
    public List<Integer> getList(@RequestParam int userId){
        return getViewData.getList(userId);
    }
}
