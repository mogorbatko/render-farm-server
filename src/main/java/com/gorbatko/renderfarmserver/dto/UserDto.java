package com.gorbatko.renderfarmserver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserDto {
    private String login;
    private String password;
}
