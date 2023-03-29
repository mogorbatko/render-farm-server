package com.gorbatko.renderfarmserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {
    private long userId;
    private String title;
}
