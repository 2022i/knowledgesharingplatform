package com.back.get;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IdsList {
     List<Integer> getIdsList(int id);
}
