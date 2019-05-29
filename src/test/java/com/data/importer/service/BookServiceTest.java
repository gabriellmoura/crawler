package com.data.importer.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.data.importer.controller.dto.BookDto;
import com.data.importer.controller.dto.BookDtoResponse;
import com.data.importer.reader.data.DataImporter;
import com.data.importer.reader.model.Book;
import com.data.importer.repository.BookRepository;
import com.data.importer.repository.entity.BookEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

  @Mock
  private BookRepository bookRepository;

  @InjectMocks
  private BookService bookService;

  @Mock
  private DataImporter dataImporter;

  @Test
  public void findById() {
    Optional<BookEntity> optionalBookEntity = Optional.of(mockBookEntity(mockBook()));
    when(bookRepository.findById(Mockito.anyLong())).thenReturn(optionalBookEntity);
    BookDto bookDtoResult = bookService.findBookById(1l);
    BookDto bookDtoExpected = mockBook();
    bookDtoAssertEquals(bookDtoResult, bookDtoExpected);
  }

  @Test
  public void save() {
    when(bookRepository.save(Mockito.any(BookEntity.class))).thenReturn(mockBookEntity(mockBook()));
    BookDto bookDtoResult = bookService.save(mockBook());
    BookDto bookDtoExpected = mockBook();
    Mockito.verify(bookRepository, times(1)).save(Mockito.any(BookEntity.class));
    bookDtoAssertEquals(bookDtoResult, bookDtoExpected);
  }

  @Test
  public void testImport() {
    when(dataImporter.importBook()).thenReturn(mockListBook());
    bookService.importBooks();
    Mockito.verify(bookRepository, times(5)).save(Mockito.any(BookEntity.class));
  }

  @Test
  public void findAll() {
    when(bookRepository.findAll()).thenReturn(mockIterableBookEntity());
    List<BookDtoResponse> books = bookService.findAll();
    Assert.assertEquals(5, books.size());
  }

  private Iterable<BookEntity> mockIterableBookEntity() {
    List<BookEntity> bookEntities = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      BookEntity bookEntity = new BookEntity();

      bookEntity.setId(Long.valueOf(i + 1));
      bookEntity.setTitle("Title ".concat(String.valueOf(i)));
      bookEntity.setIsbn("12FF2123".concat(String.valueOf(i)));
      bookEntity.setLanguage("en-us");
      bookEntity.setDescription("Lorem Ipsum ".concat(String.valueOf(i)));

      bookEntities.add(bookEntity);
    }
    return bookEntities;
  }

  private List<Book> mockListBook() {
    List<Book> books = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Book book = new Book();

      book.setTitle("Title ".concat(String.valueOf(i)));
      book.setIsbn("12FF2123".concat(String.valueOf(i)));
      book.setLanguage("en-us");
      book.setDescription("Lorem Ipsum ".concat(String.valueOf(i)));

      books.add(book);
    }
    return books;
  }

  private void bookDtoAssertEquals(BookDto bookDtoResult, BookDto bookDtoExpected) {
    Assert.assertNotNull(bookDtoExpected.getTitle(), bookDtoResult.getTitle());
    Assert.assertNotNull(bookDtoExpected.getDescription(), bookDtoResult.getDescription());
    Assert.assertNotNull(bookDtoExpected.getLanguage(), bookDtoResult.getLanguage());
    Assert.assertNotNull(bookDtoExpected.getIsbn(), bookDtoResult.getIsbn());
  }

  private BookDto mockBook() {
    return new BookDto("Title", "description", "en-us", "SJSDF1ASD");
  }

  private BookEntity mockBookEntity(BookDto bookDto) {
    BookEntity bookEntity = new BookEntity();
    bookEntity.setId(1l);
    bookEntity.setTitle(bookDto.getTitle());
    bookEntity.setDescription(bookDto.getDescription());
    bookEntity.setLanguage(bookDto.getLanguage());
    bookEntity.setIsbn(bookDto.getIsbn());
    return bookEntity;
  }
}
