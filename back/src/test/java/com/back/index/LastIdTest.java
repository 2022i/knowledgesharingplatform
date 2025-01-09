package com.back.index;

import com.back.repository.LastIdRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LastIdTest {
    @Resource
    private LastIdRepository lastIdRepository;
    @Test
    void test(){
        LastId lastId=new LastId();
        lastId.setId(1);
        lastId.setArticleId(1);
        lastId.setUserId(1);
        lastId.setCommentId(1);
        lastId.setMessageId(1);
        lastId.setViewArticleId(1);
        lastIdRepository.save(lastId);
    }
}