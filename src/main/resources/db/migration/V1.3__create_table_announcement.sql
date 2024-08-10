create table `announcement`
(
    `id` bigint not null auto_increment primary key,
    `title` varchar(255),
    `start_date` datetime(6),
    `end_date` datetime(6),
    `details` text,
    `company_id` bigint,
    constraint `fk_announcement_company_id` foreign key (`company_id`) references `company` (`id`)
);