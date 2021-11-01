package com.example.emakina.blog.controller;

import com.example.emakina.blog.exception.BlogModelExistsException;
import com.example.emakina.blog.exception.BlogModelNotFoundException;
import com.example.emakina.blog.model.BlogModel;
import com.example.emakina.blog.model.ErrorResponse;
import com.example.emakina.blog.service.IBlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "blog", produces = MediaType.APPLICATION_JSON_VALUE)
public class BlogController {

    private final IBlogService blogService;

    public BlogController(IBlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/")
    public List<BlogModel> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @GetMapping("/{id}")
    public BlogModel getBlog(@PathVariable String id) throws BlogModelNotFoundException {
        return blogService.getBlogById(id);
    }

    @PutMapping("/add")
    public void addBlog(@RequestBody BlogModel blogModel) throws BlogModelExistsException {
        blogService.addBlog(blogModel);
    }

    @PostMapping("/update")
    public void updateBlog(@RequestBody BlogModel blogModel) throws BlogModelNotFoundException {
        blogService.updateBlog(blogModel);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeBlog(@PathVariable String id) {
        if (!blogService.removeBlog(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler({BlogModelNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(BlogModelNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BlogModelExistsException.class})
    public ResponseEntity<ErrorResponse> handleModelExistException(BlogModelExistsException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }
}
