package com.snowleopard.virtual_pet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.mongo.MongoAutoConfiguration,org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration",
    "spring.main.allow-bean-definition-overriding=true"
})
class VirtualPetApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies the Spring context loads successfully
        // MongoDB and Redis are disabled for this test
    }
}
