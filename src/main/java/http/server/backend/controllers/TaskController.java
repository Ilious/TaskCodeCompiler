package http.server.backend.controllers;

import http.server.backend.model.CodeResult;
import http.server.backend.model.Task;
import http.server.backend.model.enums.Status;
import http.server.backend.service.interfaces.ITaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


//@SecurityRequirement(name = "bearerAuth")
//@SecurityScheme(
//        name = "bearerAuth",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer")
@RestController
public class TaskController {

    private final ITaskService taskService;

    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/task/{compiler}")
    @ResponseStatus(HttpStatus.CREATED)
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
    public Task PostTask(@RequestBody String code, @PathVariable String compiler) {
        return taskService.postTask(code, compiler);
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
    public Status getStatusTaskById(@PathVariable(name = "task_id") String taskId) {
        return taskService.getStatusByTaskId(taskId);
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
    public CodeResult getResultTaskById(@PathVariable(name = "task_id") String taskId) {
        return taskService.getResultByTaskId(taskId);
    }
}
