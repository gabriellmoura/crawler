package com.data.importer.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookResponseDto {
    private Integer numberOfBooks;
    private List<BookDtoResponse> books;

    public void setBooks(List<BookDtoResponse> books) {
        if (books == null || books.isEmpty()) {
            this.numberOfBooks = 0;
        } else {
            this.numberOfBooks = books.size();
        }
        this.books = books;
    }
}
