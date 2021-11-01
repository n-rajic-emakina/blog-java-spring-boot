package com.example.emakina.blog.database.impl;

import com.example.emakina.blog.database.IDatabase;
import com.example.emakina.blog.exception.BlogModelExistsException;
import com.example.emakina.blog.exception.BlogModelNotFoundException;
import com.example.emakina.blog.model.BlogModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MockDatabaseImpl implements IDatabase {
    private final List<BlogModel> database = new ArrayList<>();

    public MockDatabaseImpl() {
        BlogModel emakinaBlog = new BlogModel().withId("1").withName("emakina").withUrl("https://jobs.emakina.rs/blog/");
        BlogModel springBlog = new BlogModel().withId("2").withName("spring").withUrl("https://spring.io/blog");
        BlogModel stackoverflowBlog = new BlogModel().withId("3").withName("stackoverflow").withUrl("https://stackoverflow.blog/");
        BlogModel oracleJavaBlog = new BlogModel().withId("4").withName("java").withUrl("https://blogs.oracle.com/java/");
        BlogModel apacheMavenBlog = new BlogModel().withId("5").withName("maven").withUrl("https://blogs.apache.org/maven/");
        database.add(emakinaBlog);
        database.add(springBlog);
        database.add(stackoverflowBlog);
        database.add(oracleJavaBlog);
        database.add(apacheMavenBlog);
    }

    @Override
    public List<BlogModel> getAllBlogs() {
        return database;
    }

    @Override
    public BlogModel getBlogById(String id) throws BlogModelNotFoundException {
        Optional<BlogModel> blog = database.stream().filter(blogModel -> blogModel.getId().equals(id)).findFirst();
        if (blog.isEmpty()) {
            throw new BlogModelNotFoundException("Model is not found by given ID");
        }
        return blog.get();
    }

    @Override
    public void addBlog(BlogModel newBlogModel) throws BlogModelExistsException {
        Optional<BlogModel> foundBlog = database.stream().filter(blog -> newBlogModel.getId().equals(blog.getId())).findFirst();
        if (foundBlog.isPresent()) {
            throw new BlogModelExistsException("Model with given ID already exist");
        }
        database.add(newBlogModel);
    }

    @Override
    public void updateBlog(BlogModel newBlogModel) throws BlogModelNotFoundException {
        Optional<BlogModel> foundBlog = database.stream().filter(blog -> newBlogModel.getId().equals(blog.getId())).findFirst();
        if (foundBlog.isEmpty()) {
            throw new BlogModelNotFoundException("Model is not found by given ID");
        }
        foundBlog.get().setUrl(newBlogModel.getUrl());
        foundBlog.get().setName(newBlogModel.getName());
    }

    @Override
    public boolean removeBlog(String id) {
        Optional<BlogModel> blog = database.stream().filter(blogModel -> blogModel.getId().equals(id)).findFirst();
        if (blog.isEmpty()) {
            return false;
        }
        return database.remove(blog.get());
    }
}
