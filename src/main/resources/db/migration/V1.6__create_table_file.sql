create table file_key (
    `id` bigint not null auto_increment primary key,
    `s3_base_url` varchar(255) not null,
    `path` varchar(255) not null,
    `previous_version_id` bigint,
    constraint `fk_file_previous_version_id` foreign key (`previous_version_id`) references file_key (`id`)
);

create table `student_cv` (
    `file_id` bigint not null,
    `student_mail` varchar(255) not null
);


alter table `internship`
    add column `sgk_document_id` bigint,
    add constraint `fk_internship_sgk_document_id` foreign key (`sgk_document_id`) references file_key (`id`);

