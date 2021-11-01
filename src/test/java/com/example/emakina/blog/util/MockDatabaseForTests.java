package com.example.emakina.blog.util;

import com.example.emakina.blog.exception.BlogModelNotFoundException;
import com.example.emakina.blog.model.BlogModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

public class MockDatabaseForTests {

    private final List<BlogModel> database = new ArrayList<>();

    public MockDatabaseForTests() {
        BlogModel emakinaBlog = new BlogModel().withId("1").withName("emakina").withUrl("https://jobs.emakina.rs/blog/");
        BlogModel springBlog = new BlogModel().withId("2").withName("spring").withUrl("https://spring.io/blog");
        database.add(emakinaBlog);
        database.add(springBlog);
    }

    public List<BlogModel> getAllBlogs() {
        return database;
    }

    public BlogModel getBlogById(String id) {
        Optional<BlogModel> blog = database.stream().filter(blogModel -> blogModel.getId().equals(id)).findFirst();
        if (blog.isEmpty()) {
            fail();
        }
        return blog.get();
    }
}
