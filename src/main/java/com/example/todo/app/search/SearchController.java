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

    @Inject
    SessionTodoCriteria sessionTodoCriteria;

    @ModelAttribute
    public SearchForm setUpForm() {
        SearchForm form = new SearchForm();
        return form;
    }

    @GetMapping
    public String init(Model model) {
        LOGGER.info(
                messageSource.getMessage("i.td.lg.0000", new Object[] {"init"}, Locale.JAPANESE));
        if (sessionTodoCriteria.isSetFlg()) {
            Collection<Todo> todos = todoService.findByCriteria(sessionTodoCriteria.getCriteria());
            model.addAttribute("todos", todos);
            model.addAttribute(beanMapper.mapCriteria2Form(sessionTodoCriteria.getCriteria()));
        }
        LOGGER.info(
                messageSource.getMessage("i.td.lg.0001", new Object[] {"init"}, Locale.JAPANESE));
        return "search/search";
    }

    @PostMapping
    public String result(SearchForm form, Model model) {
        LOGGER.info(
                messageSource.getMessage("i.td.lg.0000", new Object[] {"result"}, Locale.JAPANESE));
        TodoCriteria criteria = beanMapper.mapForm2Criteria(form);
        Collection<Todo> todos = todoService.findByCriteria(criteria);
        model.addAttribute("todos", todos);
        sessionTodoCriteria.setCriteria(criteria);
        LOGGER.info(
                messageSource.getMessage("i.td.lg.0001", new Object[] {"result"}, Locale.JAPANESE));
        return "search/search";
    }

    @GetMapping("reset")
    public String reset() {
        sessionTodoCriteria.clearCriteria();
        return "search/search";
    }
}
