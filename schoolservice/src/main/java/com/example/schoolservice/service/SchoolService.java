package com.example.schoolservice.service;

import java.util.List;

import com.example.schoolservice.model.School;

public interface SchoolService {

	School save( School school);

	List<School> getSchool();

	void deleteSchool(Long id);

	School getSchoolByid(long id);

}
