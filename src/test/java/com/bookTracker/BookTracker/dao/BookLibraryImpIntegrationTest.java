package com.bookTracker.BookTracker.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.bookTracker.BookTracker.model.Book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BookLibraryImpIntegrationTest {

	@Autowired
	private BookLibrary bookLibrary;

	private Book createBookForTest() {
		Book book = new Book();
		book.setId(21);
		book.setName("Hyperfocus");
		book.setAuthor("Chris Bailey");		
		return book;
	}
	
	@Test
	public void getAllEntries_RetrievingBooks_ListOfBooks() {
		int expectedNumOfBooks = 2;
		int expectedFirstId = 1;
		int expectedSecondId = 5;
		String expectedBookName = "Turning the Flywheel";
		String expectedAuthor = "Jim Collins";
		
		List<Book> books = bookLibrary.getAllEntries();

		assertEquals(expectedNumOfBooks, books.size());
		assertEquals(expectedFirstId, books.get(0).getId());
		assertEquals(expectedSecondId, books.get(1).getId());
		assertEquals(expectedBookName, books.get(1).getName());
		assertEquals(expectedAuthor, books.get(1).getAuthor());
	}

	@Test
	public void createBook_AddBookToDatabase_BookInDatabase() {
		int expectedNumOfBooks = 3;
		String expectedName = "Hyperfocus";
		String expectedAuthor = "Chris Bailey";
		Book bookForTesting = createBookForTest();
		
		bookLibrary.createBook(bookForTesting);

		List<Book> books = bookLibrary.getAllEntries();

		assertEquals(expectedNumOfBooks, books.size());
		assertEquals(expectedName, books.get(2).getName());
		assertEquals(expectedAuthor, books.get(2).getAuthor());

		bookLibrary.deleteBook(bookForTesting.getId());		
	}
}
