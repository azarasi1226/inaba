-- Create "basket_items" table
CREATE TABLE `basket_items` (
  `basket_id` varchar(255) NOT NULL,
  `product_id` varchar(255) NOT NULL,
  `item_quantity` int NOT NULL,
  `added_at` datetime NOT NULL,
  PRIMARY KEY (`basket_id`, `product_id`),
  INDEX `idx_baskets__product_id` (`product_id`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
-- Create "brands" table
CREATE TABLE `brands` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
-- Create "lookup_baskets" table
CREATE TABLE `lookup_baskets` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_id` (`user_id`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
-- Create "lookup_users" table
CREATE TABLE `lookup_users` (
  `id` varchar(255) NOT NULL,
  `subject` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `subject` (`subject`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
-- Create "orders" table
CREATE TABLE `orders` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
-- Create "products" table
CREATE TABLE `products` (
  `id` varchar(255) NOT NULL,
  `brand_id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `image_url` varchar(255) NULL,
  `price` int NOT NULL,
  `quantity` int NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
-- Create "user_metadata" table
CREATE TABLE `user_metadata` (
  `user_id` varchar(255) NOT NULL,
  `subject` varchar(255) NOT NULL,
  `basket_id` varchar(255) NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `basket_id` (`basket_id`),
  UNIQUE INDEX `subject` (`subject`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
-- Create "users" table
CREATE TABLE `users` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
