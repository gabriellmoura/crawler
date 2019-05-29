package com.data.importer.service;

import com.data.importer.controller.dto.BookDto;
import com.data.importer.controller.dto.BookDtoResponse;
import com.data.importer.controller.mapper.BookMapper;
import com.data.importer.reader.data.DataImporter;
import com.data.importer.repository.BookRepository;
import com.data.importer.repository.entity.BookEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class BookService {

    private BookRepository bookRepository;

    private DataImporter dataImporter;

    private BookMapper mapper;

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public BookService(BookRepository bookRepository, DataImporter dataImporter, BookMapper mapper) {
        this.bookRepository = bookRepository;
        this.dataImporter = dataImporter;
        this.mapper = mapper;
    }

    public BookDto save(BookDto book) {
        log.debug("[BookService.save] - Starting save book");
        BookDto bookDto = null;
        com.data.importer.repository.entity.BookEntity bookEntity = null;
        try {
            bookEntity = modelMapper.map(book, BookEntity.class);
            bookEntity = bookRepository.save(bookEntity);
            bookDto = modelMapper.map(bookEntity, BookDto.class);
        } catch (Exception ex) {
            log.error("[BookService.save] - There was an error when trying to save book.", ex);
            throw ex;
        }
        return bookDto;
    }

    public void importBooks() {
        log.debug("[BookService.importBooks] - Starting importing books");
        try {
            dataImporter.importBook().forEach(b -> bookRepository.save(modelMapper.map(b, BookEntity.class)));
        } catch (Exception ex) {
            log.error("[BookService.importBooks] - There was an error when trying to importing.", ex);
            throw ex;
        }
    }

    public BookDto findBookById(Long id) {
        log.debug("[BookService.findBookById] - Starting finding book by ID");
        try {
            Optional<BookEntity> bookEntity = bookRepository.findById(id);
            return bookEntity.isPresent() ? modelMapper.map(bookEntity.get(), BookDtoResponse.class) : null;
        } catch (Exception ex) {
            log.error("[BookService.findBookById] - There was an error when trying to importing.", ex);
            throw ex;
        }
    }

    public List<BookDtoResponse> findAll() {
        log.debug("[BookService.findAll] - Starting finding book by ID");
        try {
            Iterable<BookEntity> booksEntity = bookRepository.findAll();
            List<BookDtoResponse> books = new ArrayList<>();

            for (BookEntity bookEntity : booksEntity) {
                books.add(modelMapper.map(bookEntity, BookDtoResponse.class));
            }
            return books;
        } catch (Exception ex) {
            log.error("[BookService.findAll] - There was an error when trying to find all books.", ex);
            throw ex;
        }
    }
}
