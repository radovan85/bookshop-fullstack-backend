package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radovan.spring.dto.BookDto;
import com.radovan.spring.exceptions.DataNotValidatedException;
import com.radovan.spring.service.BookService;

@RestController
@RequestMapping(value = "/api/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping(value = "/allBooks")
	public ResponseEntity<List<BookDto>> getAllBooks() {
		List<BookDto> allBooks = bookService.listAll();
		return ResponseEntity.ok().body(allBooks);
	}

	@PreAuthorize(value = "hasAuthority('ADMIN')")
	@PostMapping(value = "/storeBook")
	public ResponseEntity<String> storeBook(@Validated @RequestBody BookDto book, Errors errors) {
		if (errors.hasErrors()) {
			Error error = new Error("Book is not validated!");
			throw new DataNotValidatedException(error);
		}

		BookDto storedBook = bookService.addBook(book);
		return ResponseEntity.ok().body("Book stored to database with id " + storedBook.getBookId());
	}

	@PreAuthorize(value = "hasAuthority('ADMIN')")
	@DeleteMapping(value = "/deleteBook/{bookId}")
	public ResponseEntity<String> deleteBook(@PathVariable("bookId") Integer bookId) {

		bookService.deleteBook(bookId);
		return ResponseEntity.ok().body("Book with id " + bookId + " is permanelntly deleted!");
	}

	@GetMapping(value = "/bookDetails/{bookId}")
	public ResponseEntity<BookDto> getBookDetails(@PathVariable("bookId") Integer bookId) {
		BookDto book = bookService.getBookById(bookId);
		return ResponseEntity.ok().body(book);
	}
	
	@PreAuthorize(value = "hasAuthority('ADMIN')")
	@PutMapping(value="/updateBook/{bookId}")
	public ResponseEntity<String> updateBook(@Validated @RequestBody BookDto book,
			@PathVariable("bookId") Integer bookId,Errors errors){
		
		if(errors.hasErrors()) {
			Error error = new Error("Data is not validated!");
			throw new DataNotValidatedException(error);
		}
		
		BookDto updatedBook = bookService.updateBook(bookId,book);
		return ResponseEntity.ok().body("Book with id " + updatedBook.getBookId() + " is updated!");
	}
}
