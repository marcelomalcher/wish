-- SCRIPT
-- 23/03/2013

--
-- Table structure for table `tecgenbinarycontent`
--

DROP TABLE IF EXISTS `tecgenbinarycontent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tecgenbinarycontent` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `fileName` varchar(255) NOT NULL,
  `mimeType` varchar(255) NOT NULL,
  `content` longblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tecsecuser`
--

DROP TABLE IF EXISTS `tecsecuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tecsecuser` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `type` int(10) DEFAULT NULL,
  `addDate` datetime NOT NULL,
  `lastUpdate` datetime NOT NULL,
  `isActive` tinyint(4) NOT NULL,
  `email` varchar(60) DEFAULT NULL,
  `login` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `idContent` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `IX_TecSecUser` (`login`),
  KEY `FK_TecSecUser_TecGenBinaryContent` (`idContent`),
  CONSTRAINT `FK_TecSecUser_TecGenBinaryContent` FOREIGN KEY (`idContent`) REFERENCES `tecgenbinarycontent` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

--
-- Table structure for table `tecsecgroup`
--

DROP TABLE IF EXISTS `tecsecgroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tecsecgroup` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `idMaster` int(10) DEFAULT NULL,
  `addDate` datetime NOT NULL,
  `lastUpdate` datetime NOT NULL,
  `isActive` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TecSecGroup_&_TecSecGroup` (`idMaster`),
  CONSTRAINT `FK_TecSecGroup_&_TecSecGroup` FOREIGN KEY (`idMaster`) REFERENCES `tecsecgroup` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tecsecmodule`
--

DROP TABLE IF EXISTS `tecsecmodule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tecsecmodule` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `keyName` varchar(255) NOT NULL,
  `addDate` datetime NOT NULL,
  `lastUpdate` datetime NOT NULL,
  `version` char(5) NOT NULL DEFAULT '0.0.1',
  `isActive` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_TecSecModule` (`keyName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tecseccontrol`
--

DROP TABLE IF EXISTS `tecseccontrol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tecseccontrol` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `idModule` int(10) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `keyName` varchar(255) NOT NULL,
  `addDate` datetime NOT NULL,
  `lastUpdate` datetime NOT NULL,
  `isActive` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_TecSecControl` (`keyName`),
  KEY `FK_TecSecControl_&_TecSecModule` (`idModule`),
  CONSTRAINT `FK_TecSecControl_&_TecSecModule` FOREIGN KEY (`idModule`) REFERENCES `tecsecmodule` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tecsecaction`
--

DROP TABLE IF EXISTS `tecsecaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tecsecaction` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `idControl` int(10) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `keyName` varchar(255) NOT NULL,
  `addDate` datetime NOT NULL,
  `lastUpdate` datetime NOT NULL,
  `isActive` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_TecSecAction` (`keyName`),
  KEY `FK_TecSecAction_&_TecSecControl` (`idControl`),
  CONSTRAINT `FK_TecSecAction_&_TecSecControl` FOREIGN KEY (`idControl`) REFERENCES `tecseccontrol` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tecsecactiongroup`
--

DROP TABLE IF EXISTS `tecsecactiongroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tecsecactiongroup` (
  `idAction` int(10) NOT NULL,
  `idGroup` int(10) NOT NULL,
  PRIMARY KEY (`idAction`,`idGroup`),
  KEY `FK_TecActionGroup_&_TecGroup` (`idGroup`),
  CONSTRAINT `FK_TecSecActionGroup_&_TecSecAction` FOREIGN KEY (`idAction`) REFERENCES `tecsecaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_TecActionGroup_&_TecGroup` FOREIGN KEY (`idGroup`) REFERENCES `tecsecgroup` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tecsecactionuser`
--

DROP TABLE IF EXISTS `tecsecactionuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tecsecactionuser` (
  `idAction` int(10) NOT NULL,
  `idUser` int(10) NOT NULL,
  PRIMARY KEY (`idAction`,`idUser`),
  KEY `FK_TecSecActionUser_&_TecSecUser` (`idUser`),
  CONSTRAINT `FK_TecSecActionUser_&_TecSecAction` FOREIGN KEY (`idAction`) REFERENCES `tecsecaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_TecSecActionUser_&_TecSecUser` FOREIGN KEY (`idUser`) REFERENCES `tecsecuser` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tecsecgroupuser`
--

DROP TABLE IF EXISTS `tecsecgroupuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tecsecgroupuser` (
  `idGroup` int(10) NOT NULL,
  `idUser` int(10) NOT NULL,
  PRIMARY KEY (`idGroup`,`idUser`),
  KEY `FK_TecSecGroupUser_&_TecSecUser` (`idUser`),
  CONSTRAINT `FK_TecSecGroupUser_&_TecSecGroup` FOREIGN KEY (`idGroup`) REFERENCES `tecsecgroup` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_TecSecGroupUser_&_TecSecUser` FOREIGN KEY (`idUser`) REFERENCES `tecsecuser` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tecseclog`
--

DROP TABLE IF EXISTS `tecseclog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tecseclog` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `idUser` int(10) NOT NULL,
  `idAction` int(10) NOT NULL,
  `dateLog` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TecSecLog_&_TecSecAction` (`idAction`),
  KEY `FK_TecSecLog_&_TecSecUser` (`idUser`),
  CONSTRAINT `FK_TecSecLog_&_TecSecAction` FOREIGN KEY (`idAction`) REFERENCES `tecsecaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_TecSecLog_&_TecSecUser` FOREIGN KEY (`idUser`) REFERENCES `tecsecuser` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tecseclogcontent`
--

DROP TABLE IF EXISTS `tecseclogcontent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tecseclogcontent` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `idLog` int(10) NOT NULL,
  `field` varchar(30) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TecSecLogContent_&_TecSecLog` (`idLog`),
  CONSTRAINT `FK_TecSecLogContent_&_TecSecLog` FOREIGN KEY (`idLog`) REFERENCES `tecseclog` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


/*****************************************/


#MYSQL
INSERT INTO TecSecUser
           (name
           ,description
           ,type
           ,addDate
           ,lastUpdate
           ,isActive
           ,email
           ,login
           ,password)
     VALUES
           ("Marcelo tecgenbinarycontentcontents_locationMalcher"
           ,"This is the superadministrator and cannot be deleted."
           ,2 -- SUPERADMINISTRATOR
           ,CURDATE()
           ,CURDATE()
           ,1
           ,"marcelom@inf.puc-rio.br"
           ,"marcelom"
           ,"ZXVxfnx5" -- 102030
);
           

/*****************************************/

DELETE FROM scheduled_analysis_jobs;

DELETE FROM scheduled_analysis_properties;

DELETE FROM scheduled_analysis;

DELETE FROM analysis_execution_file;

DELETE FROM binary_content;

alter table analysis_execution_file
drop FOREIGN KEY FK_FILE_BINARY_CONTENT;

alter table analysis_execution_file
add constraint FK_FILE_BINARY_CONTENT foreign key(idBinaryContent) references tecgenbinarycontent(id);

-- alter table analysis_execution_file
-- drop FOREIGN KEY analysis_execution_file_ibfk_*;

/*****************************************/


-- Trigger DDL Statements
DELIMITER $$

CREATE
DEFINER=`root`@`localhost`
TRIGGER `wishdb`.`before_insert_content`
BEFORE INSERT ON `wishdb`.`contents`
FOR EACH ROW
BEGIN 
  
  DECLARE lat DOUBLE default 0.0;
  DECLARE lon DOUBLE default 0.0;
  DECLARE locId INT default 0;

  if (new.location_latitude IS NOT NULL) AND (new.location_latitude > 0.0) 
     AND (new.location_longitude IS NOT NULL) AND (new.location_longitude > 0.0) THEN
    
    SET lat = ROUND(new.location_latitude, 3);
    SET lon = ROUND(new.location_longitude, 3);
    
    SET locId = (SELECT id FROM contents_location WHERE latitude = lat AND longitude = lon);

    IF (locId IS NULL) OR (locId <= 0) THEN
	  INSERT INTO contents_location(latitude, longitude)
      VALUES(lat, lon);
      SET locId = (SELECT LAST_INSERT_ID());
    END IF;

    SET NEW.locationId = locId;
  END IF;
END$$

/************************************************/