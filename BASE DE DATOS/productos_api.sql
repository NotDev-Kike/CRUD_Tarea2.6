-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 06-07-2025 a las 08:40:47
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `productos_api`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `api_keys`
--

CREATE TABLE `api_keys` (
  `id` int(11) NOT NULL,
  `clave` varchar(64) NOT NULL,
  `activo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `api_keys`
--

INSERT INTO `api_keys` (`id`, `clave`, `activo`) VALUES
(1, '1234567890abcdef1234567890abcdef', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `categoria` varchar(100) NOT NULL,
  `imagenProducto` longtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id`, `nombre`, `precio`, `categoria`, `imagenProducto`) VALUES
(1, 'Tiramisú Clásico', 5.50, 'Tiramisú', NULL),
(2, 'Tiramisú de Fresa', 6.00, 'Tiramisú', NULL),
(3, 'Cheesecake de Frutos Rojos', 7.00, 'Cheesecake', NULL),
(4, 'Cheesecake Clásico', 6.50, 'Cheesecake', NULL),
(5, 'Flan de Caramelo', 4.00, 'Flan', NULL),
(6, 'Flan de Coco', 4.50, 'Flan', NULL),
(7, 'Brownies con Nueces', 3.50, 'Brownies', NULL),
(8, 'Brownies de Chocolate Blanco', 3.80, 'Brownies', NULL),
(9, 'Panacota de Vainilla', 5.00, 'Panacota', NULL),
(10, 'Panacota con Salsa de Frambuesa', 5.50, 'Panacota', NULL),
(11, 'Tiramisú de Chocolate', 6.20, 'Tiramisú', NULL),
(12, 'Cheesecake de Mango', 7.20, 'Cheesecake', NULL),
(13, 'Flan Napolitano', 4.30, 'Flan', NULL),
(14, 'Brownies Triple Chocolate', 4.00, 'Brownies', NULL),
(15, 'Panacota de Café', 5.30, 'Panacota', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `correo` varchar(100) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `ha1` char(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `nombre`, `correo`, `contrasena`, `ha1`) VALUES
(1, 'enrique', 'enrique@gmail.com', '$2y$10$fBJ4nlMvUQfGaT7i4sFumuLCwNjGBW5HdrX5IjrJUX0ku5qE1uA7S', '552cec041054d27e7bea8b1ee9ff7e38'),
(10, 'admin', 'admin@gmail.com', '$2y$10$SDA/fUipYSk23Fs4vkIIYuAHDkFL.iDAspB9fhhWLO2uNG3eqLAPu', NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `api_keys`
--
ALTER TABLE `api_keys`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `clave` (`clave`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `api_keys`
--
ALTER TABLE `api_keys`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
