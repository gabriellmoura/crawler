package com.data.importer.controller.mapper;

import com.data.importer.controller.dto.BookDtoResponse;
import com.data.importer.reader.model.Book;
import com.data.importer.utils.Mappable;
import org.springframework.stereotype.Component;

@Component
public class BookMapper implements Mappable<Book, BookDtoResponse> {
    @Override
    public BookDtoResponse map(Book source) {
        BookDtoResponse book = new BookDtoResponse();

        book.setId(source.getId());
        book.setDescription(source.getDescription());
        book.setIsbn(source.getIsbn());
        book.setLanguage(source.getLanguage());
        book.setTitle(source.getTitle());

        return book;
    }
}
