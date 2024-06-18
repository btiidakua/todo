package com.example.todo.app.search;

import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Size;

public class SearchForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 30)
    private String todoTitle;

    private boolean finished;

    @DateTimeFormat(pattern = "uuuu/MM/dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "uuuu/MM/dd")
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
