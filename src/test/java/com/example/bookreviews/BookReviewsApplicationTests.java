package com.example.bookreviews;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@Disabled("Отключено, так как в CI/CD нет поднятой БД PostgreSQL")

@SpringBootTest
class BookReviewsApplicationTests {
    @Disabled
    @Test
    void contextLoads() {
    }

}
