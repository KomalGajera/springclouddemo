package com.example.studentservice.service;

import java.util.List;

import com.example.studentservice.model.Student;

public interface StudentService {

	Student save(Student student);

	List<Student> getStudents();

	void deleteStudent(Long id);

	Student getStudentsById(Long id);

	List<Student> getStudentsBySchoolId(Long id);

	List<Student> getStudentByStudentId();

}
