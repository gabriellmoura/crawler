package com.data.importer.reader.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {
    private Long id;
    private String title;
    private String description;
    private String language;
    private String isbn;
    private String linkToBuy;
}
