package com.example.studentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDto {
	
	private long id;
	private String fname;
	private String lname;
	private String schoolname;

}
