package com.example.demo.global.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();

        redisConfiguration.setHostName(host);
        redisConfiguration.setPort(port);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration);

        return lettuceConnectionFactory;
    }

    @Primary
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class)); //Json포맷 형식으로 메시지를 교환하기 위해
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(String.class)); //Json포맷 형식으로 메시지를 교환하기 위해

        //* redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class)); //Json포맷 형식으로 메시지를 교환하기 위해
        // redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class)); //Json포맷 형식으로 메시지를 교환하기 위해*//*


        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class));
        //redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class));

//
//          redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoomResponseDto.class));
//         redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoomResponseDto.class));
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /*    @Primary
        @Bean
        public RedisTemplate<?, ?> redisTemplate() {
            RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory());
            redisTemplate.setKeySerializer(new StringRedisSerializer());

            // Value 및 HashValue Serializer 설정
            Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
            redisTemplate.setValueSerializer(jsonRedisSerializer);
            redisTemplate.setHashValueSerializer(jsonRedisSerializer);

            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
            redisTemplate.afterPropertiesSet();

            return redisTemplate;
        }*/




    //redis서버와 상호작용하기 위한 템플릿 관련 설정, redis 서버에는 bytes 코드만이 저장되므로
    //key와 value에 serializer을 설정해준다
//    @Bean
//    public RedisTemplate<String, Object> redis2Template
//    (RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(connectionFactory);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class)); //Json포맷 형식으로 메시지를 교환하기 위해
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(String.class)); //Json포맷 형식으로 메시지를 교환하기 위해
//
//       //* redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class)); //Json포맷 형식으로 메시지를 교환하기 위해
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatRoom.class)); //Json포맷 형식으로 메시지를 교환하기 위해*//*
//
//        return redisTemplate;
//    }

    @Bean
    public RedisTemplate<String, Object> chatRoomRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();


        return redisTemplate;
    }
    //Topic 공유를 위해 Channel Topic을 빈으로 등록해 단일화 시켜준다.
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("chatroom");
    }
}

