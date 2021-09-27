package com.bookTracker.BookTracker.controllers;

import static org.mockito.Mockito.*;

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
	private final static int expectedDatabaseId = 4;
		
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
		Book book = new Book(expectedDatabaseId,
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
}
