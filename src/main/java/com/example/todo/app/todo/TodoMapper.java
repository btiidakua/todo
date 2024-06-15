package com.example.todo.app.todo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.todo.domain.model.Todo;

@Mapper
public interface TodoMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "finished", ignore = true)
    Todo map(TodoForm form);
}
