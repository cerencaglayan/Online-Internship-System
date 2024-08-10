alter table `internship`
    add column `announcement_id` bigint,
    add constraint `fk_internship_announcement` foreign key (`announcement_id`) references `announcement` (`id`);