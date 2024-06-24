package com.example.todo.app.search;

import java.io.Serializable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import com.example.todo.domain.repository.todo.TodoCriteria;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionTodoCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private TodoCriteria criteria;

    private boolean setFlg;

    public TodoCriteria getCriteria() {
        if (criteria == null) {
            criteria = new TodoCriteria();
        }
        return criteria;
    }

    public void setCriteria(TodoCriteria criteria) {
        setFlg = true;
        this.criteria = criteria;
    }

    public boolean isSetFlg() {
        return setFlg;
    }

    public void clearCriteria() {
        criteria = null;
        setFlg = false;
    }
}
