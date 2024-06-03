CREATE TABLE `document_type` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(255) not null UNIQUE,
  `mime_type` varchar(255) not null
);

CREATE TABLE `document` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `created_at` datetime not null DEFAULT now(),
  `title` varchar(255) not null,
  `hash` varchar(255) not null UNIQUE,
  `path` varchar(2048) not null,
  `type_id` int not null
);

CREATE TABLE `privilege` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(255) not null UNIQUE
);

CREATE TABLE `role` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(255) not null UNIQUE
);

CREATE TABLE `privilege_role` (
  `role_id` int not null,
  `privilege_id` int not null,
  PRIMARY KEY (`role_id`, `privilege_id`)
);

CREATE TABLE `account` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `created_at` datetime not null DEFAULT now(),
  `username` varchar(255) not null UNIQUE,
  `password` varchar(255) not null,
  `avatar_id` int not null
);

CREATE TABLE `role_account` (
  `account_id` int not null,
  `role_id` int not null,
  PRIMARY KEY (`account_id`, `role_id`)
);

CREATE TABLE `record` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `created_at` datetime not null DEFAULT now(),
  `title` varchar(255) not null,
  `account_id` int not null,
  `duration` bigint not null,
  `accuracy` decimal(5,2),
  `voice_record_id` int not null,
  `track_id` int
);

CREATE TABLE `record_item` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `record_id` int not null,
  `time` bigint not null,
  `frequency` decimal(7,2) not null,
  `track_frequency` decimal(7,2)
);

CREATE TABLE `publication_tag` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(255) not null UNIQUE
);

CREATE TABLE `publication` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `created_at` datetime not null DEFAULT now(),
  `account_id` int not null,
  `description` text not null,
  `record_id` int not null
);

CREATE TABLE `publication_tags` (
  `publication_id` int not null,
  `tag_id` int not null,
  PRIMARY KEY (`publication_id`, `tag_id`)
);

ALTER TABLE `document` ADD FOREIGN KEY (`type_id`) REFERENCES `document_type` (`id`);

ALTER TABLE `privilege_role` ADD FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);

ALTER TABLE `privilege_role` ADD FOREIGN KEY (`privilege_id`) REFERENCES `privilege` (`id`);

ALTER TABLE `account` ADD FOREIGN KEY (`avatar_id`) REFERENCES `document` (`id`);

ALTER TABLE `role_account` ADD FOREIGN KEY (`account_id`) REFERENCES `account` (`id`);

ALTER TABLE `role_account` ADD FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);

ALTER TABLE `record` ADD FOREIGN KEY (`account_id`) REFERENCES `account` (`id`);

ALTER TABLE `record` ADD FOREIGN KEY (`voice_record_id`) REFERENCES `document` (`id`);

ALTER TABLE `record` ADD FOREIGN KEY (`track_id`) REFERENCES `document` (`id`);

ALTER TABLE `record_item` ADD FOREIGN KEY (`record_id`) REFERENCES `record` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT;

ALTER TABLE `publication` ADD FOREIGN KEY (`account_id`) REFERENCES `account` (`id`);

ALTER TABLE `publication` ADD FOREIGN KEY (`record_id`) REFERENCES `record` (`id`);

ALTER TABLE `publication_tags` ADD FOREIGN KEY (`publication_id`) REFERENCES `publication` (`id`);

ALTER TABLE `publication_tags` ADD FOREIGN KEY (`tag_id`) REFERENCES `publication_tag` (`id`);
