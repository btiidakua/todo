package com.example.todo.app.todo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Collection;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import com.example.todo.app.todo.TodoForm.TodoCreate;
import com.example.todo.app.todo.TodoForm.TodoDelete;
import com.example.todo.app.todo.TodoForm.TodoFinish;
import com.example.todo.domain.model.Todo;
import com.example.todo.domain.service.todo.TodoService;
import jakarta.inject.Inject;
import jakarta.validation.groups.Default;

@Controller
@RequestMapping("todo")
public class TodoController {

    @Inject
    TodoService todoService;

    @Inject
    TodoMapper beanMapper;

    @ModelAttribute
    public TodoForm setUpForm() {
        TodoForm form = new TodoForm();
        return form;
    }

    @GetMapping("list")
    public String list(Model model) {
        Collection<Todo> todos = todoService.findAll();
        model.addAttribute("todos", todos);
        return "todo/list";
    }

    @PostMapping("create")
    public String create(@Validated({Default.class, TodoCreate.class}) TodoForm todoForm,
            BindingResult bindingResult, Model model, RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return list(model);
        }

        Todo todo = beanMapper.map(todoForm);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd")
                .withLocale(Locale.JAPANESE).withResolverStyle(ResolverStyle.STRICT);
        todo.setStartDate(LocalDate.parse(todoForm.getStartDate(), formatter));
        try {
            todoService.create(todo);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return list(model);
        }

        attributes.addFlashAttribute(
                ResultMessages.success().add(ResultMessage.fromCode("i.td.td.0000")));
        return "redirect:/todo/list";
    }

    @PostMapping("finish")
    public String finish(@Validated({Default.class, TodoFinish.class}) TodoForm form,
            BindingResult bindingResult, Model model, RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return list(model);
        }

        try {
            todoService.finish(form.getTodoId());
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return list(model);
        }

        attributes.addFlashAttribute(
                ResultMessages.success().add(ResultMessage.fromCode("i.td.td.0001")));
        return "redirect:/todo/list";
    }


    @PostMapping("delete")
    public String delete(@Validated({Default.class, TodoDelete.class}) TodoForm form,
            BindingResult bindingResult, Model model, RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return list(model);
        }

        try {
            todoService.delete(form.getTodoId());
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return list(model);
        }

        attributes.addFlashAttribute(
                ResultMessages.success().add(ResultMessage.fromCode("i.td.td.0002")));
        return "redirect:/todo/list";
    }
}
