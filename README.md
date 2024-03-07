# simplesave
【収入】【支出】【カテゴリ毎の比率】を一目で確認できる
シンプルな家計簿アプリです。<br />
- ダッシュボード画面で収支とカテゴリ毎の内訳を一目でわかりやすく表示<br />
- 毎年、毎月、毎週、毎日など、定期的な入力を自動化できる機能を搭載<br />
- レスポンシブ対応しているのでスマホから確認可能

[![Image from Gyazo](https://i.gyazo.com/79713d560df4d38b2edc97d00a360c88.gif)](https://gyazo.com/79713d560df4d38b2edc97d00a360c88)


## URL
[https://simplesave.onrender.com/](https://simplesave.onrender.com/)



<br />


## 制作背景
Javaのアプリ開発経験を積むために作成しました。

<br />

## 成果物作成期間
2024/1/6 ～ 2024/3/5 （150時間程度）

<br />

## 使用技術
- Java 21
- SpringBoot 3.2.3
  - Spring Security 6.2.2
  - Spring Data JPA 3.2.3
  - Spring Web 6.1.4
  - thymeleaf-spring6 3.1.2
- HTML, CSS
- bootstrap 4.6.2
- JavaScript
  - jQuery 3.5.1
  - Chart.js 4.4.2
  - datatables 2.0.0
- PostgreSQL 16


<br />

## ER図
![simplesave_db - public](https://github.com/nyakako/simple-save/assets/9846779/657414f6-5eb5-448d-8769-5a1e2834413a)

<br />

## 各テーブルについて
| テーブル名 | 説明 |
|:-:|:-:|
| users | 登録ユーザーの情報 |
| category | 出入金カテゴリの情報 |
| transaction | ユーザーの家計簿データ |
| recurring_transaction | ユーザーの定期自動入力用データ |

<br />


## 機能一覧
- 基本機能
  - 会員登録機能
  - ログイン機能
- 家計簿機能
  - 出入金記録・編集・削除
  - 履歴の一覧形式
  - 出入金カテゴリ毎のグラフ表示
  - カテゴリの追加、編集、削除
  - 定期自動入力の登録、編集、削除
  - 定期自動入力の一覧表示
  - 色設定（収入、支出の赤緑入れ替え）機能

<br />

## 画面遷移図
[https://www.figma.com/file/v9hY900lhhk84DrQYSFDWT/simplesave(kakeibo-app)?type=design&node-id=0%3A1&mode=design&t=1idWKRiKKiya0JeE-1](https://www.figma.com/file/v9hY900lhhk84DrQYSFDWT/simplesave(kakeibo-app)?type=design&node-id=0%3A1&mode=design&t=1idWKRiKKiya0JeE-1)

<br />
