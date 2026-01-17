CREATE TABLE `basket` (
  `basket_id` varchar(255) NOT NULL,
  `product_id` varchar(255) NOT NULL,
  `added_at` datetime(6) NULL,
  `item_quantity` int NOT NULL,
  PRIMARY KEY (`basket_id`, `product_id`),
  INDEX `IDXpblwgb0g640da7fm6jcedfwhc` (`product_id`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `brand` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) NULL,
  `name` varchar(255) NULL,
  `updated_at` datetime(6) NULL,
  PRIMARY KEY (`id`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `lookup_basket` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UKggqd45gk0f3qm6lmwnbpbuau7` (`user_id`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `lookup_user` (
  `id` varchar(255) NOT NULL,
  `subject` varchar(255) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UKrsxu26ikxickyhg1eu8kp1p9x` (`subject`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `order_jpa_entity` (
  `id` varchar(255) NOT NULL,
  `status` tinyint NULL,
  `user_id` varchar(255) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `order_jpa_entity_chk_1` CHECK (`status` between 0 and 2)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `product` (
  `id` varchar(255) NOT NULL,
  `brand_id` varchar(255) NULL,
  `created_at` datetime(6) NULL,
  `description` varchar(255) NULL,
  `image_url` varchar(255) NULL,
  `name` varchar(255) NULL,
  `price` int NOT NULL,
  `quantity` int NOT NULL,
  `updated_at` datetime(6) NULL,
  PRIMARY KEY (`id`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) NULL,
  `updated_at` datetime(6) NULL,
  `user_name` varchar(255) NULL,
  PRIMARY KEY (`id`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `user_metadata` (
  `subject` varchar(255) NOT NULL,
  `basket_id` varchar(255) NULL,
  `user_id` varchar(255) NULL,
  PRIMARY KEY (`subject`),
  UNIQUE INDEX `UKfbok3d6mosspb3qror6wasuqn` (`user_id`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
