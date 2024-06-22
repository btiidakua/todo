package com.example.todo.app.search;

import java.util.Collection;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    @Inject
    TodoService todoService;

    @Inject
    SearchMapper beanMapper;

    @Inject
    MessageSource messageSource;

    @ModelAttribute
    public SearchForm setUpForm() {
        SearchForm form = new SearchForm();
        return form;
    }

    @GetMapping
    public String init() {
        LOGGER.info(
                messageSource.getMessage("i.td.lg.0000", new Object[] {"init"}, Locale.JAPANESE));
        LOGGER.info(
                messageSource.getMessage("i.td.lg.0001", new Object[] {"init"}, Locale.JAPANESE));
        return "search/search";
    }

    @PostMapping
    public String result(SearchForm form, Model model) {
        LOGGER.info(
                messageSource.getMessage("i.td.lg.0000", new Object[] {"result"}, Locale.JAPANESE));
        TodoCriteria criteria = beanMapper.map(form);
        Collection<Todo> todos = todoService.findByCriteria(criteria);
        model.addAttribute("todos", todos);
        LOGGER.info(
                messageSource.getMessage("i.td.lg.0001", new Object[] {"result"}, Locale.JAPANESE));
        return "search/search";
    }
}
