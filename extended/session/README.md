# todoサンプル拡張課題
## セッション管理
### 概要
本研修で取り扱うセッション管理とは、ざっくり言うと「サーバ内に情報を保存し、複数ページ間で共有する」仕組みのことを指します。  
本来、クライアントからサーバへの通信は1回のやり取りで完結しているため、情報を共有するために以下のような方法をとっています。

1. クライアントからサーバにリクエストがくると、サーバ側で`HttpSession`オブジェクトを生成する  
   この`HttpSession`オブジェクトはランダムに生成されたID(セッションID)を持つ
2. サーバからクライアントへのレスポンスにセッションIDを含む  
   クライアント側はブラウザのCookieでセッションIDを保持する
3. アプリケーション内でセッションにオブジェクトを格納すると、`HttpSession`オブジェクトに保持される
4. これ以降クライアントからのリクエストにはセッションIDが含まれる  
   アプリケーション内でセッションからオブジェクトを取得する際は、このセッションIDによって紐づける

TERASOLUNAではセッション管理の方法として、`@SessionAttributes`アノテーションの使用かSpring FrameworkのsessionスコープのBeanの使用のどちらかが推奨されています。

#### `@SessionAttributes`アノテーション
`@SessionAttributes`アノテーションによるセッション管理は、1つのControllerクラス内でデータを持ちまわる場合に使用します。  
セッションに格納したオブジェクトに対する各操作の方法は以下の通りです。

- セッションに格納するオブジェクトの指定

Controllerクラスに`@SessionAttributes`アノテーションを付与し、`types`で格納するオブジェクトのクラスを指定する
```java
@Controller
@RequestMapping("todo")
@SessionAttributes(types = { TodoForm.class, Todo.class })
public class TodoController {

}
```

- セッションにオブジェクトを格納

`@SessionAttributes`アノテーションで指定したオブジェクトを以下2種類の方法で格納する
  - `@ModelAttribute`アノテーションが付与されたメソッドにて、セッションに追加するオブジェクトを返却する  
  - `Model`オブジェクトのaddAttributeメソッドを使用して、セッションに格納するオブジェクトを追加する

```java
@Controller
@RequestMapping("todo")
@SessionAttributes(types = { TodoForm.class, Todo.class })
public class TodoController {

    @ModelAttribute(value = "todoForm")
    public TodoForm setUpTodoForm() {
        return new TodoForm();
    }

    @GetMapping("method1")
    public String method1(Model model) {
        Todo todo = new Todo;
        ...
        model.addAttribute(todo);
        ...
    }
}
```

- セッションに格納されているオブジェクトの取得

Controllerのハンドラメソッドの引数として取得する

```java
@Controller
@RequestMapping("todo")
@SessionAttributes(types = { TodoForm.class, Todo.class })
public class TodoController {

    @PostMapping("method2")
    public String method2(TodoForm todoForm, Todo todo) {
        ...
    }
}
```

- セッションに格納したオブジェクトの削除

`org.springframework.web.bind.support.SessionStatus`のsetCompleteメソッドを、Controllerのハンドラメソッドから呼び出す

```java
@Controller
@RequestMapping("todo")
@SessionAttributes(types = { TodoForm.class, Todo.class })
public class TodoController {

    @PostMapping("method3")
    public String method3(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        ...
    }
}
```

#### Spring FrameworkのsessionスコープのBean
Spring FrameworkのsessionスコープのBeanによるセッション管理は、複数のControllerをまたいだ画面遷移において、データを持ち回る場合に使用します。  
セッションに格納したオブジェクトに対する各操作の方法は以下の通りです。

- sessionスコープのBean定義

セッションに格納するBeanに`@Scope`アノテーションを付与し、`value`にsessionを指定する

```java
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionTodo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Todo todo;

    public Todo getTodo() {
        if (todo == null) {
            todo = new Todo();
        }
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }

    public void clearTodo() {
        todo = null;
    }
}
```

- sessionスコープのBeanの利用

sessionスコープのBeanを、ControllerにInjectして利用する

```java
@Controller
@RequestMapping("todo")
public class TodoController {

    @Inject
    SessionTodo sessionTodo;

    @GetMapping("method1")
    public String method1() {
        Todo todo = sessionTodo.getTodo();
        ...
    }

    @PostMapping("method2")
    public String method2() {
        Todo todo = new Todo();
        sessionTodo.setTodo(todo);
        ...
    }
}
```

- セッションに格納したオブジェクトの削除

sessionスコープのBeanに用意したクリア用のメソッドを呼び出す

```java
@Controller
@RequestMapping("todo")
public class TodoController {

    @Inject
    SessionTodo sessionTodo;

    @PostMapping("method3")
    public String method3() {
        ...
        sessionTodo.clearTodo();
        ...
    }
}
```

### 演習
検索条件をセッションに保持してみよう！

### 画面イメージ
- 検索条件のリセットボタンを追加

  ![画面イメージ1](./pic1.PNG "画面イメージ1")

### 事前準備
1. `SearchController.java`に以下のフィールドとメソッドを追加する
   ```java
   @Controller
   @RequestMapping("search")
   public class SearchController {

       ...

       // 検索条件がセッションに格納されているかを管理する
       private boolean searchFormFlg = false;

       ...

       @GetMapping("reset")
       public String reset() {
           return "search/search";
       }
   }
   ```

2. `search.html`に検索条件のリセットFormを追加する
   ```html
   <body>
       ...
       <div id="searchForm">
           ...
           <form action="/search" th:action="@{/search}" method="post" th:object="${searchForm}">
               ...
           </form>
           <form th:action="@{search/reset}" method="get">
               <button>Reset TodoCriteria</button>
           </form>
   ```

### 進め方
1. 以下の仕組みになるよう、`SearchController.java`を修正する
   - 検索画面の初期表示時、検索条件がセッションに格納されていれば検索を実施する
   - 検索時、検索条件をセッションに格納する
   - 検索条件のリセット時、セッションに格納された検索条件をクリアする

### ポイント
- 今回のケースではセッション管理の方法としてどちらを採用すべきか考えて実装してください

### 参考
- [TERASOLUNAガイドライン - 4.4. セッション管理](https://terasolunaorg.github.io/guideline/current/ja/ArchitectureInDetail/WebApplicationDetail/SessionManagement.html)
- [Qiita - WEBアプリケーションにおけるセッション管理（webセッションについて１）](https://qiita.com/Kosuke0906/items/45bdef6efae3797a2d2f)