-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: k9a106.p.ssafy.io    Database: drill
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `comment_id` bigint NOT NULL AUTO_INCREMENT,
  `comment_content` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `comment_write_time` datetime(6) NOT NULL,
  `member_nickname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `post_id` bigint NOT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `FKa97l1frc4jgrqep9s3mtlh72n` (`member_nickname`),
  KEY `FKs1slvnkuemjsq2kj4h3vhx7i1` (`post_id`),
  CONSTRAINT `FKa97l1frc4jgrqep9s3mtlh72n` FOREIGN KEY (`member_nickname`) REFERENCES `member` (`member_nickname`),
  CONSTRAINT `FKs1slvnkuemjsq2kj4h3vhx7i1` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `course_id` bigint NOT NULL AUTO_INCREMENT,
  `center` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `course_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `difficulty` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_new` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=361 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'center1','하양보라_HD','difficulty1',_binary ''),(2,'center1','하양회색_HD','difficulty1',_binary ''),(3,'center1','하양갈색_HD','difficulty1',_binary ''),(4,'center1','하양검정_HD','difficulty1',_binary ''),(5,'center1','노랑보라_HD','difficulty2',_binary ''),(6,'center1','노랑회색_HD','difficulty2',_binary ''),(7,'center1','노랑갈색_HD','difficulty2',_binary ''),(8,'center1','노랑검정_HD','difficulty2',_binary ''),(9,'center1','주황보라_HD','difficulty3',_binary ''),(10,'center1','주황회색_HD','difficulty3',_binary ''),(11,'center1','주황갈색_HD','difficulty3',_binary ''),(12,'center1','주황검정_HD','difficulty3',_binary ''),(13,'center1','초록보라_HD','difficulty4',_binary ''),(14,'center1','초록회색_HD','difficulty4',_binary ''),(15,'center1','초록갈색_HD','difficulty4',_binary ''),(16,'center1','초록검정_HD','difficulty4',_binary ''),(17,'center1','파랑보라_HD','difficulty5',_binary ''),(18,'center1','파랑회색_HD','difficulty5',_binary ''),(19,'center1','파랑갈색_HD','difficulty5',_binary ''),(20,'center1','파랑검정_HD','difficulty5',_binary ''),(21,'center1','빨강보라_HD','difficulty6',_binary ''),(22,'center1','빨강회색_HD','difficulty6',_binary ''),(23,'center1','빨강갈색_HD','difficulty6',_binary ''),(24,'center1','빨강검정_HD','difficulty6',_binary ''),(25,'center1','보라보라_HD','difficulty7',_binary ''),(26,'center1','보라회색_HD','difficulty7',_binary ''),(27,'center1','보라갈색_HD','difficulty7',_binary ''),(28,'center1','보라검정_HD','difficulty7',_binary ''),(29,'center1','핑크보라_HD','difficulty8',_binary ''),(30,'center1','핑크회색_HD','difficulty8',_binary ''),(31,'center1','핑크갈색_HD','difficulty8',_binary ''),(32,'center1','핑크검정_HD','difficulty8',_binary ''),(33,'center1','검정보라_HD','difficulty9',_binary ''),(34,'center1','검정회색_HD','difficulty9',_binary ''),(35,'center1','검정갈색_HD','difficulty9',_binary ''),(36,'center1','검정검정_HD','difficulty9',_binary ''),(37,'center2','하양보라_IS','difficulty1',_binary ''),(38,'center2','하양회색_IS','difficulty1',_binary ''),(39,'center2','하양갈색_IS','difficulty1',_binary ''),(40,'center2','하양검정_IS','difficulty1',_binary ''),(41,'center2','노랑보라_IS','difficulty2',_binary ''),(42,'center2','노랑회색_IS','difficulty2',_binary ''),(43,'center2','노랑갈색_IS','difficulty2',_binary ''),(44,'center2','노랑검정_IS','difficulty2',_binary ''),(45,'center2','주황보라_IS','difficulty3',_binary ''),(46,'center2','주황회색_IS','difficulty3',_binary ''),(47,'center2','주황갈색_IS','difficulty3',_binary ''),(48,'center2','주황검정_IS','difficulty3',_binary ''),(49,'center2','초록보라_IS','difficulty4',_binary ''),(50,'center2','초록회색_IS','difficulty4',_binary ''),(51,'center2','초록갈색_IS','difficulty4',_binary ''),(52,'center2','초록검정_IS','difficulty4',_binary ''),(53,'center2','파랑보라_IS','difficulty5',_binary ''),(54,'center2','파랑회색_IS','difficulty5',_binary ''),(55,'center2','파랑갈색_IS','difficulty5',_binary ''),(56,'center2','파랑검정_IS','difficulty5',_binary ''),(57,'center2','빨강보라_IS','difficulty6',_binary ''),(58,'center2','빨강회색_IS','difficulty6',_binary ''),(59,'center2','빨강갈색_IS','difficulty6',_binary ''),(60,'center2','빨강검정_IS','difficulty6',_binary ''),(61,'center2','보라보라_IS','difficulty7',_binary ''),(62,'center2','보라회색_IS','difficulty7',_binary ''),(63,'center2','보라갈색_IS','difficulty7',_binary ''),(64,'center2','보라검정_IS','difficulty7',_binary ''),(65,'center2','핑크보라_IS','difficulty8',_binary ''),(66,'center2','핑크회색_IS','difficulty8',_binary ''),(67,'center2','핑크갈색_IS','difficulty8',_binary ''),(68,'center2','핑크검정_IS','difficulty8',_binary ''),(69,'center2','검정보라_IS','difficulty9',_binary ''),(70,'center2','검정회색_IS','difficulty9',_binary ''),(71,'center2','검정갈색_IS','difficulty9',_binary ''),(72,'center2','검정검정_IS','difficulty9',_binary ''),(73,'center3','하양보라_YJ','difficulty1',_binary ''),(74,'center3','하양회색_YJ','difficulty1',_binary ''),(75,'center3','하양갈색_YJ','difficulty1',_binary ''),(76,'center3','하양검정_YJ','difficulty1',_binary ''),(77,'center3','노랑보라_YJ','difficulty2',_binary ''),(78,'center3','노랑회색_YJ','difficulty2',_binary ''),(79,'center3','노랑갈색_YJ','difficulty2',_binary ''),(80,'center3','노랑검정_YJ','difficulty2',_binary ''),(81,'center3','주황보라_YJ','difficulty3',_binary ''),(82,'center3','주황회색_YJ','difficulty3',_binary ''),(83,'center3','주황갈색_YJ','difficulty3',_binary ''),(84,'center3','주황검정_YJ','difficulty3',_binary ''),(85,'center3','초록보라_YJ','difficulty4',_binary ''),(86,'center3','초록회색_YJ','difficulty4',_binary ''),(87,'center3','초록갈색_YJ','difficulty4',_binary ''),(88,'center3','초록검정_YJ','difficulty4',_binary ''),(89,'center3','파랑보라_YJ','difficulty5',_binary ''),(90,'center3','파랑회색_YJ','difficulty5',_binary ''),(91,'center3','파랑갈색_YJ','difficulty5',_binary ''),(92,'center3','파랑검정_YJ','difficulty5',_binary ''),(93,'center3','빨강보라_YJ','difficulty6',_binary ''),(94,'center3','빨강회색_YJ','difficulty6',_binary ''),(95,'center3','빨강갈색_YJ','difficulty6',_binary ''),(96,'center3','빨강검정_YJ','difficulty6',_binary ''),(97,'center3','보라보라_YJ','difficulty7',_binary ''),(98,'center3','보라회색_YJ','difficulty7',_binary ''),(99,'center3','보라갈색_YJ','difficulty7',_binary ''),(100,'center3','보라검정_YJ','difficulty7',_binary ''),(101,'center3','핑크보라_YJ','difficulty8',_binary ''),(102,'center3','핑크회색_YJ','difficulty8',_binary ''),(103,'center3','핑크갈색_YJ','difficulty8',_binary ''),(104,'center3','핑크검정_YJ','difficulty8',_binary ''),(105,'center3','검정보라_YJ','difficulty9',_binary ''),(106,'center3','검정회색_YJ','difficulty9',_binary ''),(107,'center3','검정갈색_YJ','difficulty9',_binary ''),(108,'center3','검정검정_YJ','difficulty9',_binary ''),(109,'center4','하양보라_MG','difficulty1',_binary ''),(110,'center4','하양회색_MG','difficulty1',_binary ''),(111,'center4','하양갈색_MG','difficulty1',_binary ''),(112,'center4','하양검정_MG','difficulty1',_binary ''),(113,'center4','노랑보라_MG','difficulty2',_binary ''),(114,'center4','노랑회색_MG','difficulty2',_binary ''),(115,'center4','노랑갈색_MG','difficulty2',_binary ''),(116,'center4','노랑검정_MG','difficulty2',_binary ''),(117,'center4','주황보라_MG','difficulty3',_binary ''),(118,'center4','주황회색_MG','difficulty3',_binary ''),(119,'center4','주황갈색_MG','difficulty3',_binary ''),(120,'center4','주황검정_MG','difficulty3',_binary ''),(121,'center4','초록보라_MG','difficulty4',_binary ''),(122,'center4','초록회색_MG','difficulty4',_binary ''),(123,'center4','초록갈색_MG','difficulty4',_binary ''),(124,'center4','초록검정_MG','difficulty4',_binary ''),(125,'center4','파랑보라_MG','difficulty5',_binary ''),(126,'center4','파랑회색_MG','difficulty5',_binary ''),(127,'center4','파랑갈색_MG','difficulty5',_binary ''),(128,'center4','파랑검정_MG','difficulty5',_binary ''),(129,'center4','빨강보라_MG','difficulty6',_binary ''),(130,'center4','빨강회색_MG','difficulty6',_binary ''),(131,'center4','빨강갈색_MG','difficulty6',_binary ''),(132,'center4','빨강검정_MG','difficulty6',_binary ''),(133,'center4','보라보라_MG','difficulty7',_binary ''),(134,'center4','보라회색_MG','difficulty7',_binary ''),(135,'center4','보라갈색_MG','difficulty7',_binary ''),(136,'center4','보라검정_MG','difficulty7',_binary ''),(137,'center4','핑크보라_MG','difficulty8',_binary ''),(138,'center4','핑크회색_MG','difficulty8',_binary ''),(139,'center4','핑크갈색_MG','difficulty8',_binary ''),(140,'center4','핑크검정_MG','difficulty8',_binary ''),(141,'center4','검정보라_MG','difficulty9',_binary ''),(142,'center4','검정회색_MG','difficulty9',_binary ''),(143,'center4','검정갈색_MG','difficulty9',_binary ''),(144,'center4','검정검정_MG','difficulty9',_binary ''),(145,'center5','하양보라_SL','difficulty1',_binary ''),(146,'center5','하양회색_SL','difficulty1',_binary ''),(147,'center5','하양갈색_SL','difficulty1',_binary ''),(148,'center5','하양검정_SL','difficulty1',_binary ''),(149,'center5','노랑보라_SL','difficulty2',_binary ''),(150,'center5','노랑회색_SL','difficulty2',_binary ''),(151,'center5','노랑갈색_SL','difficulty2',_binary ''),(152,'center5','노랑검정_SL','difficulty2',_binary ''),(153,'center5','주황보라_SL','difficulty3',_binary ''),(154,'center5','주황회색_SL','difficulty3',_binary ''),(155,'center5','주황갈색_SL','difficulty3',_binary ''),(156,'center5','주황검정_SL','difficulty3',_binary ''),(157,'center5','초록보라_SL','difficulty4',_binary ''),(158,'center5','초록회색_SL','difficulty4',_binary ''),(159,'center5','초록갈색_SL','difficulty4',_binary ''),(160,'center5','초록검정_SL','difficulty4',_binary ''),(161,'center5','파랑보라_SL','difficulty5',_binary ''),(162,'center5','파랑회색_SL','difficulty5',_binary ''),(163,'center5','파랑갈색_SL','difficulty5',_binary ''),(164,'center5','파랑검정_SL','difficulty5',_binary ''),(165,'center5','빨강보라_SL','difficulty6',_binary ''),(166,'center5','빨강회색_SL','difficulty6',_binary ''),(167,'center5','빨강갈색_SL','difficulty6',_binary ''),(168,'center5','빨강검정_SL','difficulty6',_binary ''),(169,'center5','보라보라_SL','difficulty7',_binary ''),(170,'center5','보라회색_SL','difficulty7',_binary ''),(171,'center5','보라갈색_SL','difficulty7',_binary ''),(172,'center5','보라검정_SL','difficulty7',_binary ''),(173,'center5','핑크보라_SL','difficulty8',_binary ''),(174,'center5','핑크회색_SL','difficulty8',_binary ''),(175,'center5','핑크갈색_SL','difficulty8',_binary ''),(176,'center5','핑크검정_SL','difficulty8',_binary ''),(177,'center5','검정보라_SL','difficulty9',_binary ''),(178,'center5','검정회색_SL','difficulty9',_binary ''),(179,'center5','검정갈색_SL','difficulty9',_binary ''),(180,'center5','검정검정_SL','difficulty9',_binary ''),(181,'center6','하양보라_YN','difficulty1',_binary ''),(182,'center6','하양회색_YN','difficulty1',_binary ''),(183,'center6','하양갈색_YN','difficulty1',_binary ''),(184,'center6','하양검정_YN','difficulty1',_binary ''),(185,'center6','노랑보라_YN','difficulty2',_binary ''),(186,'center6','노랑회색_YN','difficulty2',_binary ''),(187,'center6','노랑갈색_YN','difficulty2',_binary ''),(188,'center6','노랑검정_YN','difficulty2',_binary ''),(189,'center6','주황보라_YN','difficulty3',_binary ''),(190,'center6','주황회색_YN','difficulty3',_binary ''),(191,'center6','주황갈색_YN','difficulty3',_binary ''),(192,'center6','주황검정_YN','difficulty3',_binary ''),(193,'center6','초록보라_YN','difficulty4',_binary ''),(194,'center6','초록회색_YN','difficulty4',_binary ''),(195,'center6','초록갈색_YN','difficulty4',_binary ''),(196,'center6','초록검정_YN','difficulty4',_binary ''),(197,'center6','파랑보라_YN','difficulty5',_binary ''),(198,'center6','파랑회색_YN','difficulty5',_binary ''),(199,'center6','파랑갈색_YN','difficulty5',_binary ''),(200,'center6','파랑검정_YN','difficulty5',_binary ''),(201,'center6','빨강보라_YN','difficulty6',_binary ''),(202,'center6','빨강회색_YN','difficulty6',_binary ''),(203,'center6','빨강갈색_YN','difficulty6',_binary ''),(204,'center6','빨강검정_YN','difficulty6',_binary ''),(205,'center6','보라보라_YN','difficulty7',_binary ''),(206,'center6','보라회색_YN','difficulty7',_binary ''),(207,'center6','보라갈색_YN','difficulty7',_binary ''),(208,'center6','보라검정_YN','difficulty7',_binary ''),(209,'center6','핑크보라_YN','difficulty8',_binary ''),(210,'center6','핑크회색_YN','difficulty8',_binary ''),(211,'center6','핑크갈색_YN','difficulty8',_binary ''),(212,'center6','핑크검정_YN','difficulty8',_binary ''),(213,'center6','검정보라_YN','difficulty9',_binary ''),(214,'center6','검정회색_YN','difficulty9',_binary ''),(215,'center6','검정갈색_YN','difficulty9',_binary ''),(216,'center6','검정검정_YN','difficulty9',_binary ''),(217,'center7','하양보라_GN','difficulty1',_binary ''),(218,'center7','하양회색_GN','difficulty1',_binary ''),(219,'center7','하양갈색_GN','difficulty1',_binary ''),(220,'center7','하양검정_GN','difficulty1',_binary ''),(221,'center7','노랑보라_GN','difficulty2',_binary ''),(222,'center7','노랑회색_GN','difficulty2',_binary ''),(223,'center7','노랑갈색_GN','difficulty2',_binary ''),(224,'center7','노랑검정_GN','difficulty2',_binary ''),(225,'center7','주황보라_GN','difficulty3',_binary ''),(226,'center7','주황회색_GN','difficulty3',_binary ''),(227,'center7','주황갈색_GN','difficulty3',_binary ''),(228,'center7','주황검정_GN','difficulty3',_binary ''),(229,'center7','초록보라_GN','difficulty4',_binary ''),(230,'center7','초록회색_GN','difficulty4',_binary ''),(231,'center7','초록갈색_GN','difficulty4',_binary ''),(232,'center7','초록검정_GN','difficulty4',_binary ''),(233,'center7','파랑보라_GN','difficulty5',_binary ''),(234,'center7','파랑회색_GN','difficulty5',_binary ''),(235,'center7','파랑갈색_GN','difficulty5',_binary ''),(236,'center7','파랑검정_GN','difficulty5',_binary ''),(237,'center7','빨강보라_GN','difficulty6',_binary ''),(238,'center7','빨강회색_GN','difficulty6',_binary ''),(239,'center7','빨강갈색_GN','difficulty6',_binary ''),(240,'center7','빨강검정_GN','difficulty6',_binary ''),(241,'center7','보라보라_GN','difficulty7',_binary ''),(242,'center7','보라회색_GN','difficulty7',_binary ''),(243,'center7','보라갈색_GN','difficulty7',_binary ''),(244,'center7','보라검정_GN','difficulty7',_binary ''),(245,'center7','핑크보라_GN','difficulty8',_binary ''),(246,'center7','핑크회색_GN','difficulty8',_binary ''),(247,'center7','핑크갈색_GN','difficulty8',_binary ''),(248,'center7','핑크검정_GN','difficulty8',_binary ''),(249,'center7','검정보라_GN','difficulty9',_binary ''),(250,'center7','검정회색_GN','difficulty9',_binary ''),(251,'center7','검정갈색_GN','difficulty9',_binary ''),(252,'center7','검정검정_GN','difficulty9',_binary ''),(253,'center8','하양보라_SD','difficulty1',_binary ''),(254,'center8','하양회색_SD','difficulty1',_binary ''),(255,'center8','하양갈색_SD','difficulty1',_binary ''),(256,'center8','하양검정_SD','difficulty1',_binary ''),(257,'center8','노랑보라_SD','difficulty2',_binary ''),(258,'center8','노랑회색_SD','difficulty2',_binary ''),(259,'center8','노랑갈색_SD','difficulty2',_binary ''),(260,'center8','노랑검정_SD','difficulty2',_binary ''),(261,'center8','주황보라_SD','difficulty3',_binary ''),(262,'center8','주황회색_SD','difficulty3',_binary ''),(263,'center8','주황갈색_SD','difficulty3',_binary ''),(264,'center8','주황검정_SD','difficulty3',_binary ''),(265,'center8','초록보라_SD','difficulty4',_binary ''),(266,'center8','초록회색_SD','difficulty4',_binary ''),(267,'center8','초록갈색_SD','difficulty4',_binary ''),(268,'center8','초록검정_SD','difficulty4',_binary ''),(269,'center8','파랑보라_SD','difficulty5',_binary ''),(270,'center8','파랑회색_SD','difficulty5',_binary ''),(271,'center8','파랑갈색_SD','difficulty5',_binary ''),(272,'center8','파랑검정_SD','difficulty5',_binary ''),(273,'center8','빨강보라_SD','difficulty6',_binary ''),(274,'center8','빨강회색_SD','difficulty6',_binary ''),(275,'center8','빨강갈색_SD','difficulty6',_binary ''),(276,'center8','빨강검정_SD','difficulty6',_binary ''),(277,'center8','보라보라_SD','difficulty7',_binary ''),(278,'center8','보라회색_SD','difficulty7',_binary ''),(279,'center8','보라갈색_SD','difficulty7',_binary ''),(280,'center8','보라검정_SD','difficulty7',_binary ''),(281,'center8','핑크보라_SD','difficulty8',_binary ''),(282,'center8','핑크회색_SD','difficulty8',_binary ''),(283,'center8','핑크갈색_SD','difficulty8',_binary ''),(284,'center8','핑크검정_SD','difficulty8',_binary ''),(285,'center8','검정보라_SD','difficulty9',_binary ''),(286,'center8','검정회색_SD','difficulty9',_binary ''),(287,'center8','검정갈색_SD','difficulty9',_binary ''),(288,'center8','검정검정_SD','difficulty9',_binary ''),(289,'center9','하양보라_SS','difficulty1',_binary ''),(290,'center9','하양회색_SS','difficulty1',_binary ''),(291,'center9','하양갈색_SS','difficulty1',_binary ''),(292,'center9','하양검정_SS','difficulty1',_binary ''),(293,'center9','노랑보라_SS','difficulty2',_binary ''),(294,'center9','노랑회색_SS','difficulty2',_binary ''),(295,'center9','노랑갈색_SS','difficulty2',_binary ''),(296,'center9','노랑검정_SS','difficulty2',_binary ''),(297,'center9','주황보라_SS','difficulty3',_binary ''),(298,'center9','주황회색_SS','difficulty3',_binary ''),(299,'center9','주황갈색_SS','difficulty3',_binary ''),(300,'center9','주황검정_SS','difficulty3',_binary ''),(301,'center9','초록보라_SS','difficulty4',_binary ''),(302,'center9','초록회색_SS','difficulty4',_binary ''),(303,'center9','초록갈색_SS','difficulty4',_binary ''),(304,'center9','초록검정_SS','difficulty4',_binary ''),(305,'center9','파랑보라_SS','difficulty5',_binary ''),(306,'center9','파랑회색_SS','difficulty5',_binary ''),(307,'center9','파랑갈색_SS','difficulty5',_binary ''),(308,'center9','파랑검정_SS','difficulty5',_binary ''),(309,'center9','빨강보라_SS','difficulty6',_binary ''),(310,'center9','빨강회색_SS','difficulty6',_binary ''),(311,'center9','빨강갈색_SS','difficulty6',_binary ''),(312,'center9','빨강검정_SS','difficulty6',_binary ''),(313,'center9','보라보라_SS','difficulty7',_binary ''),(314,'center9','보라회색_SS','difficulty7',_binary ''),(315,'center9','보라갈색_SS','difficulty7',_binary ''),(316,'center9','보라검정_SS','difficulty7',_binary ''),(317,'center9','핑크보라_SS','difficulty8',_binary ''),(318,'center9','핑크회색_SS','difficulty8',_binary ''),(319,'center9','핑크갈색_SS','difficulty8',_binary ''),(320,'center9','핑크검정_SS','difficulty8',_binary ''),(321,'center9','검정보라_SS','difficulty9',_binary ''),(322,'center9','검정회색_SS','difficulty9',_binary ''),(323,'center9','검정갈색_SS','difficulty9',_binary ''),(324,'center9','검정검정_SS','difficulty9',_binary ''),(325,'center10','하양보라_SU','difficulty1',_binary ''),(326,'center10','하양회색_SU','difficulty1',_binary ''),(327,'center10','하양갈색_SU','difficulty1',_binary ''),(328,'center10','하양검정_SU','difficulty1',_binary ''),(329,'center10','노랑보라_SU','difficulty2',_binary ''),(330,'center10','노랑회색_SU','difficulty2',_binary ''),(331,'center10','노랑갈색_SU','difficulty2',_binary ''),(332,'center10','노랑검정_SU','difficulty2',_binary ''),(333,'center10','주황보라_SU','difficulty3',_binary ''),(334,'center10','주황회색_SU','difficulty3',_binary ''),(335,'center10','주황갈색_SU','difficulty3',_binary ''),(336,'center10','주황검정_SU','difficulty3',_binary ''),(337,'center10','초록보라_SU','difficulty4',_binary ''),(338,'center10','초록회색_SU','difficulty4',_binary ''),(339,'center10','초록갈색_SU','difficulty4',_binary ''),(340,'center10','초록검정_SU','difficulty4',_binary ''),(341,'center10','파랑보라_SU','difficulty5',_binary ''),(342,'center10','파랑회색_SU','difficulty5',_binary ''),(343,'center10','파랑갈색_SU','difficulty5',_binary ''),(344,'center10','파랑검정_SU','difficulty5',_binary ''),(345,'center10','빨강보라_SU','difficulty6',_binary ''),(346,'center10','빨강회색_SU','difficulty6',_binary ''),(347,'center10','빨강갈색_SU','difficulty6',_binary ''),(348,'center10','빨강검정_SU','difficulty6',_binary ''),(349,'center10','보라보라_SU','difficulty7',_binary ''),(350,'center10','보라회색_SU','difficulty7',_binary ''),(351,'center10','보라갈색_SU','difficulty7',_binary ''),(352,'center10','보라검정_SU','difficulty7',_binary ''),(353,'center10','핑크보라_SU','difficulty8',_binary ''),(354,'center10','핑크회색_SU','difficulty8',_binary ''),(355,'center10','핑크갈색_SU','difficulty8',_binary ''),(356,'center10','핑크검정_SU','difficulty8',_binary ''),(357,'center10','검정보라_SU','difficulty9',_binary ''),(358,'center10','검정회색_SU','difficulty9',_binary ''),(359,'center10','검정갈색_SU','difficulty9',_binary ''),(360,'center10','검정검정_SU','difficulty9',_binary '');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `liked`
--

DROP TABLE IF EXISTS `liked`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `liked` (
  `liked_id` bigint NOT NULL AUTO_INCREMENT,
  `member_email` bigint NOT NULL,
  `post_id` bigint NOT NULL,
  PRIMARY KEY (`liked_id`),
  KEY `FK7uqbkemuxdqsq5sob82g555ir` (`member_email`),
  KEY `FKt22ayicw3p3ho63o5e9wl8dgv` (`post_id`),
  CONSTRAINT `FK7uqbkemuxdqsq5sob82g555ir` FOREIGN KEY (`member_email`) REFERENCES `member` (`member_id`),
  CONSTRAINT `FKt22ayicw3p3ho63o5e9wl8dgv` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `liked`
--

LOCK TABLES `liked` WRITE;
/*!40000 ALTER TABLE `liked` DISABLE KEYS */;
/*!40000 ALTER TABLE `liked` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` bigint NOT NULL AUTO_INCREMENT,
  `center` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT 'center0',
  `difficulty` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT 'difficulty1',
  `max_score` bigint NOT NULL DEFAULT '100',
  `member_email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `member_nickname` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `member_score` bigint NOT NULL DEFAULT '0',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT 'ROLE_BEFORE',
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `UK_3orqjaukiw2b73e2gw8rer4rq` (`member_email`),
  UNIQUE KEY `UK_j0kdf0m8cdj4uy6l7ntpgxrlo` (`member_nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1,'center1','difficulty3',225,'3141100714','kakaomin',19,NULL,'ROLE_DONE'),(2,'center1','difficulty1',100,'admin','admin',0,'admin','ROLE_DONE'),(3,'center2','difficulty1',100,'크리스범스테드','크리스범스테드',0,'1','ROLE_DONE'),(4,'center1','difficulty1',100,'email1@example.com','성격이우디르',80,'1','ROLE_ADMIN'),(5,'center2','difficulty2',150,'email2@example.com','모르는개산책',120,'1','ROLE_BOSS'),(6,'center3','difficulty3',200,'email3@example.com','코딱지나발루',160,'1','ROLE_BEFORE'),(7,'center4','difficulty4',100,'email4@example.com','봉구스박보검',90,'1','ROLE_DONE'),(8,'center5','difficulty5',150,'email5@example.com','카드값줘체리',130,'1','ROLE_ADMIN'),(9,'center6','difficulty6',200,'email6@example.com','이웃집또털어',170,'1','ROLE_BOSS'),(10,'center1','difficulty1',100,'3141188516','Ming',0,NULL,'ROLE_DONE'),(11,'center1','difficulty1',100,'ssafy@example1.com','싸피조아',0,NULL,'ROLE_BEFORE'),(12,'center1','difficulty1',100,'pjt@example1.com','자율화이팅',0,NULL,'ROLE_BEFORE');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT,
  `center` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT 'center0',
  `post_content` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `post_thumbnail` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `post_video` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `post_write_time` datetime(6) NOT NULL,
  `course_id` bigint NOT NULL,
  `member_nickname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`post_id`),
  KEY `FKe7p5x3rqf74eb00ynw9x85l5r` (`course_id`),
  KEY `FKao9gkddu4eqooblanveswhlls` (`member_nickname`),
  CONSTRAINT `FKao9gkddu4eqooblanveswhlls` FOREIGN KEY (`member_nickname`) REFERENCES `member` (`member_nickname`),
  CONSTRAINT `FKe7p5x3rqf74eb00ynw9x85l5r` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,'center1','서울대 더클라임','dummy1.png','sh_20231110_1148.mp4','2023-10-19 15:30:00.000000',1,'크리스범스테드'),(2,'center1','6 클밍 재미없는데 재미있다!','dummy2.png','sh9_20231110_1053.mp4','2023-10-20 15:30:00.000000',1,'성격이우디르'),(3,'center1','핑빨 하나 땜에 한시간 하러 다시 왔는데 한방에 성공!','dummy3.png','sh9_20231110_1057.mp4','2023-10-21 15:30:00.000000',1,'모르는개산책'),(4,'center1','나머지는 못하겠고 맥주 갈겨!!!!!!','dummy4.png','sh9_20231110_1058.mp4','2023-10-22 15:30:00.000000',1,'코딱지나발루'),(5,'center1','다이노 흰,빨 나중에 와서 할거니까 암튼 올클~','dummy5.png','sh9_20231110_1136.mp4','2023-10-23 15:30:00.000000',1,'봉구스박보검'),(6,'center1','1,2번빼곤 나머진 오른쪽 순서','dummy6.png','sh9_20231110_1146.mp4','2023-10-28 15:30:00.000000',1,'카드값줘체리'),(7,'center1','서울대 더클라임','dummy7.png','sh9_20231110_1147.mp4','2023-10-24 15:30:00.000000',2,'이웃집또털어'),(8,'center1','클라이밍 재미있다!!','dummy8.png','sh9_20231110_1149.mp4','2023-10-24 15:30:00.000000',2,'Ming'),(9,'center1','전완근 다 털린날.','dummy9.png','climb4.mp4','2023-10-24 15:30:00.000000',2,'싸피조아'),(10,'center1','오운완 완료~','dummy10.png','climb4.mp4','2023-10-24 15:30:00.000000',2,'자율화이팅'),(11,'center1','내일도 하러가야지~','climb1.jpg','climb4.mp4','2023-10-29 15:30:00.000000',1,'kakaomin'),(12,'center2','재밌다','climb1.jpg','climb4.mp4','2023-10-28 15:30:00.000000',2,'kakaomin'),(13,'center1','dddd','climb1.jpg','climb4.mp4','2023-10-30 15:30:00.000000',1,'Ming');
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `report_id` bigint NOT NULL AUTO_INCREMENT,
  `is_check` bit(1) NOT NULL DEFAULT b'1',
  `member_email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `post_id` bigint NOT NULL,
  PRIMARY KEY (`report_id`),
  KEY `FKanga9ck1enll6h56knnstmb5x` (`member_email`),
  KEY `FKnuqod1y014fp5bmqjeoffcgqy` (`post_id`),
  CONSTRAINT `FKanga9ck1enll6h56knnstmb5x` FOREIGN KEY (`member_email`) REFERENCES `member` (`member_email`),
  CONSTRAINT `FKnuqod1y014fp5bmqjeoffcgqy` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-16 15:23:03
