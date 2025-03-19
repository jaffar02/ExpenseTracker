package com.example.userservice.deserializer;

import com.example.userservice.entities.UserInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.ByteBuffer;
import java.util.Map;

@Slf4j
public class UserInfoDeserializer implements Deserializer<UserInfoDto> {

    ObjectMapper objectMapper;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public UserInfoDto deserialize(String s, byte[] bytes) {
        UserInfoDto userInfoDto = null;
        objectMapper = new ObjectMapper();
        try {
            userInfoDto = objectMapper.readValue(bytes, UserInfoDto.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return userInfoDto;
    }

    @Override
    public UserInfoDto deserialize(String topic, Headers headers, byte[] data) {
        return Deserializer.super.deserialize(topic, headers, data);
    }

    @Override
    public UserInfoDto deserialize(String topic, Headers headers, ByteBuffer data) {
        return Deserializer.super.deserialize(topic, headers, data);
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
