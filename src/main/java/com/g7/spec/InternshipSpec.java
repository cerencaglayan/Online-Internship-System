package com.g7.spec;

import com.g7.model.Internship;
import org.springframework.data.jpa.domain.Specification;

public class InternshipSpec {
    public static Specification<Internship> byCompany(Long id) {
        if (id == null) return null;
        return (root, query, cb) -> cb.equal(root.get("company").get("id"), id);
    }

    public static Specification<Internship> byStudent(String email) {
        if (email == null ) return null;
        return (root, query, cb) -> cb.equal(cb.lower(root.get("studentMail")), email.toLowerCase());
    }
}
