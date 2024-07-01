package com.example.todo.domain.service.todo;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.exception.SystemException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import com.example.todo.domain.model.Todo;
import com.example.todo.domain.repository.todo.TodoCriteria;
import com.example.todo.domain.repository.todo.TodoRepository;
import jakarta.inject.Inject;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {

    private static final long MAX_UNFINISHED_COUNT = 5;

    @Inject
    TodoRepository todoRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo create(Todo todo) {
        long unfinishedCount = todoRepository.countByFinished(false);
        if (unfinishedCount >= MAX_UNFINISHED_COUNT) {
            ResultMessages messages = ResultMessages.error();
            messages.add(
                    ResultMessage.fromCode("e.td.sc.8000", MAX_UNFINISHED_COUNT));
            throw new BusinessException(messages);
        }

        if (todo.getLimitDate().isBefore(todo.getStartDate())) {
            ResultMessages messages = ResultMessages.error();
            messages.add(ResultMessage.fromCode("e.td.sc.8001"));
            throw new BusinessException(messages);
        }
        String todoId = UUID.randomUUID().toString();
        Date createdAt = new Date();

        todo.setTodoId(todoId);
        todo.setCreatedAt(createdAt);
        todo.setFinished(false);

        todoRepository.create(todo);

        return todo;
    }

    @Override
    public Todo finish(String todoId) {
        Todo todo = findOne(todoId);
        if (todo.isFinished()) {
            ResultMessages messages = ResultMessages.error();
            messages.add(ResultMessage.fromCode("e.td.sc.8002", todoId));
            throw new BusinessException(messages);
        }
        todo.setFinished(true);
        todoRepository.update(todo);
        return todo;
    }

    @Override
    public void delete(String todoId) {
        Todo todo = findOne(todoId);
        todoRepository.delete(todo);
    }

    private Todo findOne(String todoId) {
        return todoRepository.findById(todoId).orElseThrow(() -> {
            ResultMessages messages = ResultMessages.error();
            messages.add(ResultMessage.fromCode("e.td.sc.8003", todoId));
            return new ResourceNotFoundException(messages);
        });
    }

    @Override
    public Collection<Todo> findByCriteria(TodoCriteria criteria) {
        try {
            // StackTraceSample.execute();
        } catch (Exception e) {
            ResultMessages messages = ResultMessages.error();
            messages.add(ResultMessage.fromText("StackTraceSampleException"));
            throw new BusinessException(messages, e);
        }

        Collection<Todo> result = findByCriteria(criteria);
        if (result.size() == 0) {
            throw new BusinessException("hoge");
        }
        return todoRepository.findByCriteria(criteria);
    }

    @Override
    public void finishOptimistic(String todoId) {
        Todo todo = todoRepository.findByIdForOptimistic(todoId);
        todo.setFinished(true);
        // ロック確認のため5秒停止
        try {
            Thread.sleep(5000);
        } catch(InterruptedException e) {
            throw new SystemException("e.xx.fw.9001", e);
        }
        if(!todoRepository.updateForOptimistic(todo)) {
            throw new OptimisticLockingFailureException("楽観ロックエラー");
        }
    }

    @Override
    public void deletePessimistic(String todoId) {
        Todo todo = todoRepository.findByIdForPessimistic(todoId);
        // ロック確認のため5秒停止
        try {
            Thread.sleep(5000);
        } catch(InterruptedException e) {
            throw new SystemException("e.xx.fw.9001", e);
        }
        todoRepository.delete(todo);
    }
}
