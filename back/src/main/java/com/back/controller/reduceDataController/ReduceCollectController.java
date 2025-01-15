package com.back.controller.reduceDataController;

import com.back.dto.Response;
import com.back.reduce.ReduceCollectionData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/reduce")
public class ReduceCollectController {
    @Resource
    private ReduceCollectionData reduceCollectionData;
    @PutMapping("/collectData")
    public Response reduceCollectData(@RequestParam int articleId, @RequestParam int userId){
        reduceCollectionData.reduceData(articleId, userId);
        return Response.successAddDataAndSentMessage();
    }
}
