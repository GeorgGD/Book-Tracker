package com.bookTracker.BookTracker.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a book entry in the 
 * @auther Georgios Davakos
 * @since 2021-09-14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "library")
public class Book {

	@Id
	@Positive
	private int id;

	@NotNull
	private String name;    
	private String auther;
	private String genre;
	private boolean reading;
	private Date completed_date;
	private String cover_img;
}
