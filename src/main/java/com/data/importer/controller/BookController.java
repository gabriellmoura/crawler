package com.data.importer.controller;

import com.data.importer.controller.dto.BookDtoResponse;
import com.data.importer.controller.dto.BookResponseDto;
import com.data.importer.controller.dto.BookDto;
import com.data.importer.service.BookService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/book")
@Log4j2
public class BookController {

    private static final Logger logger = LogManager.getLogger(BookController.class);

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/list")
    public ResponseEntity<BookResponseDto> getBooks() {
        logger.debug("[BookController.getBooks] - Listing all books");
        ResponseEntity<BookResponseDto> response;
        List<BookDtoResponse> books;
        BookResponseDto bookResponseDto = new BookResponseDto();
        try {
            books = bookService.importBooks();
            bookResponseDto.setBooks(books);
            response = new ResponseEntity(bookResponseDto, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("[BookController.getBooks] - There was an error when trying to retrieve the books", ex);
            response = new ResponseEntity("There was an error when trying to import books", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping("/")
    public ResponseEntity<BookDto> save(@RequestBody BookDto book) {
        logger.debug("[BookController.save] - Saving book");
        ResponseEntity<BookDto> response;
        try {
            response = new ResponseEntity(bookService.save(book), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("[BookController.getBooks] - There was an error when trying to retrieve the books", ex);
            response = new ResponseEntity("There was an error when trying to save the book",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<BookDto> findBookById(@PathVariable(value = "id") Long id) {
        logger.debug("[BookController.save] - Saving book");
        ResponseEntity<BookDto> response;
        try {
            BookDto bookDto = bookService.findBookById(id);
            response = new ResponseEntity(bookDto, bookDto == null ? HttpStatus.NOT_FOUND: HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("[BookController.getBooks] - There was an error when trying to retrieve the books", ex);
            response = new ResponseEntity("There was an error when trying to save the book",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }



}
