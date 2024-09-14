-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: monedaamoneda
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `egresos`
--

DROP TABLE IF EXISTS `egresos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `egresos` (
  `idEgresos` int NOT NULL,
  `idtipoEgreso` int NOT NULL,
  `descripcionEgreso` varchar(200) NOT NULL,
  `fechaEgreso` datetime NOT NULL,
  `montoIngreso` float NOT NULL,
  `idUsuarios` int NOT NULL,
  PRIMARY KEY (`idEgresos`),
  KEY `fk_egresos_tipoEgreso1_idx` (`idtipoEgreso`),
  KEY `fk_egresos_usuarios1_idx` (`idUsuarios`),
  CONSTRAINT `fk_egresos_tipoEgreso1` FOREIGN KEY (`idtipoEgreso`) REFERENCES `tipoegreso` (`idtipoEgreso`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_egresos_usuarios1` FOREIGN KEY (`idUsuarios`) REFERENCES `usuarios` (`idUsuarios`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `egresos`
--

LOCK TABLES `egresos` WRITE;
/*!40000 ALTER TABLE `egresos` DISABLE KEYS */;
/*!40000 ALTER TABLE `egresos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingresos`
--

DROP TABLE IF EXISTS `ingresos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingresos` (
  `idIngreso` int NOT NULL AUTO_INCREMENT,
  `idtipoIngreso` int NOT NULL,
  `descripcionIngreso` varchar(200) NOT NULL,
  `fechaIngreso` datetime NOT NULL,
  `montoIngreso` float NOT NULL,
  `idUsuarios` int NOT NULL,
  PRIMARY KEY (`idIngreso`),
  KEY `fk_ingresos_tipoIngreso_idx` (`idtipoIngreso`),
  KEY `fk_ingresos_usuarios1_idx` (`idUsuarios`),
  CONSTRAINT `fk_ingresos_tipoIngreso` FOREIGN KEY (`idtipoIngreso`) REFERENCES `tipoingreso` (`idtipoIngreso`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_ingresos_usuarios1` FOREIGN KEY (`idUsuarios`) REFERENCES `usuarios` (`idUsuarios`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingresos`
--

LOCK TABLES `ingresos` WRITE;
/*!40000 ALTER TABLE `ingresos` DISABLE KEYS */;
/*!40000 ALTER TABLE `ingresos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipoegreso`
--

DROP TABLE IF EXISTS `tipoegreso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipoegreso` (
  `idtipoEgreso` int NOT NULL,
  `descripcionTipoEgreso` varchar(45) NOT NULL,
  PRIMARY KEY (`idtipoEgreso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipoegreso`
--

LOCK TABLES `tipoegreso` WRITE;
/*!40000 ALTER TABLE `tipoegreso` DISABLE KEYS */;
/*!40000 ALTER TABLE `tipoegreso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipoingreso`
--

DROP TABLE IF EXISTS `tipoingreso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipoingreso` (
  `idtipoIngreso` int NOT NULL,
  `descripcionTipoIngreso` varchar(45) NOT NULL,
  PRIMARY KEY (`idtipoIngreso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipoingreso`
--

LOCK TABLES `tipoingreso` WRITE;
/*!40000 ALTER TABLE `tipoingreso` DISABLE KEYS */;
/*!40000 ALTER TABLE `tipoingreso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `idUsuarios` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `password` varchar(250) NOT NULL,
  `saldoInicial` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`idUsuarios`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'lucas','123',0);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-13 21:14:51
