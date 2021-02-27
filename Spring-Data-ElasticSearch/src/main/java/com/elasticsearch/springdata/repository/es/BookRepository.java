package com.elasticsearch.springdata.repository.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.elasticsearch.springdata.model.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, String> {

    List<Book> findByAuthorName(String authorName);

    Optional<Book> findByIsbn(String isbn);
}