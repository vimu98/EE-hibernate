package org.example.demohibernate.util;

import com.google.gson.Gson;
import org.example.demohibernate.dto.UserDTO;

public class JsonUtil {
    private static final Gson GSON = new Gson();

    public static UserDTO fromJson(String json) {
        return GSON.fromJson(json, UserDTO.class);
    }

    public static String toJson(UserDTO userDTO) {
        return GSON.toJson(userDTO);
    }
}