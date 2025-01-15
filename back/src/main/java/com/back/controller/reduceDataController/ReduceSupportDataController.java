package com.back.controller.reduceDataController;

import com.back.dto.Response;
import com.back.reduce.ReduceSupportData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/reduce")
public class ReduceSupportDataController {
    @Resource
    private ReduceSupportData reduceSupportData;
    @PutMapping("/supportData")
    public Response reduceSupportData(@RequestParam int articleId, @RequestParam int userId){
        reduceSupportData.reduceData(articleId, userId);
        return Response.successAddDataAndSentMessage();
    }
}
