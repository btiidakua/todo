# todoサンプル課題
## 基本課題
### 目的
- Controller、Service、Repositoryといった各部品、レイヤの役割を学ぶ
- 基本的なCRUD操作について一通り触れる

### 進め方
[TERASOLUNAガイドライン 11.2. チュートリアル(Todoアプリケーション Thymeleaf編)](https://terasolunaorg.github.io/guideline/current/ja/Tutorial/TutorialTodoThymeleaf.html)を参照して進めていく。  
環境による分岐や読み飛ばす章は以下の通り。
- 11.2.3.1. プロジェクトの作成  
  RepositoryImplの実装は「MyBatis3を使用してデータベースにアクセスするRepositoryImpl」を利用する。そのため以下の章は読み飛ばす。
  - 11.2.3.1.1. O/R Mapperに依存しないブランクプロジェクトの作成
  - 11.2.3.1.3. JPA用のブランクプロジェクトの作成

  また実行する`mvn archetype`コマンドはJava Configを選択すること。
- 11.2.5.1. ドメイン層の作成  
  データベースアクセスはMyBatis3を利用するため、以下の章は読み飛ばし、
  - 11.2.5.1.3. RepositoryImplの作成(インフラストラクチャ層)

  代わりに以下の章に読み替える。
  - 11.2.6.2.1. todo-infra.propertiesの修正
  - 11.2.6.3.2. TodoRepositoryImplの作成
  - 11.2.6.3.3. Mapperファイルの作成

  ただし、`todo-infra.properties`の設定値はガイドラインのままではなく、以下PostgreSQL用の値を利用すること。
  ```properties
  database=POSTGRESQL
  database.url=jdbc:postgresql://localhost:5432/(データベース名)
  database.username=postgres
  database.password=postgres
  database.driverClassName=org.postgresql.Driver
  cp.maxActive=96
  cp.maxIdle=16
  cp.minIdle=0
  cp.maxWait=60000
  ```

## 拡張課題
### 目的
- ガイドラインの目次レベルで優先的に覚えてほしい機能を学ぶ
- ガイドラインやWEB検索による情報の探し方を身に着ける

### 進め方
以下の課題を上から順番に実施する。
1. [日付操作](/extended/datetime/README.md)
2. [入力チェック(単項目チェック)](/extended/validation/README.md)
3. [データベースアクセス](/extended/dbaccess/README.md)
4. [メッセージ管理](/extended/message/README.md)
5. [ロギング](/extended/logging/README.md)
6. [排他制御](/extended/exclusioncontrol/README.md)
7. [セッション管理](/extended/session/README.md)
8. [二重送信防止](/extended/doublesubmit/README.md)
9. 例外ハンドリング

### 検討事項
- 課題のお題目は何にするか?
  - 優先度高(一般的な業務でよく利用する機能)
    - データベースアクセス（MyBatis3編）
    - 入力チェック
    - 日付操作(JSR-310 Date and Time API)
    - メッセージ管理
    - ロギング
    - 二重送信防止
    - 例外ハンドリング
    - セッション管理
    - 排他制御
  - 優先度中(業務で利用する場合がある機能)
    - REST
    - ページネーション
    - コードリスト
    - プロパティ管理
    - ボイラープレートコードの排除(Lombok)
    - Beanマッピング(MapStruct)
    - 国際化
    - 文字列処理
  - 優先度低(AP基盤が触るような機能＋難しい機能など)
    - ファイルアップロード
    - ファイルダウンロード
    - ヘルスチェック
    - E-mail送信(SMTP)
    - JMS(Jakarta Messaging)
    - Ajax
    - 認証
    - 認可
    - CSRF対策
    - XSS対策
    - 暗号化
    - OAuth 2.0
  - 対象外
    - 単体テスト ⇒ JUnitハンズオンで実施
