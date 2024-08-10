
alter table `internship`
    add column `letter_document_id` bigint,
    add constraint `fk_internship_letter_document_id` foreign key (`letter_document_id`) references file_key (`id`),
    add column `application_form_id` bigint,
    add constraint `fk_internship_application_form_id` foreign key (`application_form_id`) references file_key (`id`);

