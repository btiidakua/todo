# todoサンプル拡張課題
## データベースアクセス
### 概要
本研修ではデータベースアクセスにMyBatis3というO/R Mapperを利用します。  
O/R MapperとはJavaオブジェクトとして保持しているデータとデータベース上に持っているデータとの相互変換を行う機能やソフトウェアを指します。

MyBatis3を使用したデータベースアクセスの実装はTODO基本課題で実施済みなので、おさらいや捕捉として実装例を確認しましょう。

最初にRepositoryインタフェースを作成します。  
ここではJavaオブジェクトとしてO/R Mapperに渡す引数や戻り値を定義します。
```java
public interface TodoRepository {

     Todo findById(String todoId);

     long countByFinished(boolean finished);

     void create(Todo todo);
}
```

次に、マッピングファイルを作成します。  
ここではJavaオブジェクトからSQL文へのマッピングを定義します。
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- RepositoryインタフェースのFQCNを指定し、マッピングファイル自体もsrc/main/resources配下の同階層に配置する  -->
<mapper namespace="com.example.domain.repository.todo.TodoRepository">
  <!-- Javaオブジェクトのフィールド名とデータベース上のカラム名をマッピングする -->
  <!-- typeはマッピングしたいBean名に紐づく -->
  <!-- resultMapの説明のため定義しているが、スネークケースとキャメルケースの違いだけであればMyBatis3の設定変更だけで対応可能 -->
  <resultMap id="todoResultMap" type="Todo">
      <id property="todoId" column="todo_id" />
      <result property="todoTitle" column="todo_title" />
      <result property="finished" column="finished" />
      <result property="createdAt" column="created_at" />
  </resultMap>

  <!-- idはRepositoryインタフェースのメソッド名に紐づく -->
  <!-- parameterTypeはRepositoryインタフェースのメソッド引数の型に紐づく -->
  <!-- SQLの取得結果をJavaオブジェクトへマッピングするのに上で定義したtodoResultMapを指定している -->
  <select id="findById" parameterType="String" resultMap="todoResultMap">
  <!-- XMLでは「<」や「>」はタグを表現するのに使う特殊文字として扱う -->
  <!-- SQL内で特殊文字ではなく不等号として「<」や「>」を使いたい場合にCDATAで囲む必要がある -->
  <!-- CDATAの説明のため記載しているが、本SQLは不等号を使用していないため、本来は不要 -->
  <![CDATA[
      SELECT
          todo_id,
          todo_title,
          finished,
          created_at
      FROM
          todo
      WHERE
          todo_id = #{todoId}
  ]]>
  </select>

  <!-- parameterTypeやresultTypeにプリミティブ型を指定する場合、先頭にアンダースコアをつける必要がある -->
  <select id="countByFinished" parameterType="_boolean" resultType="_long">
      SELECT
          COUNT(*)
      FROM
          todo
      WHERE
          finished = #{finished}
  </select>

  <!-- Repositoryインタフェースで戻り値なし(void)としているため、resultTypeやresultMapの指定は不要 -->
  <insert id="create" parameterType="Todo">
      INSERT INTO todo
      (
          todo_id,
          todo_title,
          finished,
          created_at
      )
      VALUES
      (
          #{todoId},
          #{todoTitle},
          #{finished},
          #{createdAt}
      )
  </insert>
</mapper>
```

### 演習
TODOの検索画面を作成しよう！

### 画面イメージ
- 検索用のデータ登録サンプル

  ![画面イメージ1](./pic1.PNG "画面イメージ1")
- 検索ページの初期表示

  ![画面イメージ2](./pic2.PNG "画面イメージ2")
- タイトル「研修」で検索した結果

  ![画面イメージ3](./pic3.PNG "画面イメージ3")
- タイトル「研修」、「完了タスクも含む」で検索した結果

  ![画面イメージ4](./pic4.PNG "画面イメージ4")
- タイトル「%」で検索した結果

  ![画面イメージ5](./pic5.PNG "画面イメージ5")
- 開始日「2024/07/01」、期限「2024/08/30」で検索した結果

  ![画面イメージ6](./pic6.PNG "画面イメージ6")
- 開始日「2024/07/01」、期限「2024/08/31」で検索した結果

  ![画面イメージ7](./pic7.PNG "画面イメージ7")

### 事前準備
1. `com.example.todo.domain.repository.todo`パッケージに`TodoCriteria.java`を作成する
   ```java
   package com.example.todo.domain.repository.todo;

   import java.io.Serializable;
   import java.time.LocalDate;

   public class TodoCriteria implements Serializable {

       private static final long serialVersionUID = 1L;

       private String todoTitle;

       private boolean finished;

       private LocalDate startDate;

       private LocalDate limitDate;

       // Getter/Setterは省略
   }
   ```
2. `TodoRepository.java`にメソッドを追加する
   ```java
   public interface TodoRepository {

       ...

       Collection<Todo> findByCriteria(TodoCriteria criteria);
   }
   ```

3. `TodoService.java`と`TodoServiceImpl.java`にメソッドを追加する
   ```java
   public interface TodoService {

       ...

       Collection<Todo> findByCriteria(TodoCriteria criteria);
   }
   ```

   ```java
   public class TodoServiceImpl implements TodoService {

       ...

       @Override
       public Collection<Todo> findByCriteria(TodoCriteria criteria) {
           return todoRepository.findByCriteria(criteria);
       }
   }
   ```

4. `com.example.todo.app.search`パッケージに`SearchForm.java`を作成する
   ```java
   package com.example.todo.app.search;

   import java.io.Serializable;
   import java.time.LocalDate;
   import org.springframework.format.annotation.DateTimeFormat;
   import jakarta.validation.constraints.Size;

   public class SearchForm implements Serializable {

       private static final long serialVersionUID = 1L;

       @Size(max = 30)
       private String todoTitle;

       private boolean finished;

       @DateTimeFormat(pattern = "uuuu/MM/dd")
       private LocalDate startDate;

       @DateTimeFormat(pattern = "uuuu/MM/dd")
       private LocalDate limitDate;

       // Getter/Setterは省略
   }
   ```

5. `com.example.todo.app.search`パッケージに`SearchMapper.java`を作成する
   ```java
   package com.example.todo.app.search;

   import org.mapstruct.Mapper;
   import com.example.todo.domain.repository.todo.TodoCriteria;

   @Mapper
   public interface SearchMapper {

       TodoCriteria map(SearchForm form);
   }
   ```

6. `com.example.todo.app.search`パッケージに`SearchController.java`を作成する
   ```java
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
   ```

7. `views/common/layout`フォルダに`header.html`を作成する
   ```html
   <!DOCTYPE html>
   <html xmlns:th="http://www.thymeleaf.org" th:fragment="header">
   <head>
   <title>header</title>
   </head>
   <body>
       <div th:fragment="header" th:remove="tag">
           <ul>
               <li><a th:href="@{/todo/list}">TODO登録・更新・削除</a></li>
               <li><a th:href="@{/search}">TOOD検索</a></li>
           </ul>
       </div>
   </body>
   </html>
   ```

8. `list.html`に`header.html`のフラグメント呼び出しを追加する
   ```html
   <body>
       <div id="header" th:replace="~{common/layout/header :: header}"></div>
       <h1>Todo List</h1>
       ...
   </body>
   ```

9. `views/search`フォルダに`search.html`を作成する
   ```html
   <!DOCTYPE html>
   <html xmlns:th="http://www.thymeleaf.org">

   <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
       <title>Todo Search</title>
       <link rel="stylesheet" href="../../../resources/app/css/styles.css" th:href="@{/resources/app/css/styles.css}">
   </head>

   <body>
       <div id="header" th:replace="~{common/layout/header :: header}"></div>
       <h1>Todo Search</h1>
       <div id="searchForm">
           <div th:if="${resultMessages} != null" class="alert alert-success"
               th:class="|alert alert-${resultMessages.type}|">
               <ul>
                   <li th:each="message : ${resultMessages}" th:text="${message.text}"></li>
               </ul>
           </div>
           <form action="/search" th:action="@{/search}" method="post" th:object="${searchForm}">
               <label>タイトル:</label>
               <input type="text" th:field="*{todoTitle}" />
               <span>(部分一致検索)</span>
               <br>
               <label>開始日~期限:</label>
               <input type="text" th:field="*{startDate}" />
               <span>~</span>
               <input type="text" th:field="*{limitDate}" />
               <br>
               <label>完了タスクも含む</label>
               <input type="checkbox" th:field="*{finished}">
               <br>
               <span id="todoTitle.errors" th:errors="*{todoTitle}" class="text-error"></span>
               <span id="startDate.errors" th:errors="*{startDate}" class="text-error"></span>
               <span id="limitDate.errors" th:errors="*{limitDate}" class="text-error"></span>
               <button>Search Todo</button>
           </form>
       </div>
       <hr />
       <div id="todoList">
           <ul th:remove="all-but-first">
               <li th:each="todo : ${todos}">
                   <span th:class="${todo.finished} ? 'strike'" th:text="${todo.todoTitle}"></span>
                   <span th:text="${#temporals.format(todo.startDate, 'uuuu/MM/dd')}"></span>
                   <span>~</span>
                   <span th:text="${#temporals.format(todo.limitDate, 'uuuu/MM/dd')}"></span>
               </li>
           </ul>
       </div>
   </body>

   </html>
   ```

### 進め方
1. 以下の条件で検索できるように`TodoRepository.xmlに`SQLを追加する
   - タイトルは入力された場合、部分一致検索
   - 開始日は入力された場合、開始日が入力値以降のデータがヒット
   - 期限は入力された場合、期限が入力値以前のデータがヒット
   - 完了タスクも含むにチェックが入っていない場合、finishedがfalseのデータのみヒット

### ポイント
- 実装が完了した後、画面イメージに従って動作確認をしてみてください
- 進め方のところで「○○は入力された場合」と書いているように、入力値によってWHERE句を動的に変化させる必要があります。  
MyBatis3に動的SQLを組み立てる仕組みがあるので使ってみましょう。
- 部分一致検索にはLIKE式を使います  
  LIKE式において`%`は任意の文字列を意味するワイルドカード文字として扱われるため、そのまま`WHERE todo_title LIKE %`で検索するとすべての値がヒットしてしまいます  
  このような特殊な扱いの文字を通常の文字として扱うことをエスケープと呼びます

### 参考
- [TERASOLUNAガイドライン - 6.1. データベースアクセス（共通編）](https://terasolunaorg.github.io/guideline/current/ja/ArchitectureInDetail/DataAccessDetail/DataAccessCommon.html)
- [TERASOLUNAガイドライン - 6.2. データベースアクセス（MyBatis3編）](https://terasolunaorg.github.io/guideline/current/ja/ArchitectureInDetail/DataAccessDetail/DataAccessMyBatis3.html)
- [PostgreSQL 9.0.4文書 - 9.7. パターンマッチ](https://www.postgresql.jp/docs/9.0/functions-matching.html)
- [XMLのCDATAとは](https://qiita.com/kurorr/items/cc2d21546fc1970015d7)
