# todoサンプル課題
## 基本課題
### 目的
- Controller、Service、Repositoryといった各部品、レイヤの役割を学ぶ
- 基本的なCRUD操作について一通り触れる

### 検討事項
- JSP or Thymeleaf?
  - 流石にJSPは古いかも
- H2 or Postgres?
  - 製造中はH2でも良いかもだけど、最終的にPostgresにするなら最初からPostgresでも良い気がする
- JavaConfig or XmlConfig?
  - 最新のガイドラインはちょっとBootチックにJavaConfig対応してたので、そっちのほうが今っぽいかも

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

## 拡張課題
### 目的
- ガイドラインの目次レベルで優先的に覚えてほしい機能を学ぶ
- ガイドラインやWEB検索による情報の探し方を身に着ける
### 検討事項
- 課題のお題目は何にするか?
  - 優先度高(一般的な業務で利用する機能)
    - 入力チェック
    - データベースアクセス（MyBatis3編）
    - 二重送信防止
    - メッセージ管理
    - ロギング
  - 優先度中(業務で利用する場合がある機能)
    - 例外ハンドリング
    - ページネーション
    - セッション管理
    - コードリスト
    - 排他制御
    - プロパティ管理
    - ボイラープレートコードの排除(Lombok)
  - 優先度低(AP基盤が触るような機能＋難しい機能など)
    - Beanマッピング(MapStruct)
    - 単体テスト
    - Ajax
    - XSS対策
    - 認証
    - 認可