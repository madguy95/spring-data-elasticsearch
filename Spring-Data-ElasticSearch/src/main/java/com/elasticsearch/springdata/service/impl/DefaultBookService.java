package com.elasticsearch.springdata.service.impl;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elasticsearch.springdata.model.Book;
import com.elasticsearch.springdata.model.dto.BookDTO;
import com.elasticsearch.springdata.model.entity.BookEntity;
import com.elasticsearch.springdata.repository.es.BookRepository;
import com.elasticsearch.springdata.repository.jpa.BookJpaRepository;
import com.elasticsearch.springdata.service.BookService;
import com.elasticsearch.springdata.service.exception.BookNotFoundException;
import com.elasticsearch.springdata.service.exception.DuplicateIsbnException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultBookService implements BookService {

    private final BookRepository bookRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;

    private final BookJpaRepository jpaRepo;
    
    public DefaultBookService(BookRepository bookRepository, ElasticsearchTemplate elasticsearchTemplate, BookJpaRepository jpaRepo) {
        this.bookRepository = bookRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Optional<Book> getByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(book -> books.add(book));
        return books;
    }

    @Override
    public List<Book> findByAuthor(String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        BoolQueryBuilder criteria = QueryBuilders.boolQuery();
        criteria.should().addAll(Arrays.asList(
        		QueryBuilders.matchPhrasePrefixQuery("authorName", author),
        		QueryBuilders.matchPhrasePrefixQuery("title", title),
        		QueryBuilders.matchQuery("authorName", author),
        		QueryBuilders.matchQuery("title", title)));
        return elasticsearchTemplate.queryForList(new NativeSearchQueryBuilder().withQuery(criteria)
        		.withSort(SortBuilders.fieldSort("authorName.raw").order(SortOrder.DESC)).build(), Book.class);
    }

    @Override
    @Transactional
    public Book create(BookDTO book) throws DuplicateIsbnException {
    	BookEntity bookEntity = jpaRepo.save(BookDTO.transformEntity(book));
        if (!getByIsbn(book.getIsbn()).isPresent()) {
            return bookRepository.save(BookDTO.transformElastic(bookEntity));
        }
        throw new DuplicateIsbnException(String.format("The provided ISBN: %s already exists. Use update instead!", book.getIsbn()));
    }
    
    @Override
    @Transactional
    public Book create(Book book) throws DuplicateIsbnException {
        if (!getByIsbn(book.getIsbn()).isPresent()) {
            return bookRepository.save(book);
        }
        throw new DuplicateIsbnException(String.format("The provided ISBN: %s already exists. Use update instead!", book.getIsbn()));
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book update(String id, Book book) throws BookNotFoundException {
        Book oldBook = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("There is not book associated with the given id"));
        oldBook.setIsbn(book.getIsbn());
        oldBook.setAuthorName(book.getAuthorName());
        oldBook.setPublicationYear(book.getPublicationYear());
        oldBook.setTitle(book.getTitle());
        return bookRepository.save(oldBook);
    }
}
