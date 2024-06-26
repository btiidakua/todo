# todoサンプル拡張課題
## メッセージ管理
### 概要
メッセージとは、画面等に表示したりログに出力したりする文言のことを指します。  
一般的にメッセージはプロジェクトごとに定められたID体系に従って管理されます。  
メッセージを1つの（あるいは複数の）ファイルに集約して管理することで、メッセージの重複や表記ゆれを防いだり、変更する際の工数削減・変更漏れに有効だったりします。

### 演習
メッセージをプロパティで管理するようにしよう！

### 画面イメージ
- ページタイトルとTODO作成時のメッセージをプロパティから取得した結果
  ![画面イメージ1](./pic1.PNG "画面イメージ1")

- TODO作成数が上限を超えた際のエラーメッセージをプロパティから取得した結果
  ![画面イメージ2](./pic2.PNG "画面イメージ2")

### 事前準備
1. `ApplicationContextConfig.java`内で`MessageSource`がBean定義されていることを確認する
> [!IMPORTANT]  
> ガイドラインの「4.7.2.1.1. プロパティを使用する際の設定」はXMLで書かれているが、ここで確認したBean定義が同等のものとなる

2. `application-messages.properties`に以下を追加する
   ```properties
   # title
   title.todo.list=Todoリスト
   title.search.search=Todo検索

   # label
   label.td.todo.todoTitle=タイトル:
   label.td.todo.startDate=開始日:
   label.td.todo.limitDate=期限:
   label.td.search.todoTitle=タイトル:
   label.td.search.todoTitle.annotation=(部分一致検索)
   label.td.search.date=開始日~期限:
   label.td.search.finished=完了タスクも含む

   # message
   i.td.sc.0000=TODOの作成に成功しました.
   i.td.sc.0001=TODOの完了に成功しました.
   i.td.sc.0002=TODOの削除に成功しました.
   e.td.sc.8000=未完了のTODOが最大数の{0}件を超えています.
   e.td.sc.8001=開始日が期限より後になっています.
   e.td.sc.8002=このTODOはすでに完了しています. (id={0})
   e.td.sc.8003=対象のTODOが見つかりませんでした. (id={0})
   ```

> [!IMPORTANT]  
> メッセージのID体系はガイドラインにあるフォーマットとは異なるが、研修の特性上以下のフォーマットとする  
> `(メッセージタイプ).(プロジェクト区分).(メッセージ区分).(エラーレベル)(連番)`
> |項目|位置|内容|
> |:---|:---|:---|
> |メッセージタイプ|1桁目 (1桁)|info : i<br>warn : w<br>error : e|
> |プロジェクト区分|3-4桁目 (2桁)|TODO : td|
> |メッセージ区分|6-7桁目 (2桁)|画面出力用メッセージ : sc<br>ログ出力用メッセージ : lg|
> |エラーレベル|9桁 (1桁)|0-1 : 正常メッセージ<br>2-4 : 業務エラー（準正常）<br>5-7 : 入力チェックエラー<br>8 : 業務エラー（エラー）<br>9 : システムエラー|
> |連番|10-12桁目 (3桁)|連番で利用する(000-999)|

### 進め方
1. プロパティファイルに設定したタイトル、ラベルが画面に表示されるように修正する
2. プロパティファイルに設定した結果メッセージが画面に表示されるように修正する

### ポイント
- タイトルやラベルはプロジェクトの方針次第ではHTMLに直接記載することもありますが、今回はすべてプロパティファイルで管理する方針としています

### 参考
- [TERASOLUNAガイドライン - 4.7. メッセージ管理](https://terasolunaorg.github.io/guideline/current/ja/ArchitectureInDetail/WebApplicationDetail/MessageManagement.html)
