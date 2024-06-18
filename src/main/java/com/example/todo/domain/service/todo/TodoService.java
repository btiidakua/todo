package com.example.todo.domain.service.todo;

import java.util.Collection;
import com.example.todo.domain.model.Todo;
import com.example.todo.domain.repository.todo.TodoCriteria;

public interface TodoService {
    Collection<Todo> findAll();

    Todo create(Todo todo);

    Todo finish(String todoId);

    void delete(String todoId);

    Collection<Todo> findByCriteria(TodoCriteria criteria);
}
