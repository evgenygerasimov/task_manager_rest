package com.evgenygerasimov.task_manager_rest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
@Schema(description = "Task entity")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Task ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Column(name = "title")
    @NotBlank(message = "The title is required field!")
    @Schema(description = "Task title", example = "Buy groceries", accessMode = Schema.AccessMode.READ_WRITE)
    private String title;

    @Column(name = "description")
    @NotBlank(message = "The description is required field!")
    @Schema(description = "Task description",
            example = "I need to buy some groceries", accessMode = Schema.AccessMode.READ_WRITE)
    private String description;

    @Column(name = "status")
    @NotBlank(message = "The description is required field!")
    @Schema(description = "Task status", example = "Done", accessMode = Schema.AccessMode.READ_WRITE)
    private String status;

    @Column(name = "priority")
    @NotBlank(message = "The priority is required field!")
    @Schema(description = "Task priority", example = "High", accessMode = Schema.AccessMode.READ_WRITE)
    private String priority;

    @Column(name = "author")
    @Schema(description = "Task author", example = "JohnDoe", accessMode = Schema.AccessMode.READ_ONLY)
    private String author;

    @Column(name = "executor")
    @NotBlank(message = "The executor is required field!")
    @Schema(description = "Task executor", example = "JaneDoe", accessMode = Schema.AccessMode.READ_WRITE)
    private String executor;

    @Column(name = "comment")
    @Schema(description = "Task comment", example = "I will do it today", accessMode = Schema.AccessMode.READ_WRITE)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @Schema(description = "Task author", accessMode = Schema.AccessMode.READ_ONLY)
    private User user;

}