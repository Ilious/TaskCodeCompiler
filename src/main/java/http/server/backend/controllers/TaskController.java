package http.server.backend.controllers;

import http.server.backend.model.CodeResult;
import http.server.backend.model.Task;
import http.server.backend.model.enums.Status;
import http.server.backend.model.request.RequestTask;
import http.server.backend.service.interfaces.ITaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@SecurityRequirement(name = "bearerAuth")
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")
@RestController
public class TaskController {

    private final ITaskService taskService;

    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/task")
    @Operation(summary = "Post task", description = "Create a new Task with code and compiler")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Token is not valid anymore",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Task by this id is already exists",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error creating task",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Task> PostTask(@RequestBody @Valid RequestTask requestTask) {
        Task task = taskService.postTask(requestTask.code(), requestTask.compiler());
        return ResponseEntity.status(201).body(task);
    }

    @GetMapping("/status/{task_id}")
    @Operation(summary = "Get status of a task by specified id", description = "Get task's status by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Token is not valid anymore",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Status> getStatusTaskById(@PathVariable(name = "task_id") String taskId) {
        Status status = taskService.getStatusByTaskId(taskId);
        return ResponseEntity.ok().body(status);
    }

    @GetMapping("/result/{task_id}")
    @Operation(summary = "Get result of a task by specified id", description = "Get task's code and compiler by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "No token",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Token is not valid anymore",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CodeResult> getResultTaskById(@PathVariable(name = "task_id") String taskId) {
        CodeResult result = taskService.getResultByTaskId(taskId);
        return ResponseEntity.ok().body(result);
    }
}
