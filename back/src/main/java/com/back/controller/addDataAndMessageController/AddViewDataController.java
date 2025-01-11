package com.back.controller.addDataAndMessageController;

import com.back.add.AddViewData;
import com.back.dto.Response;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/addData")
public class AddViewDataController {
    @Resource
    private AddViewData addViewData;
    @Description("用户“从文章的概要展示点击进入文章正文时”，调用此接口，该接口用于更新数据库中的数据")
    @PutMapping("/viewData")
    public Response addViewData(@RequestParam int articleId, @RequestParam int userId) {
        return addViewData.addViewData(articleId, userId);
    }
}
