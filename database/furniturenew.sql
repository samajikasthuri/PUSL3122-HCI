-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Apr 20, 2025 at 12:23 PM
-- Server version: 9.1.0
-- PHP Version: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `furniturenew`
--

-- --------------------------------------------------------

--
-- Table structure for table `designs`
--

DROP TABLE IF EXISTS `designs`;
CREATE TABLE IF NOT EXISTS `designs` (
  `design_id` int NOT NULL AUTO_INCREMENT,
  `designer_id` int NOT NULL,
  `design_name` varchar(100) NOT NULL DEFAULT 'Untitled Design',
  `design_data` json NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `room_config` text,
  PRIMARY KEY (`design_id`),
  KEY `designer_id` (`designer_id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `designs`
--

INSERT INTO `designs` (`design_id`, `designer_id`, `design_name`, `design_data`, `created_at`, `updated_at`, `room_config`) VALUES
(1, 1, 'Untitled Design', '[{\"x\": 100, \"y\": 100, \"type\": \"Chair\", \"color\": \"Red\", \"width\": 50, \"height\": 50}]', '2025-04-17 03:54:53', '2025-04-17 03:54:53', NULL),
(2, 1, 'ash', '[{\"x\": 100, \"y\": 100, \"type\": \"Chair\", \"color\": \"Red\", \"width\": 50, \"height\": 50}]', '2025-04-17 04:16:21', '2025-04-17 04:16:21', NULL),
(3, 1, 'jani', '[{\"x\": 455, \"y\": 315, \"type\": \"Table\", \"color\": \"Red\", \"width\": 100, \"height\": 60}, {\"x\": 100, \"y\": 100, \"type\": \"Sofa\", \"color\": \"Red\", \"width\": 120, \"height\": 40}]', '2025-04-17 04:20:32', '2025-04-17 04:20:32', NULL),
(4, 1, 'kk', '[{\"x\": 116, \"y\": 124, \"type\": \"Chair\", \"color\": \"Red\", \"width\": 50, \"height\": 50}]', '2025-04-17 08:53:19', '2025-04-17 08:53:19', NULL),
(5, 1, 'kk2', '[{\"x\": 116, \"y\": 124, \"type\": \"Chair\", \"color\": \"Red\", \"width\": 50, \"height\": 50}, {\"x\": 215, \"y\": 117, \"type\": \"Sofa\", \"color\": \"Green\", \"width\": 185, \"height\": 70}, {\"x\": 184, \"y\": 217, \"type\": \"Table\", \"color\": \"Red\", \"width\": 100, \"height\": 60}]', '2025-04-17 08:54:23', '2025-04-17 08:54:23', NULL),
(6, 1, 'hh', '[{\"x\": 84, \"y\": 93, \"type\": \"Chair\", \"color\": \"Red\", \"width\": 50, \"height\": 50}, {\"x\": 316, \"y\": 106, \"type\": \"Table\", \"color\": \"Red\", \"width\": 100, \"height\": 60}, {\"x\": 184, \"y\": 246, \"type\": \"Sofa\", \"color\": \"Red\", \"width\": 120, \"height\": 40}]', '2025-04-17 09:35:14', '2025-04-17 09:35:14', NULL),
(7, 1, 'ss', '[{\"x\": 123, \"y\": 291, \"type\": \"Sofa\", \"color\": \"Red\", \"width\": 120, \"height\": 40}, {\"x\": 462, \"y\": 198, \"type\": \"Table\", \"color\": \"Red\", \"width\": 100, \"height\": 60}, {\"x\": 100, \"y\": 100, \"type\": \"Chair\", \"color\": \"Red\", \"width\": 50, \"height\": 50}]', '2025-04-17 15:11:31', '2025-04-17 15:11:31', NULL),
(8, 1, 'ty', '[{\"x\": 100, \"y\": 100, \"type\": \"Chair\", \"color\": \"Red\", \"width\": 50, \"height\": 50}, {\"x\": 353, \"y\": 183, \"type\": \"Table\", \"color\": \"Red\", \"width\": 100, \"height\": 60}, {\"x\": 148, \"y\": 315, \"type\": \"Sofa\", \"color\": \"Red\", \"width\": 120, \"height\": 40}]', '2025-04-17 15:23:29', '2025-04-17 15:23:29', NULL),
(9, 1, 'dd', '[{\"x\": 100, \"y\": 100, \"type\": \"Chair\", \"color\": \"Red\", \"width\": 50, \"height\": 50}, {\"x\": 353, \"y\": 183, \"type\": \"Table\", \"color\": \"Red\", \"width\": 100, \"height\": 60}, {\"x\": 148, \"y\": 315, \"type\": \"Sofa\", \"color\": \"Red\", \"width\": 120, \"height\": 40}]', '2025-04-18 18:57:42', '2025-04-18 18:57:42', '{\"width\":400,\"height\":450,\"depth\":500,\"shape\":\"Rectangle\",\"wallColor\":-1,\"floorColor\":-10027162}'),
(10, 1, 'rr', '[{\"x\": 100, \"y\": 100, \"type\": \"Chair\", \"color\": \"Red\", \"width\": 50, \"height\": 50}, {\"x\": 353, \"y\": 183, \"type\": \"Table\", \"color\": \"Red\", \"width\": 100, \"height\": 60}, {\"x\": 148, \"y\": 315, \"type\": \"Sofa\", \"color\": \"Red\", \"width\": 120, \"height\": 40}]', '2025-04-18 18:58:18', '2025-04-18 18:58:18', '{\"width\":540,\"height\":450,\"depth\":500,\"shape\":\"Rectangle\",\"wallColor\":-26215,\"floorColor\":-10027162}'),
(11, 1, 'op', '[{\"x\": 100, \"y\": 100, \"type\": \"Chair\", \"color\": \"Red\", \"width\": 50, \"height\": 50}, {\"x\": 112, \"y\": 306, \"type\": \"Table\", \"color\": \"Red\", \"width\": 100, \"height\": 60}, {\"x\": 373, \"y\": 215, \"type\": \"Sofa\", \"color\": \"Red\", \"width\": 120, \"height\": 40}]', '2025-04-19 03:19:52', '2025-04-19 03:19:52', '{\"width\":680,\"height\":450,\"depth\":500,\"shape\":\"Rectangle\",\"wallColor\":-1,\"floorColor\":-256}'),
(12, 1, 'new', '[{\"x\": 100, \"y\": 100, \"type\": \"Chair\", \"color\": \"Yellow\", \"width\": 90, \"height\": 55}, {\"x\": 117, \"y\": 285, \"type\": \"Table\", \"color\": \"Red\", \"width\": 100, \"height\": 60}, {\"x\": 522, \"y\": 198, \"type\": \"Sofa\", \"color\": \"Green\", \"width\": 120, \"height\": 40}]', '2025-04-19 03:20:46', '2025-04-19 03:20:46', '{\"width\":680,\"height\":450,\"depth\":500,\"shape\":\"Rectangle\",\"wallColor\":-1,\"floorColor\":-256}');

-- --------------------------------------------------------

--
-- Table structure for table `furniture_catalog`
--

DROP TABLE IF EXISTS `furniture_catalog`;
CREATE TABLE IF NOT EXISTS `furniture_catalog` (
  `item_id` int NOT NULL AUTO_INCREMENT,
  `item_type` varchar(50) NOT NULL,
  `default_width` int NOT NULL,
  `default_height` int NOT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `email`, `created_at`) VALUES
(1, 'user', 'password', 'designer1@example.com', '2025-04-17 03:50:39'),
(2, 'designer2', 'hashed_password2', 'designer2@example.com', '2025-04-17 03:50:39');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
