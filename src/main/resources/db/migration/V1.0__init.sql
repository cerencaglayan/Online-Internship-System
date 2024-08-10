create table `company` (
    `id` bigint not null auto_increment primary key,
    `name` varchar(255) not null,
    `password` varchar(255) not null,
    `email` varchar(255) not null,
    `phone_number` varchar(255) not null,
    `address` varchar(255) not null,
    `industry` varchar(255) not null,
    `about` varchar(255) not null,
    `approval_status` boolean not null
);


create table `confirmation_token` (
    `id` bigint not null auto_increment primary key,
    `token` varchar(255) not null,
    `created_at` datetime(6) not null,
    `expires_at` datetime(6) not null,
    `company_id` bigint,
    `topic` varchar(255) not null,
    foreign key (`company_id`) references `company` (`id`)
);
