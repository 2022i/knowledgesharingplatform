package com.back.controller.reduceDataController;

import com.back.dto.Response;
import com.back.reduce.ReduceOppositionData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/reduce")
public class ReduceOpposeDataController {
    @Resource
    private ReduceOppositionData reduceOppositionData;
    @PutMapping("/opposeData")
    public Response reduceOpposeData(@RequestParam int articleId, @RequestParam int userId){
        reduceOppositionData.reduceData(articleId, userId);
        return Response.successAddDataAndSentMessage();
    }
}
