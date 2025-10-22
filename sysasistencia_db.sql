-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 14, 2025 at 01:49 PM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sysasistencia_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `upeu_accesos`
--

CREATE TABLE `upeu_accesos` (
  `id_acceso` bigint NOT NULL,
  `icono` varchar(60) NOT NULL,
  `nombre` varchar(60) NOT NULL,
  `url` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `upeu_accesos`
--

INSERT INTO `upeu_accesos` (`id_acceso`, `icono`, `nombre`, `url`) VALUES
(1, 'fa-users', 'Usuarios', '/usuarios'),
(2, 'fa-user-shield', 'Roles', '/roles'),
(3, 'fa-cog', 'Configuración', '/configuracion'),
(4, 'fa-clipboard-list', 'Matrículas', '/matriculas'),
(5, 'fa-file-excel', 'Importar Excel', '/matriculas/importar'),
(6, 'fa-building', 'Sedes', '/sedes'),
(7, 'fa-university', 'Facultades', '/facultades'),
(8, 'fa-graduation-cap', 'Programas', '/programas'),
(9, 'fa-chart-bar', 'Reportes', '/reportes'),
(10, 'fa-tachometer-alt', 'Dashboard Admin', '/dashboard/admin'),
(11, 'fa-chart-line', 'Dashboard Líder', '/dashboard/lider'),
(12, 'fa-chart-pie', 'Dashboard Integrante', '/dashboard/integrante');

-- --------------------------------------------------------

--
-- Table structure for table `upeu_acceso_rol`
--

CREATE TABLE `upeu_acceso_rol` (
  `rol_id` bigint NOT NULL,
  `acceso_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `upeu_acceso_rol`
--

INSERT INTO `upeu_acceso_rol` (`rol_id`, `acceso_id`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10),
(1, 11),
(1, 12),
(2, 4),
(2, 5),
(2, 6),
(2, 7),
(2, 8),
(2, 9),
(2, 10),
(3, 11),
(4, 12);

-- --------------------------------------------------------

--
-- Table structure for table `upeu_facultad`
--

CREATE TABLE `upeu_facultad` (
  `id_facultad` bigint NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `upeu_facultad`
--

INSERT INTO `upeu_facultad` (`id_facultad`, `descripcion`, `nombre`) VALUES
(1, 'Facultad de Ingeniería y Arquitectura', 'Facultad de Ingeniería y Arquitectura');

-- --------------------------------------------------------

--
-- Table structure for table `upeu_matricula`
--

CREATE TABLE `upeu_matricula` (
  `id_matricula` bigint NOT NULL,
  `ciclo` varchar(10) DEFAULT NULL,
  `estado` varchar(20) DEFAULT NULL,
  `fecha_matricula` datetime(6) DEFAULT NULL,
  `grupo` varchar(10) DEFAULT NULL,
  `modalidad_estudio` varchar(50) DEFAULT NULL,
  `modo_contrato` varchar(50) DEFAULT NULL,
  `facultad_id` bigint NOT NULL,
  `persona_id` bigint NOT NULL,
  `programa_id` bigint NOT NULL,
  `sede_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `upeu_persona`
--

CREATE TABLE `upeu_persona` (
  `id_persona` bigint NOT NULL,
  `celular` varchar(20) DEFAULT NULL,
  `codigo_estudiante` varchar(20) DEFAULT NULL,
  `correo` varchar(100) DEFAULT NULL,
  `correo_institucional` varchar(100) DEFAULT NULL,
  `documento` varchar(20) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `foto` varchar(500) DEFAULT NULL,
  `nombre_completo` varchar(200) NOT NULL,
  `pais` varchar(50) DEFAULT NULL,
  `religion` varchar(50) DEFAULT NULL,
  `tipo_persona` enum('ESTUDIANTE','INVITADO') NOT NULL,
  `usuario_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `upeu_programa_estudio`
--

CREATE TABLE `upeu_programa_estudio` (
  `id_programa` bigint NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `nombre` varchar(150) NOT NULL,
  `facultad_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `upeu_programa_estudio`
--

INSERT INTO `upeu_programa_estudio` (`id_programa`, `descripcion`, `nombre`, `facultad_id`) VALUES
(1, 'EP Ingeniería de Sistemas', 'EP Ingeniería de Sistemas', 1);

-- --------------------------------------------------------

--
-- Table structure for table `upeu_roles`
--

CREATE TABLE `upeu_roles` (
  `id_rol` bigint NOT NULL,
  `descripcion` varchar(120) NOT NULL,
  `nombre` enum('ADMIN','INTEGRANTE','LIDER','SUPERADMIN') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `upeu_roles`
--

INSERT INTO `upeu_roles` (`id_rol`, `descripcion`, `nombre`) VALUES
(1, 'Super Administrador - Acceso total al sistema', 'SUPERADMIN'),
(2, 'Administrador - Gestión de matrículas, sedes, facultades y programas', 'ADMIN'),
(3, 'Líder - Acceso a dashboard de líder', 'LIDER'),
(4, 'Integrante - Acceso a dashboard de integrante', 'INTEGRANTE');

-- --------------------------------------------------------

--
-- Table structure for table `upeu_sede`
--

CREATE TABLE `upeu_sede` (
  `id_sede` bigint NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `upeu_sede`
--

INSERT INTO `upeu_sede` (`id_sede`, `descripcion`, `nombre`) VALUES
(1, 'Filial Juliaca', 'Filial Juliaca');

-- --------------------------------------------------------

--
-- Table structure for table `upeu_usuario`
--

CREATE TABLE `upeu_usuario` (
  `id_usuario` bigint NOT NULL,
  `clave` varchar(100) NOT NULL,
  `estado` varchar(10) NOT NULL,
  `user` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `upeu_usuario`
--

INSERT INTO `upeu_usuario` (`id_usuario`, `clave`, `estado`, `user`) VALUES
(1, '$2a$10$y5oQKhjju2dV1Fk6uu4WO.nLo.pOVIQCdMHCS55KLRQk7j.f7wFw.', 'ACTIVO', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `upeu_usuario_rol`
--

CREATE TABLE `upeu_usuario_rol` (
  `rol_id` bigint NOT NULL,
  `usuario_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `upeu_usuario_rol`
--

INSERT INTO `upeu_usuario_rol` (`rol_id`, `usuario_id`) VALUES
(2, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `upeu_accesos`
--
ALTER TABLE `upeu_accesos`
  ADD PRIMARY KEY (`id_acceso`);

--
-- Indexes for table `upeu_acceso_rol`
--
ALTER TABLE `upeu_acceso_rol`
  ADD PRIMARY KEY (`acceso_id`,`rol_id`),
  ADD KEY `FK5olgwvkvi6gfmjp117cnq02cc` (`rol_id`);

--
-- Indexes for table `upeu_facultad`
--
ALTER TABLE `upeu_facultad`
  ADD PRIMARY KEY (`id_facultad`),
  ADD UNIQUE KEY `UKr4ltlc1rojwdr25ctjpo5ljen` (`nombre`);

--
-- Indexes for table `upeu_matricula`
--
ALTER TABLE `upeu_matricula`
  ADD PRIMARY KEY (`id_matricula`),
  ADD KEY `FK7awilfnvri2qwojcwrp6ahh42` (`facultad_id`),
  ADD KEY `FKrka3gnhkh2ndye2c4lv7ioxl8` (`persona_id`),
  ADD KEY `FKi3gcktb2mjnbi7uejv79g3eod` (`programa_id`),
  ADD KEY `FK4twl184v124wty2vhuek0lvp6` (`sede_id`);

--
-- Indexes for table `upeu_persona`
--
ALTER TABLE `upeu_persona`
  ADD PRIMARY KEY (`id_persona`),
  ADD UNIQUE KEY `UKlxoymbr59itrbf6sjaw9to5to` (`codigo_estudiante`),
  ADD UNIQUE KEY `UKaf0lk6c2hkk600sjp80y1j8it` (`documento`),
  ADD UNIQUE KEY `UK16e70r46fswii1afns4i9wvmq` (`usuario_id`);

--
-- Indexes for table `upeu_programa_estudio`
--
ALTER TABLE `upeu_programa_estudio`
  ADD PRIMARY KEY (`id_programa`),
  ADD UNIQUE KEY `UKopjcq007j3v306qek5xvbc3ac` (`nombre`),
  ADD KEY `FKsrkcs3278ksxs8of0vd4iwu8o` (`facultad_id`);

--
-- Indexes for table `upeu_roles`
--
ALTER TABLE `upeu_roles`
  ADD PRIMARY KEY (`id_rol`),
  ADD UNIQUE KEY `UK3hu3jg08dv5v4g9mhobni9kxm` (`nombre`);

--
-- Indexes for table `upeu_sede`
--
ALTER TABLE `upeu_sede`
  ADD PRIMARY KEY (`id_sede`),
  ADD UNIQUE KEY `UKrf48l6lcylhbvh4l3o7adv24b` (`nombre`);

--
-- Indexes for table `upeu_usuario`
--
ALTER TABLE `upeu_usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `UKnglinedpa99qfym8jreg27mx8` (`user`);

--
-- Indexes for table `upeu_usuario_rol`
--
ALTER TABLE `upeu_usuario_rol`
  ADD PRIMARY KEY (`rol_id`,`usuario_id`),
  ADD KEY `FKehuc4c37b7soxfqfexqh3kg6s` (`usuario_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `upeu_accesos`
--
ALTER TABLE `upeu_accesos`
  MODIFY `id_acceso` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `upeu_facultad`
--
ALTER TABLE `upeu_facultad`
  MODIFY `id_facultad` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `upeu_matricula`
--
ALTER TABLE `upeu_matricula`
  MODIFY `id_matricula` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `upeu_persona`
--
ALTER TABLE `upeu_persona`
  MODIFY `id_persona` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `upeu_programa_estudio`
--
ALTER TABLE `upeu_programa_estudio`
  MODIFY `id_programa` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `upeu_roles`
--
ALTER TABLE `upeu_roles`
  MODIFY `id_rol` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `upeu_sede`
--
ALTER TABLE `upeu_sede`
  MODIFY `id_sede` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `upeu_usuario`
--
ALTER TABLE `upeu_usuario`
  MODIFY `id_usuario` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `upeu_acceso_rol`
--
ALTER TABLE `upeu_acceso_rol`
  ADD CONSTRAINT `FK5olgwvkvi6gfmjp117cnq02cc` FOREIGN KEY (`rol_id`) REFERENCES `upeu_roles` (`id_rol`),
  ADD CONSTRAINT `FKpt947hoat26u0hbc4qmkrfwj2` FOREIGN KEY (`acceso_id`) REFERENCES `upeu_accesos` (`id_acceso`);

--
-- Constraints for table `upeu_matricula`
--
ALTER TABLE `upeu_matricula`
  ADD CONSTRAINT `FK4twl184v124wty2vhuek0lvp6` FOREIGN KEY (`sede_id`) REFERENCES `upeu_sede` (`id_sede`),
  ADD CONSTRAINT `FK7awilfnvri2qwojcwrp6ahh42` FOREIGN KEY (`facultad_id`) REFERENCES `upeu_facultad` (`id_facultad`),
  ADD CONSTRAINT `FKi3gcktb2mjnbi7uejv79g3eod` FOREIGN KEY (`programa_id`) REFERENCES `upeu_programa_estudio` (`id_programa`),
  ADD CONSTRAINT `FKrka3gnhkh2ndye2c4lv7ioxl8` FOREIGN KEY (`persona_id`) REFERENCES `upeu_persona` (`id_persona`);

--
-- Constraints for table `upeu_persona`
--
ALTER TABLE `upeu_persona`
  ADD CONSTRAINT `FKdcoevt6hru00vhxxuakg6yk89` FOREIGN KEY (`usuario_id`) REFERENCES `upeu_usuario` (`id_usuario`);

--
-- Constraints for table `upeu_programa_estudio`
--
ALTER TABLE `upeu_programa_estudio`
  ADD CONSTRAINT `FKsrkcs3278ksxs8of0vd4iwu8o` FOREIGN KEY (`facultad_id`) REFERENCES `upeu_facultad` (`id_facultad`);

--
-- Constraints for table `upeu_usuario_rol`
--
ALTER TABLE `upeu_usuario_rol`
  ADD CONSTRAINT `FK5i9y5d4yvar0uhcf6q50qpw9h` FOREIGN KEY (`rol_id`) REFERENCES `upeu_roles` (`id_rol`),
  ADD CONSTRAINT `FKehuc4c37b7soxfqfexqh3kg6s` FOREIGN KEY (`usuario_id`) REFERENCES `upeu_usuario` (`id_usuario`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
