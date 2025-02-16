# inaba
CQRS/ES, DDDをベースに作られた販売サイトのマイクロサービスサンプルです。

## 参考
* [ドメインストーミング図](https://miro.com/app/board/uXjVM1s4A4A=/)
* [Terraform](https://github.com/azarasi1226/inaba-infrastructure)

<br>

## ローカル環境での開発
### 1. Dockerによる環境構築
docker-composeを使用してインフラ構築をします。
```bash
cd ./setup
docker compose up -d
```
<br>

### 2. Cognitoの初期セットアップ
※ OIDCをサポートするIDプロバイダーだったらぶっちゃけCognitoじゃなくてもOK

すんません...時間なくてLocalで構築できなかった..
Cognitoで適当にアプリケーションクライアントつくってください....

<br>

### 3. AxonServerの起動
* [AxonServer Console](http://localhost:8024/)にアクセスして、defualt contextを手動で作成します。

<br>

### 4. サービス起動
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

### 5. APIGatewayの起動
以下の環境変数をセットし、`inaba-apigateway`モジュールを起動します。

```
SPRING_PROFILES_ACTIVE=local;
SERVER_PORT=8082;
BACKEND_SERVER=dns:///localhost:8091;
OIDC_ISSUER={自分で作ったOIDCのISSUER_URL};
```

<br>

## 起動したら下のリンクでチェック！
* [API Gateway Swagger](http://localhost:8082/swagger-ui/index.html)
* [AxonServer Console](http://localhost:8024/)