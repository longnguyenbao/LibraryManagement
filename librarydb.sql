CREATE SCHEMA `librarydb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `librarydb`;
SET SQL_SAFE_UPDATES = 0;

CREATE TABLE `category` (
	`id` int NOT NULL AUTO_INCREMENT,
	`name` text DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `book` (
	`id` varchar(50) NOT NULL,
	`name` text DEFAULT NULL,
	`description` text DEFAULT NULL,
	`publication_year` int DEFAULT NULL,
	`publication_place` text DEFAULT NULL,
	`entry_date` date DEFAULT NULL,
	`location` text DEFAULT NULL,
	`category_id` int DEFAULT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `author` (
	`id` int NOT NULL AUTO_INCREMENT,
	`name` text DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `book_author` (
	`book_id` varchar(50) NOT NULL,
	`author_id` int NOT NULL,
	PRIMARY KEY (`book_id`, `author_id`),
	FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE,
	FOREIGN KEY (`author_id`) REFERENCES `author` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `role` (
	`id` int NOT NULL AUTO_INCREMENT,
	`name` text DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `department` (
	`id` int NOT NULL AUTO_INCREMENT,
	`name` text DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user` (
	`id` int NOT NULL AUTO_INCREMENT,
	`username` varchar(25) NOT NULL UNIQUE,
	`password` varchar(255) NOT NULL,
	`name` text DEFAULT NULL,
	`gender` text DEFAULT NULL,
	`date_of_birth` date DEFAULT NULL,
	`role_id` int DEFAULT NULL,
	`department_id` int DEFAULT NULL,
	`registration_date` date DEFAULT (DATE_FORMAT(NOW(), '%Y-%m-%d')),
	`expiration_date` date DEFAULT (DATE_FORMAT(NOW() + 20000000000, '%Y-%m-%d')),
	`email` text DEFAULT NULL,
	`address` text DEFAULT NULL,
	`phone` varchar(10) DEFAULT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE SET NULL,
	FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `fine` (
	`id` int NOT NULL,
	`amount` float NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `book_user` (
	`id` int NOT NULL AUTO_INCREMENT,
	`book_id` varchar(50) DEFAULT NULL,
	`user_id` int DEFAULT NULL,
	`quantity`  int NOT NULL,
	`return_date` datetime DEFAULT NULL,
	`borrow_date` datetime DEFAULT NULL,
	`book_date` datetime NOT NULL,
	`fine_id` int DEFAULT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE SET NULL,
	FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL,
	FOREIGN KEY (`fine_id`) REFERENCES `fine` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `fine` VALUES (1, 0.0), (2, 5000.0);
INSERT INTO `category` VALUES (1, 'Chưa có danh mục'), (2, 'Sách khoa học'), (3, 'Sách tiếng Anh');
INSERT INTO `author` VALUES (1, 'Tác giả 1'), (2, 'Tác giả 2'), (3, 'Tác giả 3');
INSERT INTO `role` VALUES (1, 'Student'), (2, 'Lecturer'), (3, 'Officer'), (4, 'Admin');
INSERT INTO `department` VALUES (1, 'Khoa công nghệ thông tin'), (2, 'Khoa công nghệ sinh học'), (3, 'Khoa xây dựng');

INSERT INTO `user`(`username`, `password` ,`name`, `gender`, `role_id`, `department_id`, `email`) 
VALUES ('admin', '202cb962ac59075b964b07152d234b70', 'Nguyễn Văn A', 'Nam', 4, 1, 'user1@ou.edu.vn');
INSERT INTO `user`(`username`, `password` ,`name`, `gender`, `role_id`, `department_id`, `registration_date`, `expiration_date`, `email`)  
VALUES ('user', '202cb962ac59075b964b07152d234b70', 'Nguyễn Văn B', 'Nam', 1, 1, '2017-11-18', '2020-12-31', 'user1@ou.edu.vn');

INSERT INTO `book` VALUES ('267f046d-defc-4621-8672-3cf62d56eee1', 'Kiểm thử phần mềm', 'Mô tả 1', 2022, 'TP Hồ Chí Minh', '2022-01-01', 'Kệ sách 1', 1);
INSERT INTO `book` VALUES ('115dd543-06f0-417e-941a-1ec75fde5f64', 'Công nghệ phần mềm', 'Mô tả 2', 2022, 'TP Hồ Chí Minh', '2022-01-01', 'Kệ sách 1', 1);
INSERT INTO `book` VALUES ('372c7092-0c50-4f1a-b9e9-827093a522c2', 'TEST1', 'Mô tả 3', 2022, 'TP Hồ Chí Minh', '2022-01-01', 'Kệ sách 1', 1);
INSERT INTO `book` VALUES ('8e624f82-2281-42ac-b1bf-e84ae3747417', 'TEST2', 'Mô tả 4', 2022, 'TP Hồ Chí Minh', '2022-01-01', 'Kệ sách 1', 1);
INSERT INTO `book_author` VALUES ('267f046d-defc-4621-8672-3cf62d56eee1', 1);
INSERT INTO `book_author` VALUES ('267f046d-defc-4621-8672-3cf62d56eee1', 2);
INSERT INTO `book_author` VALUES ('115dd543-06f0-417e-941a-1ec75fde5f64', 3);