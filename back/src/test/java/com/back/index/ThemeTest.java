package com.back.index;

import com.back.repository.ThemeRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ThemeTest {
    @Resource
    private ThemeRepository themeRepository;
    @Test
    void initializeTheme() {
        //插入name=计算机科学的数据
        Theme theme = new Theme();
        theme.setId(1);
        theme.setName("计算机科学");
        themeRepository.save(theme);

//插入name=新闻的数据
        theme = new Theme();
        theme.setId(2);
        theme.setName("新闻");
        themeRepository.save(theme);

//插入name=哲学的数据
        theme = new Theme();
        theme.setId(3);
        theme.setName("哲学");
        themeRepository.save(theme);

//插入name=心理学的数据
        theme = new Theme();
        theme.setId(4);
        theme.setName("心理学");
        themeRepository.save(theme);

//插入name=逻辑学的数据
        theme = new Theme();
        theme.setId(5);
        theme.setName("逻辑学");
        themeRepository.save(theme);

//插入name=宗教的数据
        theme = new Theme();
        theme.setId(6);
        theme.setName("宗教");
        themeRepository.save(theme);

//插入name=政治的数据
        theme = new Theme();
        theme.setId(7);
        theme.setName("政治");
        themeRepository.save(theme);

//插入name=经济的数据
        theme = new Theme();
        theme.setId(8);
        theme.setName("经济");
        themeRepository.save(theme);

//插入name=法律的数据
        theme = new Theme();
        theme.setId(9);
        theme.setName("法律");
        themeRepository.save(theme);

//插入name=教育的数据
        theme = new Theme();
        theme.setId(10);
        theme.setName("教育");
        themeRepository.save(theme);

//插入name=语言的数据
        theme = new Theme();
        theme.setId(11);
        theme.setName("语言");
        themeRepository.save(theme);

//插入name=数学的数据
        theme = new Theme();
        theme.setId(12);
        theme.setName("数学");
        themeRepository.save(theme);

//插入name=物理的数据
        theme = new Theme();
        theme.setId(13);
        theme.setName("物理");
        themeRepository.save(theme);

//插入name=化学的数据
        theme = new Theme();
        theme.setId(14);
        theme.setName("化学");
        themeRepository.save(theme);

//插入name=生命科学的数据
        theme = new Theme();
        theme.setId(15);
        theme.setName("生命科学");
        themeRepository.save(theme);

//插入name=医学的数据
        theme = new Theme();
        theme.setId(16);
        theme.setName("医学");
        themeRepository.save(theme);



    }
}