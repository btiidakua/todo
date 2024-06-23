# todoサンプル拡張課題
## 排他制御
### 概要
楽観ロック、悲観ロックによる排他制御を行おう！

### 画面イメージ
- 2つのウインドウを開いて両方でFinishボタンを押下
  ![画面イメージ1](./pic1.PNG "画面イメージ1")

- 2つのウインドウを開いて両方でDeleteボタンを押下
  ![画面イメージ2](./pic2.PNG "画面イメージ2")

### 事前準備
1. `Todo.java`にフィールドを追加する
   ```java
   public class Todo implements Serializable {
       ...

       private long version;

       // Getter/Setterは省略
   }
   ```

2. `TodoRepository.java`にメソッドを3つ追加する
   ```java
   public interface TodoRepository {
       ...

       Todo findByIdForOptimistic(String todoId);

       boolean updateForOptimistic(Todo todo);

       Todo findByIdForPessimistic(String todoId);
   }
   ```

3. `TodoService.java`と`TodoServiceImpl.java`にメソッドを追加する
   ```java
   public interface TodoService {

       ...

       void finishOptimistic(String todoId);

       void deletePessimistic(String todoId);
   }
   ```

   ```java
   public class TodoServiceImpl implements TodoService {

       ...

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
   ```

4. `TodoMapper.java`にアノテーションを追加する
   ```java
   @Mapper
   public interface TodoMapper {

       ...
       @Mapping(target = "version", ignore = true)
       Todo map(TodoForm form);
   }
   ```

5. `application-messages.properties`にメッセージを追加する
   ```properties
   # message
   ...
   e.td.sc.8004=このTODOは他の操作によりロックされています. (id={0})
   ```

6. `TodoController.java`にメソッドを追加する
   ```java
   @Controller
   @RequestMapping("todo")
   public class TodoController {

       ...

       @PostMapping("finishOptimistic")
       public String finishOptimistic(TodoForm form, Model model, RedirectAttributes attributes) {
           try {
               todoService.finishOptimistic(form.getTodoId());
           } catch (OptimisticLockingFailureException e) {
               model.addAttribute(ResultMessages.error()
                       .add(ResultMessage.fromCode("e.td.sc.8002", form.getTodoId())));
               return list(model);
           }
           attributes.addFlashAttribute(
                   ResultMessages.success().add(ResultMessage.fromCode("i.td.sc.0001")));
           return "redirect:/todo/list";
       }

       @PostMapping("deletePessimistic")
       public String deletePessimistic(TodoForm form, Model model, RedirectAttributes attributes) {
           try {
               todoService.deletePessimistic(form.getTodoId());
           } catch (PessimisticLockingFailureException e) {
               model.addAttribute(ResultMessages.error()
                       .add(ResultMessage.fromCode("e.td.sc.8004", form.getTodoId())));
               return list(model);
           }
           attributes.addFlashAttribute(
                   ResultMessages.success().add(ResultMessage.fromCode("i.td.sc.0002")));
           return "redirect:/todo/list";
       }
   }
   ```

7. `list.html`のFinish、Deleteボタンの遷移先(th:action)を変更する
   ```html
   <form th:if="${!todo.finished}" action="/todo/finish" th:action="@{/todo/finishOptimistic}" method="post"
       class="inline">
       <input type="hidden" name="todoId" th:value="${todo.todoId}" />
       <button>Finish</button>
   </form>
   <form action="/todo/delete" th:action="@{/todo/finishOptimistic}" method="post" class="inline">
       <input type="hidden" name="todoId" th:value="${todo.todoId}" />
       <button>Delete</button>
   </form>
   ```

### 進め方
1. `TodoRepository.xml`に以下3つのSQLを追加する
   - `findByIdForOptimistic`：楽観ロック用にVersionカラムを含むデータを取得する
   - `updateForOptimistic`：データの競合が無ければfinishedカラムを更新する
   - `findByIdForPessimistic`：悲観ロックを取得する

### ポイント
- 楽観ロックと悲観ロックの最大の違いは競合を制御するタイミングです  
  楽観ロックはデータの読み込み時にはロックをかけず、書き込み時に競合をチェックします  
  対して、悲観ロックはデータの読み込み時にロックをかけ、書き込み時までそのロックを維持します

### 参考
- [TERASOLUNAガイドライン - 6.4. 排他制御](https://terasolunaorg.github.io/guideline/current/ja/ArchitectureInDetail/DataAccessDetail/ExclusionControl.html)