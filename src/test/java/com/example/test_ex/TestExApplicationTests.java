package com.example.test_ex;

import com.example.test_ex.apicontrollers.UsersController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestExApplicationTests {

    @Autowired
    private UsersController usersController;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(usersController).isNotNull();
    }

}
