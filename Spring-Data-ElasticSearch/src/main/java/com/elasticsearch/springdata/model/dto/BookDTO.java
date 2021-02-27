package com.elasticsearch.springdata.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.elasticsearch.springdata.metadata.PublicationYear;
import com.elasticsearch.springdata.model.Book;
import com.elasticsearch.springdata.model.entity.BookEntity;

public class BookDTO {

    @NotBlank
    private String title;

    @Positive
    @PublicationYear
    private Integer publicationYear;

    @NotBlank
    private String authorName;

    @NotBlank
    private String isbn;

    public static Book transform(BookDTO bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.title);
        book.setPublicationYear(bookDto.publicationYear);
        book.setAuthorName(bookDto.authorName);
        book.setIsbn(bookDto.isbn);
        return book;
    }
    
    public static Book transformElastic(BookEntity bookEntity) {
        Book book = new Book();
        book.setBookId(bookEntity.getId());
        book.setTitle(bookEntity.getTitle());
        book.setPublicationYear(bookEntity.getPublicationYear());
        book.setAuthorName(bookEntity.getAuthorName());
        book.setIsbn(bookEntity.getIsbn());
        return book;
    }
    
    public static BookEntity transformEntity(BookDTO bookDto) {
    	BookEntity book = new BookEntity();
        book.setTitle(bookDto.title);
        book.setPublicationYear(bookDto.publicationYear);
        book.setAuthorName(bookDto.authorName);
        book.setIsbn(bookDto.isbn);
        return book;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
