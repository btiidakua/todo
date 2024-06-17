# todoサンプル拡張課題
## 入力チェック(単項目チェック)
### 概要
開始日、期限に入力チェックを追加しよう！

### 画面イメージ
- タイトルが未入力の場合のエラーメッセージ
  ![画面イメージ1](./pic1.PNG "画面イメージ1")
- 開始日が`uuuu/MM/dd`形式以外の場合のエラーメッセージ
  ![画面イメージ2](./pic2.PNG "画面イメージ2")
- 期限に日付以外の文字列が入力された場合のエラーメッセージ
  ![画面イメージ3](./pic3.PNG "画面イメージ3")

### 事前準備
1. `ApplicationContextConfig.java`のmessageSourceメソッドに文字コード`UTF-8`を指定する1行を追加する
   ```java
   @Configuration
   @EnableAspectJAutoProxy
   @Import({ TodoDomainConfig.class })
   public class ApplicationContextConfig {
      ...
      @Bean("messageSource")
      public MessageSource messageSource() {
          ResourceBundleMessageSource bean = new ResourceBundleMessageSource();
          bean.setBasenames("i18n/application-messages");
          bean.setDefaultEncoding("UTF-8");
      }
      ...
   }
   ```

2. `list.html`に開始日、期限のエラーメッセージ出力箇所を追加する
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
       <span id="startDate.errors" th:errors="${todoForm.startDate}" class="text-error"></span>
       <span id="limitDate.errors" th:errors="${todoForm.limitDate}" class="text-error"></span>
       <button>Create Todo</button>
   </form>
   ```

### 進め方
1. タイトルが1文字以上、30文字以内ではない場合のエラーメッセージを以下に変更する
   > タイトルは1文字以上、30文字以内で入力してください.
2. 開始日が「数字4桁/数字4桁/数字2桁」の形式ではない場合のバリデーションを追加し、メッセージは以下とする
   > 開始日の入力形式が不正です.
3. 期限が未入力の場合のバリデーションを追加し、メッセージは以下とする
   > 期限は必須項目です.
4. 期限に日付以外の文字列が入力された場合のバリデーションを追加し、メッセージは以下とする
   > 期限の入力形式が不正です.

### ポイント
- 「タイトル」、「開始日」などのフィールド名や「0」、「30」といったアノテーションの属性値をメッセージにべた書きしてしまうと汎用性が損なわれてしまうため避けましょう

### 参考
- https://terasolunaorg.github.io/guideline/current/ja/ArchitectureInDetail/WebApplicationDetail/Validation.html