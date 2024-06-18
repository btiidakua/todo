package com.example.todo.domain.repository.todo;

import java.io.Serializable;
import java.time.LocalDate;

public class TodoCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private String todoTitle;

    private boolean finished;

    private LocalDate startDate;

    private LocalDate limitDate;

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(LocalDate limitDate) {
        this.limitDate = limitDate;
    }
}
