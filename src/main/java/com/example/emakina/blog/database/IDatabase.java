package com.example.emakina.blog.database;

import com.example.emakina.blog.exception.BlogModelExistsException;
import com.example.emakina.blog.exception.BlogModelNotFoundException;
import com.example.emakina.blog.model.BlogModel;

import java.util.List;

public interface IDatabase {
    List<BlogModel> getAllBlogs();

    BlogModel getBlogById(String id) throws BlogModelNotFoundException;

    void addBlog(BlogModel blogModel) throws BlogModelExistsException;

    void updateBlog(BlogModel blogModel) throws BlogModelNotFoundException;

    boolean removeBlog(String id);
}
