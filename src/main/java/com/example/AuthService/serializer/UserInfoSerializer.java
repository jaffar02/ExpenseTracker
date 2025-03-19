package com.example.AuthService.serializer;

import com.example.AuthService.model.UserInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class UserInfoSerializer implements Serializer<UserInfoDto> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, UserInfoDto userInfoDto) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(userInfoDto).getBytes();
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Serialization error: {}", e.getMessage());
        }
        return retVal;
    }

    @Override
    public byte[] serialize(String topic, Headers headers, UserInfoDto data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
