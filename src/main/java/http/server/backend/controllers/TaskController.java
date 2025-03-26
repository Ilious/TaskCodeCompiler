package http.server.backend.controllers;

import http.server.backend.model.RequestCode;
import http.server.backend.model.Task;
import http.server.backend.model.enums.Status;
import http.server.backend.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/")
@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/task")
    @Operation(summary = "Post task", description = "Create a new Task with code and compiler")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Task> PostTask(@RequestBody RequestCode requestCode) {
        Task task = taskService.postTask(requestCode.code(), requestCode.compiler());
        return ResponseEntity.status(201).body(task);
    }

    @GetMapping("/status/{task_id}")
    @Operation(summary = "Get status of a task by specified id", description = "Get task's statuss by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
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
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> getResultTaskById(@PathVariable(name = "task_id") String taskId) {
        String result = taskService.getResultByTaskId(taskId);
        return ResponseEntity.ok().body(result);
    }
}
