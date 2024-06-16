# todoサンプル拡張課題
## 日付操作
### 概要
TODOに開始日、期限の項目を追加しよう!

![画面イメージ1](./pic1.PNG "画面イメージ1")

![画面イメージ2](./pic2.PNG "画面イメージ2")

### 事前準備
1. `TodoMapper.java`にアノテーションを追加する
   ```java
   @Mapper
   public interface TodoMapper {
       @Mapping(target = "createdAt", ignore = true)
       @Mapping(target = "finished", ignore = true)
       @Mapping(target = "startDate", ignore = true)
       @Mapping(target = "limitDate", ignore = true)
       Todo map(TodoForm form);
   }
   ```

2. `list.html`に入力項目、出力項目を追加する
   ```html
    <!-- 入力項目 -->
    <form action="/todo/create" th:action="@{/todo/create}" method="post">
        <label>タイトル:</label>
        <input type="text" th:field="${todoForm.todoTitle}" />
        <label>開始日:</label>
        <input type="text" th:field="${todoForm.startDate}" />
        <label>期限:</label>
        <input type="text" th:field="${todoForm.limitDate}" />
        <span id="todoTitle.errors" th:errors="${todoForm.todoTitle}" class="text-error">size must be between 1 and 30</span>
        <button>Create Todo</button>
    </form>

   ```

   ```html
    <!-- 出力項目 -->
    <li th:each="todo : ${todos}">
        <span th:class="${todo.finished} ? 'strike'" th:text="${todo.todoTitle}">Send a e-mail</span>
        <span th:text="${todo.startDate}">2024/07/01</span>
        <span>~</span>
        <span th:text="${todo.limitDate}">2024/07/31</span>
        ...
    </li>
    ```
> [!IMPORTANT]  
> 日付の入力項目は`type="date"`とすることでカレンダーによる入力も出来るが、今回は研修の都合上`type="text"`としている
> 
> ![画面イメージ3](./pic3.PNG "画面イメージ3")

### 進め方
1. `Todo.java`にLocalDate型で項目を追加する
2. 追加した項目をSQLに反映する
3. `TodoForm.java`にString型で項目を追加する
4. `TodoController.java`のcreateメソッドに追加した項目の詰め替え処理を追加する

### ポイント
- 日付操作についてはガイドラインの以下ページを参照してください
  https://terasolunaorg.github.io/guideline/current/ja/ArchitectureInDetail/GeneralFuncDetail/DateAndTime.html
- 入力される日付のフォーマットは`uuuu/MM/dd`としてください  
  余裕のある人は`yyyy/MM/dd`との違いも調べてみてください
