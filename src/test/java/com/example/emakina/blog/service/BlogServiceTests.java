package com.example.emakina.blog.service;

import com.example.emakina.blog.database.IDatabase;
import com.example.emakina.blog.exception.BlogModelExistsException;
import com.example.emakina.blog.exception.BlogModelNotFoundException;
import com.example.emakina.blog.model.BlogModel;
import com.example.emakina.blog.service.impl.BlogServiceImpl;
import com.example.emakina.blog.util.MockDatabaseForTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogServiceTests {

    @Mock
    IDatabase database;

    final MockDatabaseForTests mockDatabaseForTests = new MockDatabaseForTests();

    @Test
    void shouldGetAllBlogs() {
        IBlogService blogService = new BlogServiceImpl(database);
        when(database.getAllBlogs()).thenReturn(mockDatabaseForTests.getAllBlogs());
        List<BlogModel> allBlogs = blogService.getAllBlogs();
        assertFalse(allBlogs.isEmpty());
        assertEquals("1", allBlogs.get(0).getId());
        assertEquals("emakina", allBlogs.get(0).getName());
        assertEquals("https://jobs.emakina.rs/blog/", allBlogs.get(0).getUrl());
        assertEquals("2", allBlogs.get(1).getId());
        assertEquals("spring", allBlogs.get(1).getName());
        assertEquals("https://spring.io/blog", allBlogs.get(1).getUrl());
    }

    @Test
    void shouldGetBlogByID() throws BlogModelNotFoundException {
        IBlogService blogService = new BlogServiceImpl(database);
        when(database.getBlogById("1")).thenReturn(mockDatabaseForTests.getBlogById("1"));
        BlogModel firstBlog = blogService.getBlogById("1");
        assertNotNull(firstBlog);
        assertEquals("1", firstBlog.getId());
        assertEquals("emakina", firstBlog.getName());
        assertEquals("https://jobs.emakina.rs/blog/", firstBlog.getUrl());
    }

    @Test
    void shouldThrowNotFoundException() throws BlogModelNotFoundException {
        IBlogService blogService = new BlogServiceImpl(database);
        when(database.getBlogById("1")).thenThrow(new BlogModelNotFoundException("test message from shouldThrowNotFoundException"));
        BlogModelNotFoundException blogModelNotFoundException = assertThrows(BlogModelNotFoundException.class, () -> blogService.getBlogById("1"));
        assertEquals("test message from shouldThrowNotFoundException", blogModelNotFoundException.getMessage());
    }

    @Test
    void shouldAddBlogToDatabase() throws BlogModelExistsException {
        IBlogService blogService = new BlogServiceImpl(database);
        doNothing().when(database).addBlog(any());
        blogService.addBlog(new BlogModel());
        verify(database, times(1)).addBlog(any());
    }

    @Test
    void shouldThrowAlreadyExistException() throws BlogModelExistsException {
        IBlogService blogService = new BlogServiceImpl(database);
        doThrow(new BlogModelExistsException("test message from shouldThrowAlreadyExistException")).when(database).addBlog(any());
        BlogModelExistsException blogModelExistsException = assertThrows(BlogModelExistsException.class, () -> blogService.addBlog(new BlogModel()));
        assertEquals("test message from shouldThrowAlreadyExistException", blogModelExistsException.getMessage());
    }

    @Test
    void shouldUpdateBlogInDatabase() throws BlogModelNotFoundException {
        IBlogService blogService = new BlogServiceImpl(database);
        doNothing().when(database).updateBlog(any());
        blogService.updateBlog(new BlogModel());
        verify(database, times(1)).updateBlog(any());
    }

    @Test
    void shouldThrowNotExistForUpdateException() throws BlogModelNotFoundException {
        IBlogService blogService = new BlogServiceImpl(database);
        doThrow(new BlogModelNotFoundException("test message from shouldThrowNotExistForUpdateException")).when(database).updateBlog(any());
        BlogModelNotFoundException blogModelNotFoundException = assertThrows(BlogModelNotFoundException.class, () -> blogService.updateBlog(new BlogModel()));
        assertEquals("test message from shouldThrowNotExistForUpdateException", blogModelNotFoundException.getMessage());
    }

    @Test
    void shouldReturnTrueForRemovingBlog() {
        IBlogService blogService = new BlogServiceImpl(database);
        when(database.removeBlog("1")).thenReturn(true);
        boolean removeBlog = blogService.removeBlog("1");
        assertTrue(removeBlog);
    }

    @Test
    void shouldReturnFalseForNotRemovingBlog() {
        IBlogService blogService = new BlogServiceImpl(database);
        when(database.removeBlog("1")).thenReturn(false);
        boolean removeBlog = blogService.removeBlog("1");
        assertFalse(removeBlog);
    }
}
