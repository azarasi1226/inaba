CREATE TABLE users (
  oidc_sub varchar(255) NOT NULL COMMENT 'OIDCのsubクレーム',
  email varchar(255) NOT NULL COMMENT 'ユーザーのメールアドレス、OIDCプロバイダから提供されない可能性があるので、NULLを許容する',
  email_verified boolean NOT NULL,
  name varchar(255) NOT NULL,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) NOT NULL,
  PRIMARY KEY (oidc_sub)
);

CREATE TABLE brands (
  id varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) NOT NULL,
  PRIMARY KEY (id)
);

-- 商品が削除された時に、連鎖して対象レコードを削除する用
CREATE INDEX idx_baskets__product_id
  ON baskets (product_id);

CREATE TABLE products (
  id varchar(255) NOT NULL,
  brand_id varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  description text NOT NULL,
  image_url varchar(255),
  price int NOT NULL,
  quantity int NOT NULL,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE baskets (
  oidc_sub varchar(255) NOT NULL,
  product_id varchar(255) NOT NULL,
  item_quantity int NOT NULL,
  added_at datetime NOT NULL,
  PRIMARY KEY (oidc_sub, product_id)
);

CREATE TABLE orders (
  id varchar(255) NOT NULL,
  user_id varchar(255) NOT NULL,
  status int NOT NULL,
  PRIMARY KEY (id)
);