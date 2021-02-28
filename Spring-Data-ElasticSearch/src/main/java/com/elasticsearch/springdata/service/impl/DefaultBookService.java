package com.elasticsearch.springdata.service.impl;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultBookService implements BookService {

    private final BookRepository bookRepository;

    private final BookJpaRepository jpaRepo;
    
    private final RestHighLevelClient restClient;
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    public DefaultBookService(BookRepository bookRepository, RestHighLevelClient restClient, BookJpaRepository jpaRepo) {
        this.bookRepository = bookRepository;
        this.restClient = restClient;
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
        criteria.should().addAll(Arrays.asList(QueryBuilders.matchPhrasePrefixQuery("authorName", author), 
        		QueryBuilders.matchPhrasePrefixQuery("title", title),
        		QueryBuilders.matchQuery("authorName", author), 
        		QueryBuilders.matchQuery("title", title)));
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(criteria);

        String[] strings = new String[]{"books"};
        SearchRequest request = new SearchRequest(strings, builder);
        SearchResponse sr = null;
		try {
			sr = restClient.search(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
        SearchHit[] results = sr.getHits().getHits();
        List<Book> list = new ArrayList<Book>();
        for(SearchHit hit : results){
            String sourceAsString = hit.getSourceAsString();
            if (sourceAsString != null) {
            	try {
            		list.add(mapper.readValue(sourceAsString, Book.class));
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        return list;
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