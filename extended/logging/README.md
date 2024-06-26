# todoサンプル拡張課題
## ロギング
### 概要
アプリケーションを動かすと予期せぬ挙動やエラーが発生することが多々あります。  
本研修のような環境であれば原因を探るためにもう一度操作することで再現が出来るかもしれませんが、実際にシステムを運用すると操作するのはインターネットの先にいるエンドユーザなので再現しようにも確認のしようがありません。  
そんな状況下でもバグの解析をするため、ユーザの操作内容やエラーの詳細などをログに出力する必要があります。

ログの出力内容はプロジェクトごとのログ設計により異なりますが、多くの場合以下の情報は必要となります。
- 日時
- ログレベル
- ログを出力したクラス名
- メッセージ本文
- 各種トレース情報  
(※実際のシステム運用では複数ユーザ、複数トランザクションによる操作が同時に行われるため、複数操作によるログが混ざって出力される。  
その中で1人のユーザによる操作を追跡するためにリクエストIDやセッションIDなどを出力することでトレーサビリティが向上する。)

ログレベルについてもロギングライブラリの種類やプロジェクトごとのログ設計により異なりますが、一例として以下のように使い分けます。  

|ログレベル|出力目的|出力内容|
|:---|:---|:---|
|TRACE|処理時間測定|実行された処理名<br>処理の開始・終了時間|
|DEBUG|任意|任意<br>(処理途中の変数の状態が見たいなど)|
|INFO|アクセス証跡|いつ、どのユーザが、何の処理を実行したか|
|WARN|業務エラー監視|システムが正常に動作する中で発生しうるエラーの情報|
|ERROR|システムエラー監視|すぐにSE対応が必要なエラーの情報|

※TRACE、DEBUGレベルについては本番運用後は不要な情報であり、ログに余計な情報が出力されることで必要な情報が探しにくくなるため、本番運用では出力しないことが多い

ログの出力方法は多数ありますが、本研修ではSLF4JとLogbackというロギングライブラリを利用したログ出力について取り扱います。

### 演習
ログを出力しよう！

### ログ出力例
- TODO画面の初期表示成功時
  ```log
  date:2024-06-21 15:46:20	thread:http-nio-8080-exec-44	X-Track:043adba7f592472e875ee1438f4af2b0	level:INFO 	logger:com.example.todo.app.todo.TodoController        	message:[処理開始] メソッド名:list
  date:2024-06-21 15:46:20	thread:http-nio-8080-exec-44	X-Track:043adba7f592472e875ee1438f4af2b0	level:INFO 	logger:com.example.todo.app.todo.TodoController        	message:[処理正常終了] メソッド名:list
  ```

- TODO作成数上限エラーの発生時
  ```log
  date:2024-06-22 22:09:01	thread:http-nio-8080-exec-43	X-Track:dc59a0efb6aa4ef5923f7b6c924a0f48	level:ERROR	logger:com.example.todo.app.todo.TodoController        	message:[処理異常終了] メソッド名:create
  ```

### 事前準備
1. `logback.xml`に任意のログ出力ディレクトリを設定する
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE configuration>
   <configuration>

       <property name="app.log.dir" value="C:/logs" />

       ...
   ```

> [!IMPORTANT]  
> 本来ログ出力ディレクトリはOSの違いなど環境差分を含むことが多いため、OSの環境変数で設定するか、環境ごとに`logback.xml`を複数作成することが多いが本研修では簡易的に設定する

2. `application-messages.properties`にログ出力用メッセージを追加する
   ```properties
   # message
   ...

   i.td.lg.0000=[処理開始] メソッド名:{0}
   i.td.lg.0001=[処理正常終了] メソッド名:{0}
   e.td.lg.8000=[処理異常終了] メソッド名:{0}
   ```

3. `TodoController.java`と`SearchController.java`にSLF4Jのロガーとメッセージソースを追加する
   ```java
   @Controller
   @RequestMapping("todo")
   public class TodoController {

       private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

       @Inject
       MessageSource messageSource;

       ...
   }
   ```

4. [StackTraceSample.java](StackTraceSample.java)をそのまま`com.example.todo.domain.service.todo`パッケージに配置する

5. `TodoServiceImpl.java`のfindByCriteriaメソッドに`StackTraceSample.java`の呼び出しを追加する
   ```java
   @Override
   public Collection<Todo> findByCriteria(TodoCriteria criteria) {
       try {
           StackTraceSample.execute();
       } catch (Exception e) {
           ResultMessages messages = ResultMessages.error();
           messages.add(ResultMessage.fromText("StackTraceSampleException"));
           throw new BusinessException(messages, e);
       }

       return todoRepository.findByCriteria(criteria);
   }
   ```

### 進め方
1. `TodoController.java`と`SearchController.java`の各ハンドラメソッドの開始・終了時にINFOレベルでログ出力処理を追加する
2. `TodoController.java`と`SearchController.java`の各ハンドラメソッドで例外を補足した時、ERRORレベルでログ出力処理を追加する
3. わざとTODO検索時に例外が発生するようにしているので、ログファイルに出力されたスタックトレースを見てバグの原因を特定・修正する

### ポイント
- プロパティファイルに設定したメッセージの取得には`MessageSource.java`のgetMessageメソッドを利用してください
- ログ出力処理はControllerに追加する想定ですが、本来はAOPとして実装することが推奨されています  
  AOPについては参考のリンク先を読んだり、自分で調べてみたりしてください

### 参考
- [TERASOLUNAガイドライン - 7.1. ロギング](https://terasolunaorg.github.io/guideline/current/ja/ArchitectureInDetail/GeneralFuncDetail/Logging.html)
- [Spring BootでのAOPを利用したログ出力](https://qiita.com/be834194/items/c9c5b7084baade390abc)
- [Javaのエラー解決の王道～スタックトレースの読み方～](https://ittoybox.com/archives/588)
