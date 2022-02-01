package com.example.devtraining;

import com.example.devtraining.model.Department;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
@EnableCaching
public class DevtrainingApplication implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(DevtrainingApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DevtrainingApplication.class, args);
    }

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory(){
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Department> redisTemplate() {
        final RedisTemplate<String, Department> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory());

        return template;
    }

    @Override
    public void run(String... args) {
        logger.debug("Application STARTED");
    }
}
