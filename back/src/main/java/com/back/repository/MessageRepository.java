package com.back.repository;

import com.back.index.Message;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends ElasticsearchRepository<Message,Integer> {
    List<Message> findMessagesByAuthorId(int authorId);
}
