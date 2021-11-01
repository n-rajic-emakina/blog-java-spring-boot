package com.example.emakina.blog.service;

import com.example.emakina.blog.exception.BlogModelExistsException;
import com.example.emakina.blog.exception.BlogModelNotFoundException;
import com.example.emakina.blog.model.BlogModel;

import java.util.List;

public interface IBlogService {
    List<BlogModel> getAllBlogs();

    BlogModel getBlogById(String id) throws BlogModelNotFoundException;

    void addBlog(BlogModel blogModel) throws BlogModelExistsException;

    void updateBlog(BlogModel blogModel) throws BlogModelNotFoundException;

    boolean removeBlog(String id);
}
