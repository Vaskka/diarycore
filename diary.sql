-- MySQL dump 10.13  Distrib 5.7.31, for macos10.14 (x86_64)
--
-- Host: localhost    Database: diary
-- ------------------------------------------------------
-- Server version	5.7.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


CREATE DATABASE IF NOT EXISTS diary CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE diary;
--
-- Table structure for table `author`
--

DROP TABLE IF EXISTS `author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `author` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `author_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `author_avatar_url` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL,
  `extern_param` varchar(2048) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_index_name_idx` (`author_name`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `author_type`
--

DROP TABLE IF EXISTS `author_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `author_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `author_id` bigint(20) NOT NULL,
  `author_type_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `extern_param` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT '{}',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `diary`
--

DROP TABLE IF EXISTS `diary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `author_id` bigint(20) DEFAULT NULL,
  `diary_title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sub_title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `diary_date_timestamp` bigint(20) NOT NULL COMMENT 'diary date',
  `start_page` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `end_page` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `origin_pic` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'original pdf or jpeg or ...',
  `comment` varchar(4096) COLLATE utf8mb4_unicode_ci NOT NULL,
  `extern_param` varchar(2048) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_author_title_sub_title_idx` (`author_id`,`diary_title`,`sub_title`),
  UNIQUE KEY `u_author_title_timestamp` (`author_id`,`diary_title`,`diary_date_timestamp`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ips` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `psw` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `extern_param` varchar(2048) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_uidx` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_diary`
--

DROP TABLE IF EXISTS `user_diary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_diary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` bigint(20) NOT NULL,
  `diary_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_u_d` (`user_id`,`diary_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_ip`
--

DROP TABLE IF EXISTS `user_ip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_ip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ip` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_ip` (`ip`),
  KEY `idx_user_id` (`ref_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_session`
--

DROP TABLE IF EXISTS `user_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_session` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` bigint(20) NOT NULL,
  `auth_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `expire_timestamp` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_uid_expire` (`user_id`,`expire_timestamp`),
  KEY `idx_authtoken_expiretimestamp` (`auth_token`,`expire_timestamp`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-27 20:41:04
