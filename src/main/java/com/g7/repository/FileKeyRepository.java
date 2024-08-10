package com.g7.repository;

import com.g7.model.FileKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FileKeyRepository extends JpaRepository<FileKey, Long>, JpaSpecificationExecutor<FileKey> {
}
