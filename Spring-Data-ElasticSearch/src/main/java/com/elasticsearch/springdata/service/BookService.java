package com.elasticsearch.springdata.service;

import java.util.List;
import java.util.Optional;

import com.elasticsearch.springdata.model.Book;
import com.elasticsearch.springdata.model.dto.BookDTO;
import com.elasticsearch.springdata.service.exception.BookNotFoundException;
import com.elasticsearch.springdata.service.exception.DuplicateIsbnException;

public interface BookService {

    Optional<Book> getByIsbn(String isbn);

    List<Book> getAll();

    List<Book> findByAuthor(String authorName);

    List<Book> findByTitleAndAuthor(String title, String author);

    Book create(BookDTO book) throws DuplicateIsbnException;
    
    Book create(Book book) throws DuplicateIsbnException;

    void deleteById(String id);

    Book update(String id, Book book) throws BookNotFoundException;
}
