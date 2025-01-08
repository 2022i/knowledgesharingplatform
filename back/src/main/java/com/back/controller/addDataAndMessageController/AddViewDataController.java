package com.back.controller.addDataAndMessageController;

import com.back.add.AddViewData;
import com.back.dto.Response;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/addData")
public class AddViewDataController {
    @Resource
    private AddViewData addViewData;
    @PutMapping("/viewData")
    public Response addViewData(int articleId, int userId) {
        return addViewData.addViewData(articleId, userId);
    }
}
