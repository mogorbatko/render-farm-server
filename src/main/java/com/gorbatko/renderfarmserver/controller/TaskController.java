package com.gorbatko.renderfarmserver.controller;

import com.gorbatko.renderfarmserver.dto.TaskDto;
import com.gorbatko.renderfarmserver.dto.TaskResponseDto;
import com.gorbatko.renderfarmserver.dto.TaskServiceListResponseDto;
import com.gorbatko.renderfarmserver.dto.TaskServiceMessageResponseDto;
import com.gorbatko.renderfarmserver.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gorbatko.renderfarmserver.util.Endpoints.TASK_PATH;

@RestController
@RequestMapping(TASK_PATH)
@RequiredArgsConstructor
public class TaskController {
    private static final String USER_HEADER = "X-User";
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskServiceMessageResponseDto> addTask(@RequestBody TaskDto dto,
                                                                 @RequestHeader(value = USER_HEADER) long userId) {
        dto.setUserId(userId);
        return ResponseEntity.ok().body(taskService.addTask(dto));
    }

    @GetMapping
    public ResponseEntity<TaskServiceListResponseDto> getAllTasks(@RequestHeader(value = USER_HEADER) long userId) {
        return ResponseEntity.ok().body(taskService.getAllTasks(userId));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTask(@RequestHeader(value = USER_HEADER) long userId, @PathVariable("taskId") long taskId) {
        return ResponseEntity.ok().body(taskService.getTask(userId, taskId));
    }


}
