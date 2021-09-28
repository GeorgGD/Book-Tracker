package com.bookTracker.BookTracker.controllers;

import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bookTracker.BookTracker.api.GoogleBooks;
import com.bookTracker.BookTracker.dao.BookLibrary;
import com.bookTracker.BookTracker.dto.BookSearch;
import com.bookTracker.BookTracker.model.Book;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookLibraryControllerIntegrationTest {

	@Autowired
	private BookLibrary bookLibrary;

	private BookLibraryController bookLibraryController;
	private GoogleBooks googleBooks;
	private MockMvc mockMvc;

	// Expected test results
	private final static String expectedTitle = "Courage Is Calling";
	private final static String expectedAuthor = "Ryan Holiday";
	private final static int expectedDatabaseId = 2;
		
	private static Optional<List<BookSearch>> createList() {
		List<BookSearch> list = new ArrayList<>();
		BookSearch bookSearch = new BookSearch();

		bookSearch.setId("isMsEAAAQBAJ");
		bookSearch.setTitle(expectedTitle);
		bookSearch.setAuthor(expectedAuthor);
		bookSearch.setCoverImg("http://books.google.com/books/content?id=isMsEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");

		list.add(bookSearch);
		
		return Optional.ofNullable(list);
	}

	private static Optional<Book> createBook() {
		Book book = new Book(4,
							 expectedTitle,
							 expectedAuthor,
							 "Business & Economics",
							 false,
							 null,
							 "http://books.google.com/books/content?id=isMsEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");

		return Optional.ofNullable(book);
	}
    
	private void setupController() {
		googleBooks = mock(GoogleBooks.class);
		when(googleBooks.searchBook(any(String.class))).thenReturn(createList());
		when(googleBooks.bookInfo(any(String.class))).thenReturn(createBook());
		bookLibraryController = new BookLibraryController(bookLibrary, googleBooks);
	}

	// Checks that the database only has 2 books.
	// If it has more or well books it returns false!
	private void checkDatabaseSizeBeforeHttpRequest() {
		int expectedSize = 2;
		if (expectedSize == bookLibrary.getAllEntries().size())
			return;
		else
			resetDatabase();

	}

	// Checks that the database only has 3 books.
	// If it has more or well books it returns false!
	private boolean checkDatabaseSizeAfterHttpRequest() {
		int expectedSize = 3;
		return expectedSize == bookLibrary.getAllEntries().size();

	}

	/**
	 * Checks if a book was added to the database after performing an HTTP request
	 * @param expectedReadingStatus True if book status is reading
	 * @param expectedDate The date the book was completed
	 * @return True if the book was found in the database with the correct status	
	 */
	private boolean checkIfBookWasAddedToDatabase(boolean expectedReadingStatus, Date expectedDate) {
		for(Book book : bookLibrary.getAllEntries()) {
			if(book.getId() == expectedDatabaseId) {
				assertEquals(expectedTitle, book.getName());
				assertEquals(expectedAuthor, book.getAuthor());
				assertEquals(expectedReadingStatus, book.isReading());
				
				if (expectedDate == null) {
					assertEquals(expectedDate, book.getCompleted_date());
				} else {
					assertEquals(expectedDate.toString(), book.getCompleted_date().toString());
				}
				
				return true;
			}
		}
		return false;
	}

	private void resetDatabase() {
		bookLibrary.deleteBook(expectedDatabaseId);
	}
	
	@BeforeEach
	private void setupMockMvc() {
		setupController();
		mockMvc = MockMvcBuilders.standaloneSetup(bookLibraryController).build();
	}

	@Test
	public void searchForBook_HttpRequestToEndpoint_ListOfBooks() {		
		String endpoint = "/searchBook";
		RequestBuilder request = get(endpoint)
			.param("query", "Input is stubbed");
		
		try {
			mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$..title").value(expectedTitle))
				.andExpect(jsonPath("$..author").value(expectedAuthor));
		} catch (Exception e) {
    		e.printStackTrace();
		    fail();
		}
	}

	@Test
	public void addBookToRead_HttpRequestToEndpoint_IncreaseNumberOfBooksInDatabase() {	    	  		
		String endpoint = "/toRead";
		RequestBuilder request = get(endpoint)
			.param("id", "Input is stubbed");
		
	    checkDatabaseSizeBeforeHttpRequest();

		try {
			mockMvc.perform(request)
				.andExpect(status().isOk());

			assertTrue(checkDatabaseSizeAfterHttpRequest());			
			assertTrue(checkIfBookWasAddedToDatabase(false, null));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			resetDatabase();
		}
	}

	@Test
	public void addBookReading_HttpRequestToEndpoint_IncreaseNumberOfBooksInDatabase() {
		String endpoint = "/reading";
		RequestBuilder request = get(endpoint)
			.param("id", "Input is stubbed");

	    checkDatabaseSizeBeforeHttpRequest();

		try {
			mockMvc.perform(request)
				.andExpect(status().isOk());

			assertTrue(checkDatabaseSizeAfterHttpRequest());			
			assertTrue(checkIfBookWasAddedToDatabase(true, null));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			resetDatabase();
		}
	}

	@Test
	public void addBookCompleted_HttpRequestToEndpoint_IncreaseNumberOfBooksInDatabase() {
		String endpoint = "/completed";
		RequestBuilder request = get(endpoint)
			.param("id", "Input is stubbed");

	    checkDatabaseSizeBeforeHttpRequest();

		try {
			mockMvc.perform(request)
				.andExpect(status().isOk());
			
			assertTrue(checkDatabaseSizeAfterHttpRequest());			
			assertTrue(checkIfBookWasAddedToDatabase(false, new Date(System.currentTimeMillis())));
			
			resetDatabase();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void updateBookToRead_HttpRequestToEndpoint_UpdateBooksStatus() {
		String endpoint = "/updateToRead";
		RequestBuilder request = get(endpoint)
			.param("id", "1");
		
		Book book = bookLibrary.getBook(1).get();
		assertTrue(book.isReading());
		
		try {
			mockMvc.perform(request)
				.andExpect(status().isOk());

			Book updatedBook = bookLibrary.getBook(1).get();
			assertFalse(updatedBook.isReading());

			updatedBook.setReading(true);
			bookLibrary.updateBook(updatedBook);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
