package com.example.emakina.blog.service.impl;

import com.example.emakina.blog.database.IDatabase;
import com.example.emakina.blog.exception.BlogModelExistsException;
import com.example.emakina.blog.exception.BlogModelNotFoundException;
import com.example.emakina.blog.model.BlogModel;
import com.example.emakina.blog.service.IBlogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements IBlogService {

    private final IDatabase database;

    public BlogServiceImpl(IDatabase database) {
        this.database = database;
    }

    @Override
    public List<BlogModel> getAllBlogs() {
        return database.getAllBlogs();
    }

    @Override
    public BlogModel getBlogById(String id) throws BlogModelNotFoundException {
        return database.getBlogById(id);
    }

    @Override
    public void addBlog(BlogModel blogModel) throws BlogModelExistsException {
        database.addBlog(blogModel);
    }

    @Override
    public void updateBlog(BlogModel blogModel) throws BlogModelNotFoundException {
        database.updateBlog(blogModel);
    }

    @Override
    public boolean removeBlog(String id) {
        return database.removeBlog(id);
    }
}
