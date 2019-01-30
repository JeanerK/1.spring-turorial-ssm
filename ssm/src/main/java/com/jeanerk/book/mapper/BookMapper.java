package com.jeanerk.book.mapper;

import com.jeanerk.book.dto.Book;

import java.util.List;

public interface BookMapper {

    int updateBook(Book book);

    int insertBook(Book book);

    List<Book> selectBooks(Book book);

    Book selectBookById(Long bookId);

    int deleteProductById(Long bookId);
}
