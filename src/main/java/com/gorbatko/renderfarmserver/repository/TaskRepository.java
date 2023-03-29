package com.gorbatko.renderfarmserver.repository;

import com.gorbatko.renderfarmserver.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserId(long userId);
    Task findByIdAndUserId(long id, long userId);
}
