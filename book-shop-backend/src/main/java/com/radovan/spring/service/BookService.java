package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.BookDto;

public interface BookService {

	BookDto addBook(BookDto book);
	
	void deleteBook(Integer bookId);
	
	List<BookDto> listAll();
	
	BookDto getBookById(Integer bookId);

	BookDto updateBook(Integer bookId, BookDto book);
}
