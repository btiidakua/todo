# todoサンプル拡張課題
## 日付操作
### 概要
TODOに開始日、期限の項目を追加しよう！

### 画面イメージ
- 入力Formに開始日、期限を追加
  ![画面イメージ1](./pic1.PNG "画面イメージ1")
- TODOの一覧にも開始日、期限を追加
  ![画面イメージ2](./pic2.PNG "画面イメージ2")
- 開始日が期限より後になっている場合はエラーメッセージを表示
  ![画面イメージ3](./pic4.PNG "画面イメージ3")

### 事前準備
1. `TodoForm.java`に項目、Getter/Setterを追加する
   ```java
   public class TodoForm implements Serializable {
       ...

       private String startDate;

       private LocalDate limitDate;

       ...

       public String getStartDate() {
           return startDate;
       }

       public void setStartDate(String startDate) {
           this.startDate = startDate;
       }

       public LocalDate getLimitDate() {
           return limitDate;
       }

       public void setLimitDate(LocalDate limitDate) {
           this.limitDate = limitDate;
       }
   }
   ```

> [!IMPORTANT]  
> 本来日付項目に`String`型を用いるか、`LocalDate`型を用いるかは統一するべきだが、今回は研修の都合上両方を採用している

2. `Todo.java`に項目、Getter/Setterを追加する
   ```java
   public class Todo implements Serializable {
       ...

       private LocalDate startDate;

       private LocalDate limitDate;

       ...

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
   ```

3. 開始日はBeanマッピングの対象外として手動で値の詰め替えを行うため、`TodoMapper.java`にアノテーションを追加する
   ```java
   @Mapper
   public interface TodoMapper {
       @Mapping(target = "createdAt", ignore = true)
       @Mapping(target = "finished", ignore = true)
       @Mapping(target = "startDate", ignore = true)
       Todo map(TodoForm form);
   }
   ```

4. `list.html`に入力項目、出力項目を追加する
   - 入力項目
   ```html
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

   - 出力項目
   ```html
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
> ![カレンダー](./pic3.PNG "カレンダー")

### 進め方
1. 追加した項目をSQLに反映する
2. `TodoController.java`のcreateメソッド内で`TodoForm.java`から`Todo.java`へ開始日を詰め替える処理を追加する
3. `TodoServiceImpl.java`のcreateメソッド内で開始日と期限を比較し、開始日＞期限となる場合は以下のメッセージを設定した`BusinessException.java`をスローする

   > 開始日が期限より後になっています.
4. 一覧の日付が`uuuu-MM-dd`形式になっているので、`uuuu/MM/dd`で表示するように`list.html`を修正する

### ポイント
- `TodoForm.java`から`Todo.java`への開始日の詰め替えは型が異なるため型変換が必要になります
- 開始日と期限の比較は以下となるように注意してください
  - 開始日＞期限：エラー
  - 開始日＝期限：OK
  - 開始日＜期限：OK
- 一覧の日付フォーマットの変更にはThymeleafのダイアレクトという機能を利用してください
- 日付のフォーマットは`uuuu/MM/dd`としてください  
  余裕のある人は`yyyy/MM/dd`との違いも調べてみてください

### 参考
- https://terasolunaorg.github.io/guideline/current/ja/ArchitectureInDetail/GeneralFuncDetail/DateAndTime.html