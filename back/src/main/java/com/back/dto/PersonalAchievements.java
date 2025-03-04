package com.back.dto;

import lombok.Data;

@Data
public class PersonalAchievements {
    private String userName;
    private int writeArticleCount;
    private int supportedCount;
    private int OpposedCount;
    private int sharedCount;
    private int CollectedCount;
    private int followCount;
    private int fansCount;
    private boolean haveMessage;
}
