package com.example.schoolservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.schoolservice.exception.SchoolNotFoundException;
import com.example.schoolservice.model.School;
import com.example.schoolservice.repository.SchoolRepository;

@Service
public class SchoolServiceImpl implements SchoolService {
	
	@Autowired
	SchoolRepository repo;

	@Override
	public School save(School school) {
		// TODO Auto-generated method stub
		return repo.save(school);
		
	}

	@Override
	public List<School> getSchool() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}

	@Override
	public void deleteSchool(Long id) {
		// TODO Auto-generated method stub
		School school=repo.findById(id).orElseThrow(() -> new SchoolNotFoundException("sorry school is not avilable"));
		repo.delete(school);
	}

	@Override
	public School getSchoolByid(long id) {
		// TODO Auto-generated method stub
		School school=repo.findById(id).orElseThrow(() -> new SchoolNotFoundException("sorry school is not avilable"));
		return school;
	}

}
