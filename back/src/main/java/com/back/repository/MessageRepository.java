package com.back.repository;

import com.back.index.Message;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends ElasticsearchRepository<Message,Integer> {
    Message findMessageById(int id);
}
