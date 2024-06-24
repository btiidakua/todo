package com.example.todo.app.search;

import org.mapstruct.Mapper;
import com.example.todo.domain.repository.todo.TodoCriteria;

@Mapper
public interface SearchMapper {

    TodoCriteria mapForm2Criteria(SearchForm form);

    SearchForm mapCriteria2Form(TodoCriteria criteria);
}
