create table `internship`
(
    `id` bigint not null auto_increment primary key ,
    `student_mail` varchar(255) not null ,
    `status` enum('STUDENT_APPLIED','STUDENT_ACCEPTED', 'STUDENT_REJECTED','STUDENT_CANCELLED','STARTED'),
    `company_id` bigint not null,
    `application_date` datetime(6),
    `start_date` datetime(6),
    `end_date` datetime(6),
    constraint `fk_internship_company_id` foreign key (`company_id`) references `company` (`id`)
);