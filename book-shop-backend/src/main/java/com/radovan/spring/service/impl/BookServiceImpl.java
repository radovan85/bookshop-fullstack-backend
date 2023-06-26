package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.BookDto;
import com.radovan.spring.entity.BookEntity;
import com.radovan.spring.exceptions.InstanceNotExistException;
import com.radovan.spring.repository.BookRepository;
import com.radovan.spring.service.BookService;

@Service
@Transactional
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	public BookDto addBook(BookDto book) {
		// TODO Auto-generated method stub
		BookEntity bookEntity = tempConverter.bookDtoToEntity(book);
		BookEntity storedBook = bookRepository.save(bookEntity);
		BookDto returnValue = tempConverter.bookEntityToDto(storedBook);
		return returnValue;
	}

	@Override
	public void deleteBook(Integer bookId) {
		// TODO Auto-generated method stub
		Optional<BookEntity> bookOpt = bookRepository.findById(bookId);
		if (bookOpt.isPresent()) {
			bookRepository.deleteById(bookId);
			bookRepository.flush();
		} else {
			Error error = new Error("Book not found!");
			throw new InstanceNotExistException(error);
		}
	}

	@Override
	public List<BookDto> listAll() {
		// TODO Auto-generated method stub
		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<List<BookEntity>> allBooksOpt = Optional.ofNullable(bookRepository.findAll());
		if (!allBooksOpt.isEmpty()) {
			allBooksOpt.get().forEach((book) -> {
				BookDto bookDto = tempConverter.bookEntityToDto(book);
				returnValue.add(bookDto);
			});
		}
		return returnValue;
	}

	@Override
	public BookDto getBookById(Integer bookId) {
		// TODO Auto-generated method stub
		BookDto returnValue = null;
		Optional<BookEntity> bookOpt = bookRepository.findById(bookId);
		if (bookOpt.isPresent()) {
			returnValue = tempConverter.bookEntityToDto(bookOpt.get());
		} else {
			Error error = new Error("Book not found!");
			throw new InstanceNotExistException(error);
		}

		return returnValue;
	}

	@Override
	public BookDto updateBook(Integer bookId, BookDto book) {
		// TODO Auto-generated method stub
		BookDto returnValue = null;
		Optional<BookEntity> bookOpt = bookRepository.findById(bookId);
		if (bookOpt.isPresent()) {
			book.setBookId(bookId);
			BookEntity bookEntity = tempConverter.bookDtoToEntity(book);
			BookEntity updatedBook = bookRepository.saveAndFlush(bookEntity);
			returnValue = tempConverter.bookEntityToDto(updatedBook);
		} else {
			Error error = new Error("Book not found!");
			throw new InstanceNotExistException(error);
		}
		return returnValue;
	}

}
