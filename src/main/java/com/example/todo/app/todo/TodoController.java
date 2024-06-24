package com.example.todo.app.todo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Collection;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
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
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;
import com.example.todo.app.todo.TodoForm.TodoCreate;
import com.example.todo.app.todo.TodoForm.TodoDelete;
import com.example.todo.app.todo.TodoForm.TodoFinish;
import com.example.todo.domain.model.Todo;
import com.example.todo.domain.service.todo.TodoService;
import jakarta.inject.Inject;
import jakarta.validation.groups.Default;

@Controller
@RequestMapping("todo")
@TransactionTokenCheck
public class TodoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    @Inject
    TodoService todoService;

    @Inject
    TodoMapper beanMapper;

    @Inject
    MessageSource messageSource;

    @ModelAttribute
    public TodoForm setUpForm() {
        TodoForm form = new TodoForm();
        return form;
    }

    @GetMapping("list")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String list(Model model) {
        LOGGER.info(
                messageSource.getMessage("i.td.lg.0000", new Object[] {"list"}, Locale.JAPANESE));
        Collection<Todo> todos = todoService.findAll();
        model.addAttribute("todos", todos);
        LOGGER.info(
                messageSource.getMessage("i.td.lg.0001", new Object[] {"list"}, Locale.JAPANESE));
        return "todo/list";
    }

    @PostMapping("create")
    @TransactionTokenCheck(type = TransactionTokenType.IN)
    public String create(@Validated({Default.class, TodoCreate.class}) TodoForm todoForm,
            BindingResult bindingResult, Model model, RedirectAttributes attributes) {

        LOGGER.info(
                messageSource.getMessage("i.td.lg.0000", new Object[] {"create"}, Locale.JAPANESE));

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
            LOGGER.error(messageSource.getMessage("e.td.lg.8000", new Object[] {"create"},
                    Locale.JAPANESE));
            return list(model);
        }

        attributes.addFlashAttribute(
                ResultMessages.success().add(ResultMessage.fromCode("i.td.sc.0000")));
        LOGGER.info(
                messageSource.getMessage("i.td.lg.0001", new Object[] {"create"}, Locale.JAPANESE));
        return "redirect:/todo/list";
    }

    @PostMapping("finish")
    @TransactionTokenCheck(type = TransactionTokenType.IN)
    public String finish(@Validated({Default.class, TodoFinish.class}) TodoForm form,
            BindingResult bindingResult, Model model, RedirectAttributes attributes) {

        LOGGER.info(
                messageSource.getMessage("i.td.lg.0000", new Object[] {"finish"}, Locale.JAPANESE));

        if (bindingResult.hasErrors()) {
            return list(model);
        }

        try {
            todoService.finish(form.getTodoId());
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            LOGGER.error(messageSource.getMessage("e.td.lg.8000", new Object[] {"finish"},
                    Locale.JAPANESE));
            return list(model);
        }

        attributes.addFlashAttribute(
                ResultMessages.success().add(ResultMessage.fromCode("i.td.sc.0001")));
        LOGGER.info(
                messageSource.getMessage("i.td.lg.0001", new Object[] {"finish"}, Locale.JAPANESE));
        return "redirect:/todo/list";
    }


    @PostMapping("delete")
    @TransactionTokenCheck(type = TransactionTokenType.IN)
    public String delete(@Validated({Default.class, TodoDelete.class}) TodoForm form,
            BindingResult bindingResult, Model model, RedirectAttributes attributes) {

        LOGGER.info(
                messageSource.getMessage("i.td.lg.0000", new Object[] {"delete"}, Locale.JAPANESE));

        if (bindingResult.hasErrors()) {
            return list(model);
        }

        try {
            todoService.delete(form.getTodoId());
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            LOGGER.error(messageSource.getMessage("e.td.lg.8000", new Object[] {"delete"},
                    Locale.JAPANESE));
            return list(model);
        }

        attributes.addFlashAttribute(
                ResultMessages.success().add(ResultMessage.fromCode("i.td.sc.0002")));

        LOGGER.info(
                messageSource.getMessage("i.td.lg.0001", new Object[] {"delete"}, Locale.JAPANESE));

        return "redirect:/todo/list";
    }

    @PostMapping("finishOptimistic")
    @TransactionTokenCheck(type = TransactionTokenType.IN)
    public String finishOptimistic(TodoForm form, Model model, RedirectAttributes attributes) {
        try {
            todoService.finishOptimistic(form.getTodoId());
        }  catch (OptimisticLockingFailureException e) {
            model.addAttribute(ResultMessages.error()
                    .add(ResultMessage.fromCode("e.td.sc.8002", form.getTodoId())));
            return list(model);
        }
        attributes.addFlashAttribute(
                ResultMessages.success().add(ResultMessage.fromCode("i.td.sc.0001")));
        return "redirect:/todo/list";
    }

    @PostMapping("deletePessimistic")
    @TransactionTokenCheck(type = TransactionTokenType.IN)
    public String deletePessimistic(TodoForm form, Model model, RedirectAttributes attributes) {
        try {
            todoService.deletePessimistic(form.getTodoId());
        }  catch (PessimisticLockingFailureException e) {
            model.addAttribute(ResultMessages.error()
                    .add(ResultMessage.fromCode("e.td.sc.8004", form.getTodoId())));
            return list(model);
        }
        attributes.addFlashAttribute(
                ResultMessages.success().add(ResultMessage.fromCode("i.td.sc.0002")));
        return "redirect:/todo/list";
    }
}
