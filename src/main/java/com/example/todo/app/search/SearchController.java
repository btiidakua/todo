package com.example.todo.app.search;

import java.util.Collection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.todo.domain.model.Todo;
import com.example.todo.domain.repository.todo.TodoCriteria;
import com.example.todo.domain.service.todo.TodoService;
import jakarta.inject.Inject;

@Controller
@RequestMapping("search")
public class SearchController {

    @Inject
    TodoService todoService;

    @Inject
    SearchMapper beanMapper;

    @ModelAttribute
    public SearchForm setUpForm() {
        SearchForm form = new SearchForm();
        return form;
    }

    @GetMapping
    public String init() {
        return "search/search";
    }

    @PostMapping
    public String result(SearchForm form, Model model) {
        TodoCriteria criteria = beanMapper.map(form);
        Collection<Todo> todos = todoService.findByCriteria(criteria);
        model.addAttribute("todos", todos);
        return "search/search";
    }
}
