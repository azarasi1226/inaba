# inaba

## Local環境構築手順
### 1. インフラ構築
docker-composeを使用してAxonServer・Databaseを起動します。
```bash
cd ./setup/docker
docker compose up -d
```
<br>

### 2. サービス起動
InteliJIDEのマルチプロジェクト機能で同時に起動します。

* [OrderService Swagger](http://localhost:8081/swagger-ui/index.html)
* [CatalogService Swagger](http://localhost:8082/swagger-ui/index.html)
* [BasketService Swagger](http://localhost:8083/swagger-ui/index.html)

#### OrderService環境変数
```
SERVER_PORT=8081;
SPRING_DATASOURCE_DATABASE=order;
SPRING_DATASOURCE_HOST=localhost;
SPRING_DATASOURCE_PASSWORD=passw0rd;
SPRING_DATASOURCE_PORT=3306;
SPRING_DATASOURCE_USERNAME=root;
SPRING_PROFILES_ACTIVE=local;
```

#### CatalogService環境変数
```
SERVER_PORT=8082;
SPRING_DATASOURCE_DATABASE=catalog;
SPRING_DATASOURCE_HOST=localhost;
SPRING_DATASOURCE_PASSWORD=passw0rd;
SPRING_DATASOURCE_PORT=3306;
SPRING_DATASOURCE_USERNAME=root;
SPRING_PROFILES_ACTIVE=local;
```

#### BasketService環境変数
```
SERVER_PORT=8083;
SPRING_DATASOURCE_DATABASE=basket;
SPRING_DATASOURCE_HOST=localhost;
SPRING_DATASOURCE_PASSWORD=passw0rd;
SPRING_DATASOURCE_PORT=3306;
SPRING_DATASOURCE_USERNAME=root;
SPRING_PROFILES_ACTIVE=local;
```