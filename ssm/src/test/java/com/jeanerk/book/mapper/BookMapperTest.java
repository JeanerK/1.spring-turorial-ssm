package com.jeanerk.book.mapper;

import com.jeanerk.book.dto.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext-test.xml"})
@Rollback
@Transactional
public class BookMapperTest {

    @Autowired
    private BookMapper bookMapper;

    @Test
    public void updateBook() {
        Book book = new Book("TypeScript入门", "廖雪峰", 33.7F);
        book.setBookId(1L);
        int result = bookMapper.updateBook(book);
        assertEquals(1, result);
    }

    @Test
    public void insertBook() {
        Book book = new Book("ES6标准入门", "阮一峰", 37.0F);
        int result = bookMapper.insertBook(book);
        assertEquals(1, result);
    }

    @Test
    public void selectBooks() {
        Book book = new Book(null, "阮一峰", null);
        List<Book> books = bookMapper.selectBooks(book);
        assertTrue("no books found.", !CollectionUtils.isEmpty(books));
    }

    @Test
    public void selectBookById() {
        Book book = bookMapper.selectBookById(2L);
        assertNotNull(book);
    }

    @Test
    public void deleteProductById() {
        int result = bookMapper.deleteProductById(1L);
        assertEquals(1, result);
    }
}