package com.data.importer.controller;

import com.data.importer.controller.dto.BookDtoResponse;
import com.data.importer.controller.dto.BookResponseDto;
import com.data.importer.controller.dto.BookDto;
import com.data.importer.service.BookService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "Import Operation", notes = "This endpoint is responsible for import the books from the https://kotlinlang.org/docs/books.html" +
            " and then save these books on H2 database")
    @PostMapping("/import")
    public ResponseEntity<String> importBooks() {
        logger.debug("[BookController.importBooks] - Listing all books");
        ResponseEntity<String> response;
        try {
            bookService.importBooks();
            response = new ResponseEntity("The books are being imported", HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("[BookController.importBooks] - There was an error when trying to retrieve books", ex);
            response = new ResponseEntity("There was an error when trying to import books", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @ApiOperation(value = "Save Book", notes = "This endpoint is responsible for save new book.")
    @PostMapping("/")
    public ResponseEntity<BookDto> save(@ApiParam(value = "It indicates all values to create a new Book",
            name = "Book Object", required = true) @RequestBody BookDto book) {
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

    @ApiOperation(value = "Find Book By ID", notes = "This endpoint is responsible for find book by ID.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<BookDto> findBookById(@ApiParam(value = "It indicates the Book Identification value",
            name = "ID", required = true) @PathVariable(value = "id") Long id) {
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

    @ApiOperation(value = "List All Books", notes = "This endpoint is responsible find all books.")
    @GetMapping("/")
    public ResponseEntity<BookResponseDto> findAllBooks() {
        logger.debug("[BookController.getBooks] - Listing all books");
        ResponseEntity<BookResponseDto> response;
        List<BookDtoResponse> books;
        BookResponseDto bookResponseDto = new BookResponseDto();
        try {
            books = bookService.findAll();
            bookResponseDto.setBooks(books);
            response = new ResponseEntity(bookResponseDto, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("[BookController.getBooks] - There was an error when trying to retrieve the books", ex);
            response = new ResponseEntity("There was an error when trying to import books", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }


}
