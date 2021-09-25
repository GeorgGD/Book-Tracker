package com.bookTracker.BookTracker.controllers;

import com.bookTracker.BookTracker.api.GoogleBooks;
import com.bookTracker.BookTracker.dao.BookLibrary;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

/**
 * The following controller manages a book tracking system
 * were a user can save books they have read, are reading or want 
 * to read in the future
 * @author Georgios Davakos
 * @since 2021-09-25
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class BookLibraryController {

	private BookLibrary bookLibrary;
	private GoogleBooks googleBooks;
}
