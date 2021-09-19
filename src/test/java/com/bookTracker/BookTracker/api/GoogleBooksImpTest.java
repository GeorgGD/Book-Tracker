package com.bookTracker.BookTracker.api;

import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GoogleBooksImpTest {

	private GoogleBooksImp googleBooksImp;
	private OkHttpClient client;
	
	@Before
	private GoogleBooksImp createGoogleBooksImp() {
		try {
			setupClient();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return new GoogleBooksImp(client, new ObjectMapper());
	}

	
}
