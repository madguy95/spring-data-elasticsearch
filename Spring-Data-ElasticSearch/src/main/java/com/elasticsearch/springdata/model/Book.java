package com.elasticsearch.springdata.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "books", type = "book" )
@Setting(settingPath = "/elasticsearch/lower-ascii.json")
public class Book {

    @Id
    private String id;

    private Long bookId;

    @Field(analyzer = "folding", type = FieldType.Text)
	private String title;

    private int publicationYear;

    
    @MultiField(mainField = @Field(analyzer = "folding", type = FieldType.Text),
    		otherFields = @InnerField(type = FieldType.Keyword, suffix = "raw"))
    private String authorName;

    private String isbn;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
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
    
    public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
}
