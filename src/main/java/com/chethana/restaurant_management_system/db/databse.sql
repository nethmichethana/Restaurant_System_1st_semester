CREATE
DATABASE IF NOT EXISTS restaurant_management_system;
USE
restaurant_management_system;

DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`
(
    `customer_id`   int          NOT NULL AUTO_INCREMENT,
    `customer_code` varchar(20)  NOT NULL,
    `name`          varchar(100) NOT NULL,
    `email`         varchar(100) DEFAULT NULL,
    `contact`       varchar(15)  DEFAULT NULL,
    `created_at`    timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`customer_id`),
    UNIQUE KEY `customer_code` (`customer_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `customer`
VALUES (1, 'CT001', 'Dulanjana Lakshan', 'dulanjana20013@gmail.com', '0712280020', '2026-01-01 07:53:25'),
       (2, 'CT002', 'Kasun Perera', 'kasun@example.com', '0711234567', '2026-01-01 08:00:00'),
       (3, 'CT003', 'Nadeesha Silva', 'nadeesha@example.com', '0717654321', '2026-01-01 08:10:00');

DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff`
(
    `staff_id`   int          NOT NULL AUTO_INCREMENT,
    `staff_code` varchar(20)  NOT NULL,
    `name`       varchar(100) NOT NULL,
    `email`      varchar(100) NOT NULL,
    `contact`    varchar(15) DEFAULT NULL,
    `role`       enum('OWNER','WAITER','KITCHEN','CASHIER','ADMIN') NOT NULL,
    `username`   varchar(50)  NOT NULL,
    `password`   varchar(255) NOT NULL,
    `hire_date`  date        DEFAULT NULL,
    `status`     enum('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`staff_id`),
    UNIQUE KEY `staff_code` (`staff_code`),
    UNIQUE KEY `email` (`email`),
    UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `staff`
VALUES (1, 'STF001', 'Owner User', 'owner@restaurant.com', '0711111111', 'OWNER', 'owner', 'owner123', '2024-01-01',
        'ACTIVE', '2026-01-01 01:56:39'),
       (2, 'STF002', 'Admin User', 'admin@restaurant.com', '0722222222', 'ADMIN', 'admin', 'admin123', '2024-02-01',
        'ACTIVE', '2026-01-01 01:56:39'),
       (3, 'STF003', 'Waiter User', 'waiter@restaurant.com', '0733333334', 'WAITER', 'waiter', 'waiter123',
        '2024-03-01', 'ACTIVE', '2026-01-01 01:56:39'),
       (4, 'STF004', 'Kitchen User', 'kitchen@restaurant.com', '0744444444', 'KITCHEN', 'kitchen', 'kitchen123',
        '2024-04-01', 'ACTIVE', '2026-01-01 01:56:39'),
       (5, 'STF005', 'Cashier User', 'cashier@restaurant.com', '0755555555', 'CASHIER', 'cashier', 'cashier123',
        '2024-05-01', 'ACTIVE', '2026-01-01 01:56:39'),
       (6, 'STF006', 'Dulanjana Lakshan', 'dulanjana@gmail.com', '0712280020', 'ADMIN', 'admin2', 'admin', '2026-01-01',
        'ACTIVE', '2026-01-01 07:41:44');

DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory`
(
    `inventory_id` int          NOT NULL AUTO_INCREMENT,
    `item_name`    varchar(100) NOT NULL,
    `quantity`     int          NOT NULL,
    `unit`         varchar(20) DEFAULT NULL,
    `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`inventory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `inventory`
VALUES (1, 'Flour', 50, 'kg', '2026-01-01 06:30:00'),
       (2, 'Chicken', 30, 'kg', '2026-01-01 06:30:00'),
       (3, 'Salmon', 20, 'kg', '2026-01-01 06:30:00'),
       (4, 'Chocolate', 15, 'kg', '2026-01-01 06:30:00'),
       (5, 'Vegetables', 40, 'kg', '2026-01-01 06:30:00');

DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier`
(
    `supplier_id` int          NOT NULL AUTO_INCREMENT,
    `name`        varchar(100) NOT NULL,
    `contact`     varchar(15)  DEFAULT NULL,
    `email`       varchar(100) DEFAULT NULL,
    `created_at`  timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`supplier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `supplier`
VALUES (1, 'ABC Foods', '0712345678', 'abc@foods.com', '2026-01-01 06:30:00'),
       (2, 'Fresh Supplies', '0723456789', 'fresh@supplies.com', '2026-01-01 06:30:00');

DROP TABLE IF EXISTS `supplier_inventory`;
CREATE TABLE `supplier_inventory`
(
    `supplier_inventory_id` int NOT NULL AUTO_INCREMENT,
    `supplier_id`           int NOT NULL,
    `inventory_id`          int NOT NULL,
    PRIMARY KEY (`supplier_inventory_id`),
    KEY                     `supplier_id` (`supplier_id`),
    KEY                     `inventory_id` (`inventory_id`),
    CONSTRAINT `supplier_inventory_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`supplier_id`),
    CONSTRAINT `supplier_inventory_ibfk_2` FOREIGN KEY (`inventory_id`) REFERENCES `inventory` (`inventory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `supplier_inventory`
VALUES (1, 1, 1),
       (2, 1, 2),
       (3, 2, 3),
       (4, 2, 5);

DROP TABLE IF EXISTS `menu_item`;
CREATE TABLE `menu_item`
(
    `menu_item_id` int            NOT NULL AUTO_INCREMENT,
    `name`         varchar(100)   NOT NULL,
    `category`     varchar(50) DEFAULT NULL,
    `description`  text,
    `price`        decimal(10, 2) NOT NULL,
    `availability` tinyint(1) DEFAULT '1',
    `created_at`   timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`menu_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `menu_item`
VALUES (1, 'Garlic Bread', 'Appetizers', 'Freshly baked bread with garlic butter', 450.00, 1, '2026-01-01 06:33:30'),
       (2, 'Caesar Salad', 'Appetizers', 'Romaine lettuce with Caesar dressing', 650.00, 1, '2026-01-01 06:33:30'),
       (3, 'Tomato Soup', 'Appetizers', 'Creamy tomato soup', 400.00, 1, '2026-01-01 06:33:30'),
       (4, 'Grilled Salmon', 'Main Course', 'Fresh salmon with lemon butter sauce', 1860.00, 1, '2026-01-01 06:33:30'),
       (5, 'Chicken Alfredo Pasta', 'Main Course', 'Creamy pasta with grilled chicken', 1250.00, 1,
        '2026-01-01 06:33:30'),
       (6, 'Vegetable Stir Fry', 'Main Course', 'Assorted vegetables in soy sauce', 950.00, 1, '2026-01-01 06:33:30'),
       (7, 'Chocolate Lava Cake', 'Desserts', 'Warm chocolate cake with molten center', 550.00, 1,
        '2026-01-01 06:33:30'),
       (8, 'Ice Cream Sundae', 'Desserts', 'Vanilla ice cream with chocolate sauce', 450.00, 1, '2026-01-01 06:33:30'),
       (9, 'Fresh Orange Juice', 'Beverages', 'Freshly squeezed orange juice', 350.00, 1, '2026-01-01 06:33:30'),
       (10, 'Cappuccino', 'Beverages', 'Hot cappuccino with foam', 400.00, 1, '2026-01-01 06:33:30'),
       (11, 'Iced Tea', 'Beverages', 'Refreshing iced tea with lemon', 300.00, 1, '2026-01-01 06:33:30');

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`
(
    `order_id`       int            NOT NULL AUTO_INCREMENT,
    `order_code`     varchar(20) DEFAULT NULL,
    `customer_id`    int            NOT NULL,
    `staff_id`       int            NOT NULL,
    `order_time`     timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `total_amount`   decimal(10, 2) NOT NULL,
    `status`         varchar(20) DEFAULT 'Pending',
    `payment_method` varchar(20) DEFAULT 'Cash',
    `notes`          text,
    PRIMARY KEY (`order_id`),
    UNIQUE KEY `order_code` (`order_code`),
    KEY              `fk_orders_customer` (`customer_id`),
    KEY              `fk_orders_staff` (`staff_id`),
    CONSTRAINT `fk_orders_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
    CONSTRAINT `fk_orders_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `orders`
VALUES (1, 'ORD00001', 1, 3, '2026-01-01 08:47:46', 7750.00, 'Pending', 'Cash', ''),
       (2, 'ORD00002', 2, 5, '2026-01-01 09:00:00', 1450.00, 'Confirmed', 'Card', 'No onions in salad');

DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items`
(
    `order_item_id` int            NOT NULL AUTO_INCREMENT,
    `order_id`      int            NOT NULL,
    `menu_item_id`  int            NOT NULL,
    `quantity`      int            NOT NULL,
    `unit_price`    decimal(10, 2) NOT NULL,
    `total_price`   decimal(10, 2) NOT NULL,
    PRIMARY KEY (`order_item_id`),
    KEY             `fk_order_items_order` (`order_id`),
    KEY             `fk_order_items_menu` (`menu_item_id`),
    CONSTRAINT `fk_order_items_menu` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_item` (`menu_item_id`),
    CONSTRAINT `fk_order_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `order_items`
VALUES (1, 1, 7, 10, 550.00, 5500.00),
       (2, 1, 1, 5, 450.00, 2250.00),
       (3, 2, 2, 1, 650.00, 650.00),
       (4, 2, 11, 2, 400.00, 800.00);

DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment`
(
    `payment_id`     int            NOT NULL AUTO_INCREMENT,
    `order_id`       int            NOT NULL,
    `amount`         decimal(10, 2) NOT NULL,
    `payment_method` varchar(20) DEFAULT NULL,
    `payment_time`   timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`payment_id`),
    KEY              `fk_payment_order` (`order_id`),
    CONSTRAINT `fk_payment_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `payment`
VALUES (1, 1, 7750.00, 'Cash', '2026-01-01 08:50:00'),
       (2, 2, 1450.00, 'Card', '2026-01-01 09:05:00');

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation`
(
    `reservation_id`   int  NOT NULL AUTO_INCREMENT,
    `customer_id`      int  NOT NULL,
    `date`             date NOT NULL,
    `time`             time NOT NULL,
    `number_of_guests` int  NOT NULL,
    `table_number`     varchar(10) DEFAULT NULL,
    `status`           varchar(20) DEFAULT 'Pending',
    `special_requests` text,
    `created_at`       timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`reservation_id`),
    KEY                `customer_id` (`customer_id`),
    CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `reservation`
VALUES (1, 1, '2026-01-01', '18:30:00', 5, NULL, 'Confirmed', NULL, '2026-01-01 08:29:18'),
       (2, 1, '2026-01-01', '10:11:00', 2, NULL, 'Cancelled', NULL, '2026-01-01 08:35:53'),
       (3, 2, '2026-01-01', '12:00:00', 3, 'T5', 'Pending', 'Window seat', '2026-01-01 08:45:00');

DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`
(
    `report_id`     int         NOT NULL AUTO_INCREMENT,
    `staff_id`      int         NOT NULL,
    `report_type`   varchar(50) NOT NULL,
    `generate_date` datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`report_id`),
    KEY             `staff_id` (`staff_id`),
    CONSTRAINT `report_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `report`
VALUES (1, 1, 'Daily Sales', '2026-01-01 23:59:00'),
       (2, 2, 'Inventory Status', '2026-01-01 12:00:00');

DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket`
(
    `ticket_id`   int NOT NULL AUTO_INCREMENT,
    `staff_id`    int NOT NULL,
    `title`       varchar(100) DEFAULT NULL,
    `description` text,
    `status`      enum('OPEN','IN_PROGRESS','CLOSED') DEFAULT 'OPEN',
    `created_at`  timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`ticket_id`),
    KEY           `staff_id` (`staff_id`),
    CONSTRAINT `ticket_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `ticket`
VALUES (1, 3, 'Coffee Machine Issue', 'Coffee machine not working', 'OPEN', '2026-01-01 09:10:00'),
       (2, 4, 'Oven Malfunction', 'Oven heating issue', 'IN_PROGRESS', '2026-01-01 09:15:00');
