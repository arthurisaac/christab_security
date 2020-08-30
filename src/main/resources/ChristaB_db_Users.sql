-- phpMyAdmin SQL Dump
-- version 4.9.3
-- https://www.phpmyadmin.net/
--
-- Generation Time: Aug 29, 2020 at 06:53 PM
-- Server version: 5.7.26
-- PHP Version: 7.4.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `christaB_db_Users`
--

-- --------------------------------------------------------

--
-- Table structure for table `app_role`
--

CREATE TABLE `app_role` (
  `id_app_role` bigint(20) NOT NULL,
  `rolename` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `app_role`
--

INSERT INTO `app_role` (`id_app_role`, `rolename`) VALUES
(1, 'ADMIN'),
(2, 'USER');

-- --------------------------------------------------------

--
-- Table structure for table `app_user`
--

CREATE TABLE `app_user` (
  `id_app_user` bigint(20) NOT NULL,
  `activated` bit(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `limit_password_time` datetime DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `password_forget` int(11) DEFAULT NULL,
  `validated` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `app_user`
--

INSERT INTO `app_user` (`id_app_user`, `activated`, `email`, `limit_password_time`, `password`, `password_forget`, `validated`) VALUES
(1, b'1', 'wendyataps@gmail.com', '2020-07-10 12:10:09', '$2a$10$lug8zcjygmI1Tlz5X96duOAJFIIZmYo/CGoSiG7GIQ18kQCT5rWCW', 195569, b'1'),
(2, b'1', 'dorisflora6@gmail.com', NULL, '$2a$10$E0EoYMsVP5vbD9LLWyNBPuNUAdhMc314r4KjiAsezR91kctN6lfMm', 0, b'1'),
(4, b'1', 'eakakpovi656@gmail.com', NULL, '$2a$10$FlmnAIdiclBDnDslIKXBVOZA4KNuzPUdKY3aaD0.BTv7DCkD54ctS', 0, b'1');

-- --------------------------------------------------------

--
-- Table structure for table `app_user_roles`
--

CREATE TABLE `app_user_roles` (
  `app_user_id_app_user` bigint(20) NOT NULL,
  `roles_id_app_role` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `app_user_roles`
--

INSERT INTO `app_user_roles` (`app_user_id_app_user`, `roles_id_app_role`) VALUES
(1, 1),
(1, 2),
(2, 2),
(4, 2);

-- --------------------------------------------------------

--
-- Table structure for table `confirmation_token`
--

CREATE TABLE `confirmation_token` (
  `token_id` bigint(20) NOT NULL,
  `confirmation_token` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `id_app_user` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `confirmation_token`
--

INSERT INTO `confirmation_token` (`token_id`, `confirmation_token`, `created_date`, `id_app_user`) VALUES
(1, '0a80b689-46da-4c06-aa98-28d35347311a', '2020-07-07 16:37:40', 2),
(2, '59ad40f1-0132-450d-ada7-b4b0692c986f', '2020-08-28 18:53:26', 4);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `app_role`
--
ALTER TABLE `app_role`
  ADD PRIMARY KEY (`id_app_role`);

--
-- Indexes for table `app_user`
--
ALTER TABLE `app_user`
  ADD PRIMARY KEY (`id_app_user`);

--
-- Indexes for table `app_user_roles`
--
ALTER TABLE `app_user_roles`
  ADD PRIMARY KEY (`app_user_id_app_user`,`roles_id_app_role`),
  ADD KEY `FK6tdo87hk6tcfsltqjbis8be11` (`roles_id_app_role`);

--
-- Indexes for table `confirmation_token`
--
ALTER TABLE `confirmation_token`
  ADD PRIMARY KEY (`token_id`),
  ADD KEY `FK8ys0trv04btpmefqt17ib5dlt` (`id_app_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `app_role`
--
ALTER TABLE `app_role`
  MODIFY `id_app_role` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `app_user`
--
ALTER TABLE `app_user`
  MODIFY `id_app_user` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `confirmation_token`
--
ALTER TABLE `confirmation_token`
  MODIFY `token_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `app_user_roles`
--
ALTER TABLE `app_user_roles`
  ADD CONSTRAINT `FK6tdo87hk6tcfsltqjbis8be11` FOREIGN KEY (`roles_id_app_role`) REFERENCES `app_role` (`id_app_role`),
  ADD CONSTRAINT `FKsjji8qc3u3ws5sxse9bpy9l49` FOREIGN KEY (`app_user_id_app_user`) REFERENCES `app_user` (`id_app_user`);

--
-- Constraints for table `confirmation_token`
--
ALTER TABLE `confirmation_token`
  ADD CONSTRAINT `FK8ys0trv04btpmefqt17ib5dlt` FOREIGN KEY (`id_app_user`) REFERENCES `app_user` (`id_app_user`);
