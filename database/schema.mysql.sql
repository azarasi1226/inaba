CREATE TABLE lookup_baskets (
  id varchar(255) NOT NULL,
  user_id varchar(255) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE lookup_users (
  id varchar(255) NOT NULL,
  subject varchar(255) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE basket_items (
  basket_id varchar(255) NOT NULL,
  product_id varchar(255) NOT NULL,
  item_quantity int NOT NULL,
  added_at datetime NOT NULL,
  PRIMARY KEY (basket_id, product_id),
  -- 商品が削除された時に、連鎖して対象レコードを削除する用
  INDEX idx_baskets__product_id (product_id)
);

CREATE TABLE brands (
  id varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE orders (
  id varchar(255) NOT NULL,
  user_id varchar(255) NOT NULL,
  status int NOT NULL,
  PRIMARY KEY (id)
);

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

CREATE TABLE users (
  id varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  created_at datetime(6) NOT NULL,
  updated_at datetime(6) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE user_metadata (
  -- この二つは最初のトランザクションで必ず存在する
  user_id varchar(255) NOT NULL,
  subject varchar(255) NOT NULL UNIQUE,
  -- ここからはレコード生成後でないと値がわからないのでNULL許容
  basket_id varchar(255) UNIQUE,
  PRIMARY KEY (user_id)
);
