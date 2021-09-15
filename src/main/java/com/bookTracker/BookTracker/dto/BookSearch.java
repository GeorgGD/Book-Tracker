package com.bookTracker.BookTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSearch {

	private String id;
	private String title;
	private String auther;
	private String coverImg;
}
