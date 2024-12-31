package com.back.get;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IdsList {
    public List<Integer> getIdsList(int id);
}
