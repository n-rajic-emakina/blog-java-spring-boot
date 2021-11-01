package com.example.emakina.blog.controller;

import com.example.emakina.blog.exception.BlogModelExistsException;
import com.example.emakina.blog.exception.BlogModelNotFoundException;
import com.example.emakina.blog.model.BlogModel;
import com.example.emakina.blog.service.IBlogService;
import com.example.emakina.blog.util.MockDatabaseForTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlogController.class)
class BlogControllerTests {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    IBlogService blogService;

    final ObjectMapper objectMapper = new ObjectMapper();
    final MockDatabaseForTests mockDatabase = new MockDatabaseForTests();

    @Test
    void shouldReturnBlogsFromDatabase() throws Exception {
        when(blogService.getAllBlogs()).thenReturn(mockDatabase.getAllBlogs());
        this.mockMvc.perform(get("/blog/"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockDatabase.getAllBlogs())));
    }

    @Test
    void shouldReturnEmptyList() throws Exception {
        when(blogService.getAllBlogs()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/blog/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldReturnBlogWithGivenID() throws Exception {
        BlogModel blogById = mockDatabase.getBlogById("1");
        when(blogService.getBlogById("1")).thenReturn(blogById);
        this.mockMvc.perform(get("/blog/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(blogById)));
    }

    @Test
    void shouldReturnErrorForNotFoundException() throws Exception {
        when(blogService.getBlogById("1")).thenThrow(new BlogModelNotFoundException("test message from shouldReturnErrorForNotFoundException"));
        this.mockMvc.perform(get("/blog/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"test message from shouldReturnErrorForNotFoundException\",\"code\":404}"));
    }

    @Test
    void shouldAddBlogToDatabase() throws Exception {
        doNothing().when(blogService).addBlog(any());
        this.mockMvc.perform(put("/blog/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new BlogModel())))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnErrorForAlreadyExistException() throws Exception {
        doThrow(new BlogModelExistsException("test message from shouldReturnErrorForAlreadyExistException")).when(blogService).addBlog(any());
        this.mockMvc.perform(put("/blog/add").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new BlogModel())))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"test message from shouldReturnErrorForAlreadyExistException\",\"code\":400}"));
    }

    @Test
    void shouldUpdateBlogInDatabase() throws Exception {
        doNothing().when(blogService).updateBlog(any());
        this.mockMvc.perform(post("/blog/update").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new BlogModel())))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnErrorForNotFoundForUpdateException() throws Exception {
        doThrow(new BlogModelNotFoundException("test message from shouldReturnErrorForNotFoundForUpdateException")).when(blogService).updateBlog(any());
        this.mockMvc.perform(post("/blog/update").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new BlogModel())))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"test message from shouldReturnErrorForNotFoundForUpdateException\",\"code\":404}"));
    }

    @Test
    void shouldRemoveBlogInDatabase() throws Exception {
        when(blogService.removeBlog("1")).thenReturn(true);
        this.mockMvc.perform(delete("/blog/remove/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnErrorForNotRemovingBlogFromDatabase() throws Exception {
        when(blogService.removeBlog("1")).thenReturn(false);
        this.mockMvc.perform(delete("/blog/remove/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
}
