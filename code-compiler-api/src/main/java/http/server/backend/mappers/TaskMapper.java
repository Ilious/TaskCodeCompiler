package http.server.backend.mappers;

import http.server.backend.model.task.Task;
import http.server.dto.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    TaskDto toDto(Task task);
}
