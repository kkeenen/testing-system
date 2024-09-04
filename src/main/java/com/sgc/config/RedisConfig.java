package com.sgc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;

@Component
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60 * 60)
@Controller
public class RedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 设置key的序列化器 要求key必须是字符串
        redisTemplate.setKeySerializer(RedisSerializer.string());

        //设置value序列化器
        RedisSerializer<Object> valueSerializer = GenericJackson2JsonRedisSerializer.builder().objectMapper(objectMapper).defaultTyping(true).build();
        redisTemplate.setValueSerializer(valueSerializer);

        // 设置哈希序列化器
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);

        RedisSerializer<Object> valueSerializer = GenericJackson2JsonRedisSerializer
                .builder()
                .objectMapper(objectMapper)
                .defaultTyping(true)
                .build();

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(valueSerializer))
                .entryTtl(Duration.ofHours(2));

        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }

    @GetMapping(value = "/session", produces = "application/json;charset=utf-8")
    public ResponseEntity<String> session(HttpSession session) {
        String user = (String) session.getAttribute("user");
        if (user == null) {
            user = "孙小美";
            session.setAttribute("user", user);
            System.out.println(user);
        } else {
            System.out.println(user);  //打印到控制台
        }        return ResponseEntity.ok(session.getId()); //显示到前端页面
    }

}
