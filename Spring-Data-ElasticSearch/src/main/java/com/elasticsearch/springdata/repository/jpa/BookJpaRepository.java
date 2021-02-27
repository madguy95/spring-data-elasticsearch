package com.elasticsearch.springdata.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elasticsearch.springdata.model.entity.BookEntity;

@Repository
public interface BookJpaRepository extends JpaRepository<BookEntity, String> {

    List<BookEntity> findByAuthorName(String authorName);

    Optional<BookEntity> findByIsbn(String isbn);
}