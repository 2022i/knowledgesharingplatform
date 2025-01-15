package com.back.index;

import com.back.mail.encoding.Encode;
import com.back.repository.AdministratorRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdministratorTest {
    @Resource
    private AdministratorRepository administratorRepository;
    @Resource
    private Encode encode;
    @Test
    public void test1() {
        Administrator administrator = new Administrator();
        administrator.setId(1);
        administrator.setUsername("xtgly1");
        administrator.setPassword(encode.encrypt("123456"));
        administratorRepository.save(administrator);
        administrator.setId(2);
        administrator.setUsername("xtgly2");
        administrator.setPassword(encode.encrypt("123456"));
        administratorRepository.save(administrator);
        administrator.setId(3);
        administrator.setUsername("xtgly3");
        administrator.setPassword(encode.encrypt("123456"));
        administratorRepository.save(administrator);
    }
}