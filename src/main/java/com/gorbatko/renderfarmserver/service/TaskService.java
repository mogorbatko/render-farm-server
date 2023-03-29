package com.gorbatko.renderfarmserver.service;

import com.gorbatko.renderfarmserver.dto.TaskDto;
import com.gorbatko.renderfarmserver.dto.TaskResponseDto;
import com.gorbatko.renderfarmserver.dto.TaskServiceListResponseDto;
import com.gorbatko.renderfarmserver.dto.TaskServiceMessageResponseDto;
import com.gorbatko.renderfarmserver.entity.Task;
import com.gorbatko.renderfarmserver.entity.User;
import com.gorbatko.renderfarmserver.exception.RenderFarmException;
import com.gorbatko.renderfarmserver.repository.TaskRepository;
import com.gorbatko.renderfarmserver.repository.UserRepository;
import com.gorbatko.renderfarmserver.util.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.gorbatko.renderfarmserver.exception.RenderFarmException.TASK_NOT_FOUND;
import static com.gorbatko.renderfarmserver.exception.RenderFarmException.USER_NOT_FOUND;
import static com.gorbatko.renderfarmserver.util.TaskStatus.COMPLETE;

@Service
@RequiredArgsConstructor
public class TaskService {
    private static final int MAX_DELAY = 5;
    private static final int MIN_DELAY = 1;
    private static final Random RANDOM = new Random();
    private static final String TASK_ADDED = "Task has been added";
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public TaskServiceMessageResponseDto addTask(TaskDto dto) {
        int delay = RANDOM.nextInt(MAX_DELAY) + MIN_DELAY;
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RenderFarmException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND));
        Task task = new Task()
                .setTitle(dto.getTitle())
                .setUser(user)
                .setStatus(TaskStatus.RENDERING);
        Task saved = taskRepository.save(task);
        long savedId = saved.getId();
        scheduledExecutorService.schedule(() -> {
            changeStatus(savedId);
        }, delay, TimeUnit.MINUTES);
        return new TaskServiceMessageResponseDto(TASK_ADDED);
    }

    public TaskServiceListResponseDto getAllTasks(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RenderFarmException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND);
        }
        return new TaskServiceListResponseDto(taskRepository.findAllByUserId(userId));
    }

    public TaskResponseDto getTask(long userId, long taskId) {
        if (!userRepository.existsById(userId)) {
            throw new RenderFarmException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND);
        }
        if (!taskRepository.existsById(taskId)) {
            throw new RenderFarmException(HttpStatus.BAD_REQUEST, TASK_NOT_FOUND);
        }
        return new TaskResponseDto(taskRepository.findByIdAndUserId(taskId, userId));
    }

    private void changeStatus(long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setStatus(COMPLETE);
            task.setUpdateAt(Instant.now());
            taskRepository.save(task);
        }
    }


}
