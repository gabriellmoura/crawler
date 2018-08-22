package com.data.importer.service;

import com.data.importer.controller.dto.BookDto;
import com.data.importer.controller.dto.BookDtoResponse;
import com.data.importer.controller.mapper.BookMapper;
import com.data.importer.reader.data.DataImporter;
import com.data.importer.repository.BookRepository;
import com.data.importer.repository.entity.BookEntity;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class BookService {

    private BookRepository bookRepository;

    private DataImporter dataImporter;

    private BookMapper mapper;

    ModelMapper modelMapper = new ModelMapper();

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

    public List<BookDtoResponse> importBooks() {
        log.debug("[BookService.importBooks] - Starting importing books");
        List<BookDtoResponse> books = new ArrayList<>();
        try {
            dataImporter.importBook().forEach(b -> books.add(mapper.map(b)));
        } catch (Exception ex) {
            log.error("[BookService.importBooks] - There was an error when trying to importing.", ex);
            throw ex;
        }
        return books;
    }

    public BookDto findBookById(Long id) {
        try {
            Optional<BookEntity> bookEntity = bookRepository.findById(id);
            return bookEntity.isPresent() ? modelMapper.map(bookEntity.get(), BookDtoResponse.class) : null;
        } catch (Exception ex) {
            log.error("[BookService.importBooks] - There was an error when trying to importing.", ex);
            throw ex;
        }
    }
}