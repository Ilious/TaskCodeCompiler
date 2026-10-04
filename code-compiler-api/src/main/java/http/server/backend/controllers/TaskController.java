package http.server.backend.controllers;

import http.server.backend.service.interfaces.ITaskService;
import http.server.dto.CodeResultDto;
import http.server.dto.TaskDto;
import http.server.dto.enums.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


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

    @PostMapping("/task/{compiler}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Submit code for execution",
            description = "Create a new execution task. Supported compilers: py, c and c++"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Unsupported compiler or invalid request"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Session token has expired"),
            @ApiResponse(responseCode = "500", description = "Failed to create or submit the task")
    })
    public TaskDto PostTask(
            @RequestBody String code,
            @PathVariable String compiler,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        return taskService.postTask(code, compiler, userId);
    }


    @GetMapping("/status/{task_id}")
    @Operation(
            summary = "Get task status",
            description = "Get the current execution status of a task"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task status returned successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Session token has expired"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public Status getStatusTaskById(
            @PathVariable(name = "task_id") UUID taskId,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        return taskService.getStatusByTaskId(taskId, userId);
    }


    @GetMapping("/result/{task_id}")
    @Operation(
            summary = "Get task result",
            description = "Get the execution result of a task"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task result returned successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Session token has expired"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public CodeResultDto getResultTaskById(
            @PathVariable(name = "task_id") UUID taskId,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        return taskService.getResultByTaskId(taskId, userId);
    }
}
