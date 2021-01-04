package com.example.studentservice.controller;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentservice.dto.APIResponse;
import com.example.studentservice.model.Student;
import com.example.studentservice.service.StudentService;

@RestController
@RequestMapping("student")
public class DemoController {

	@Autowired
	private MessageSource messages;

	@Autowired
	private StudentService studentService;

	@SuppressWarnings("deprecation")
	@GetMapping(value = "/getstudentdatabyid/students", produces = { "application/hal+json" })
	public ResponseEntity<APIResponse> getStudentdataById() {
		
		List<Student> students=studentService.getStudents();
		
		for(final Student student:students) {
			Link selflink=linkTo(methodOn(StudentController.class).getStudentById(student.getId())).withSelfRel();
			student.add(selflink);
		}
		
		Link link=linkTo(methodOn(StudentController.class).getStudent()).withSelfRel();
		
		CollectionModel<Student> finalStudent=new CollectionModel<>(students, link);
		
		
		return ResponseEntity.ok(new APIResponse(HttpStatus.OK.value(), true,
				this.messages.getMessage("get school success", null, LocaleContextHolder.getLocale()),finalStudent ));
	}

}
