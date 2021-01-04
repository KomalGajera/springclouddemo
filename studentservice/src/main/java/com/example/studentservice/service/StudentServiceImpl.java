package com.example.studentservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.studentservice.exception.StudentNotFoundexception;
import com.example.studentservice.model.Student;
import com.example.studentservice.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	StudentRepository repo;

	@Override
	public Student save(Student student) {
		// TODO Auto-generated method stub
		return repo.save(student);
	}

	@Override
	public List<Student> getStudents() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}

	@Override
	public void deleteStudent(Long id) {
		// TODO Auto-generated method stub
		repo.deleteById(id);
	}

	@Override
	public Student getStudentsById(Long id) {
		// TODO Auto-generated method stub
		Student student=repo.findById(id).orElseThrow(()-> new StudentNotFoundexception("Student id is not found..."));
		return student;
	}

	@Override
	public List<Student> getStudentsBySchoolId(Long id) {
		// TODO Auto-generated method stub
		return repo.findBySchoolid(id);
	}

	@Override
	public List<Student> getStudentByStudentId() {
		// TODO Auto-generated method stub		
		return repo.findAll();
	}

}