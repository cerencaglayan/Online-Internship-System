package com.g7.spec;

import com.g7.model.Announcement;
import org.springframework.data.jpa.domain.Specification;

public class AnnouncementSpec {
    public static Specification<Announcement> byActive() {
        return (root, query, cb) -> cb.equal(root.get("isActive"), true);
    }
}
