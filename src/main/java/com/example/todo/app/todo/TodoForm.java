package com.example.todo.app.todo;

import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TodoForm implements Serializable {

    public static interface TodoCreate {
    };

    public static interface TodoFinish {
    };

    public static interface TodoDelete {
    };

    private static final long serialVersionUID = 1L;

    @NotNull(groups = {TodoFinish.class, TodoDelete.class})
    private String todoId;

    @NotNull(groups = {TodoCreate.class})
    @Size(min = 1, max = 30, groups = {TodoCreate.class})
    private String todoTitle;

    @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2}", groups = {TodoCreate.class})
    private String startDate;

    @NotNull(groups = {TodoCreate.class})
    @DateTimeFormat(pattern = "uuuu/MM/dd")
    private LocalDate limitDate;

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public LocalDate getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(LocalDate limitDate) {
        this.limitDate = limitDate;
    }
}
