# inaba
CQRS/ES, DDDをベースに作られた販売サイトのマイクロサービスサンプルです。

## 参考
* [wiki](https://github.com/azarasi1226/inaba/wiki)
* [ドメインストーミング図](https://miro.com/app/board/uXjVM1s4A4A=/)
* [Terraform](https://github.com/azarasi1226/inaba-infrastructure)

<br>

## Getting Started
### 1. aquaのインストール
このプロジェクトではaquaというツール管理ソフトを導入しています。  
aquaのインストール方法は[公式ドキュメント - install](https://aquaproj.github.io/docs/install/)を参照してください。

<br>

### 2. aquaによるセットアップ
プロジェクトルートで以下のコマンドを実行し、aquaによるセットアップを行います。
```bash
aqua i
```

<br>

### 3. local環境の構築
以下のコマンドを実行し、local環境を構築します。
```bash
task local-up
```
<br>

### 4. DBの初回セットアップ
以下のコマンドを実行し、DBの初期セットアップを行います。
```bash
task atlas-migrate-apply
```

<br>

### 5. Cognitoの初期セットアップ
※ OIDCをサポートするIDプロバイダーだったらぶっちゃけCognitoじゃなくてもOK

すんません...時間なくてLocalで構築できなかった..
Cognitoで適当にアプリケーションクライアントつくってください....

<br>

### 6. サービス起動
以下の環境変数をセットし、`inaba-service`モジュールを起動します。

#### 通常のローカル環境
```
APPLICATION_NAME=inaba;
SERVER_PORT=8081;
GRPC_PORT=8091;
SPRING_DATASOURCE_DATABASE=inaba;
SPRING_DATASOURCE_HOST=localhost;
SPRING_DATASOURCE_PASSWORD=passw0rd;
SPRING_DATASOURCE_PORT=3306;
SPRING_DATASOURCE_USERNAME=root;
SPRING_PROFILES_ACTIVE=local;
```

#### AxonIQConsoleを使用する場合 (わからない場合はスキップ)
AxonIQってのはマイクロサービスの間の状態確認とかやってくるれる無料サービスのこと。  
登録もGithubアカウントあるとすぐできるので調べてみるといい感じかも
```
APPLICATION_NAME=inaba;
AXONIQ_CONSOLE_CREDENTIALS={自分で登録して作ったAXONIQの認証情報};
SERVER_PORT=8081;
GRPC_PORT=8091;
SPRING_DATASOURCE_DATABASE=inaba;
SPRING_DATASOURCE_HOST=localhost;
SPRING_DATASOURCE_PASSWORD=passw0rd;
SPRING_DATASOURCE_PORT=3306;
SPRING_DATASOURCE_USERNAME=root;
SPRING_PROFILES_ACTIVE=local-with-axoniq;
```

<br>

### 7. APIGatewayの起動
以下の環境変数をセットし、`inaba-apigateway`モジュールを起動します。

```
SPRING_PROFILES_ACTIVE=local;
SERVER_PORT=8082;
BACKEND_SERVER=dns:///localhost:8091;
OIDC_ISSUER={自分で作ったOIDCのISSUER_URL};
```

<br>

### 8. 起動したら下のリンクでチェック！
* [API Gateway Swagger](http://localhost:8082/swagger-ui/index.html)
* [AxonServer Console](http://localhost:8024/)

<br>

## DBマイグレーションについて
DBのマイグレーションには[Atlas](https://atlasgo.io/)を使用しています。  

### マイグレーションの手順
1. `./database/schema.mysql.sql`の中にDBのスキーマ情報が入っています(ただのCREATE文です)。これを理想の形に修正してください。
2. 以下のコマンドを実行し、マイグレーションファイルを生成します。
```bash
task atlas-migrate-diff
```
3. `./database/migrations`ディレクトリにマイグレーション用SQlが生成されたことを確認してください。
4. 以下のコマンドを実行し、DBにマイグレーションを適用します。
```bash
task atlas-migrate-apply
```