package com.gorbatko.renderfarmserver.dto;

import com.gorbatko.renderfarmserver.entity.Task;

import java.util.List;

public record TaskServiceListResponseDto(List<Task> tasks) {
}
