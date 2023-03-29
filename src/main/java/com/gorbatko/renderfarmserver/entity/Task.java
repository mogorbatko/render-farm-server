package com.gorbatko.renderfarmserver.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gorbatko.renderfarmserver.util.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "tasks")
@Accessors(chain = true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    private String title;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createAt = Instant.now();

    @Column(nullable = false)
    private Instant updateAt = Instant.now();

}
