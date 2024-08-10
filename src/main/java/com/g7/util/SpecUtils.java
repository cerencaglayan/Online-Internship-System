package com.g7.util;

import org.springframework.data.jpa.domain.Specification;

public class SpecUtils {

    public static <T> Specification<T> and(Specification<T> first, Specification<T> second) {
        if (first == null) {
            return second;
        } else {
            return first.and(second);
        }
    }
    @SafeVarargs
    public static <T> Specification<T> and(Specification<T>... specs) {
        Specification<T> result = null;
        for (Specification<T> spec : specs) {
            result = and(result, spec);
        }
        return result;
    }

    @SafeVarargs
    public static <T> Specification<T> or(Specification<T>... specs) {
        Specification<T> result = null;
        for (Specification<T> spec : specs) {
            result = or(result, spec);
        }
        return result;
    }

    public static <T> Specification<T> or(Specification<T> first, Specification<T> second) {
        if (first == null) {
            return second;
        } else {
            return first.or(second);
        }
    }
}
