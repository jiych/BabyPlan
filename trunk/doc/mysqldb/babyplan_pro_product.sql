CREATE DATABASE  IF NOT EXISTS `babyplan` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `babyplan`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: babyplan
-- ------------------------------------------------------
-- Server version	5.5.25

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

--
-- Table structure for table `pro_product`
--

DROP TABLE IF EXISTS `pro_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pro_product` (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `bb_info` varchar(1024) NOT NULL,
  `state` int(11) NOT NULL DEFAULT '1' COMMENT '1：启用 2：禁用',
  `level` int(11) NOT NULL DEFAULT '1' COMMENT '1：普通 2：VIP',
  `create_id` int(11) DEFAULT NULL,
  `view_num` int(11) DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `iswash` int(11) NOT NULL DEFAULT '1' COMMENT '1：洗过 2：未洗',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pro_product`
--

LOCK TABLES `pro_product` WRITE;
/*!40000 ALTER TABLE `pro_product` DISABLE KEYS */;
INSERT INTO `pro_product` VALUES (1,'34','ewofjewofeowqf',1,1,2,22,'2012-07-27 13:57:11',1),(2,'66','345345345',1,1,2,60,'2012-07-27 14:05:37',1),(3,'7777','456546',1,1,3,0,'2012-08-01 13:03:35',1),(4,'dsf','sdfdsafdsafdsaf',1,1,5,6,'2012-08-20 13:17:33',1);
/*!40000 ALTER TABLE `pro_product` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-08-20 22:02:20
