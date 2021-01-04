package com.example.studentservice.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.studentservice.dto.APIResponse;
import com.example.studentservice.dto.StudentDto;
import com.example.studentservice.model.Student;
import com.example.studentservice.service.StudentService;
import com.google.common.reflect.TypeToken;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Validated
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private MessageSource messages;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private StudentService studentService;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private KafkaTemplate<String, Student> kafkaTemplate;

	private static final String TOPIC = "StudentTopic";

	@PostMapping("/addstudent")
	@HystrixCommand(fallbackMethod = "callStudentServiceAndGetData_Fallback")
	public ResponseEntity<APIResponse> addStudent(@RequestBody @Valid Student student) {

		log.info("start to save student data...");
		APIResponse response = restTemplate.exchange("http://school-service/school/getschoolbyid/{schoolid}",
				HttpMethod.GET, null, new ParameterizedTypeReference<APIResponse>() {
				}, student.getSchoolid()).getBody();

		if (response.getStatus() == 200) {

			Student student2 = studentService.save(student);
			kafkaTemplate.send(TOPIC,student2);
			log.info("student save successfully..");
			return ResponseEntity.status(HttpStatus.OK).body(new APIResponse(HttpStatus.OK.value(), true,
					messages.getMessage("student save sucessfully..", null, LocaleContextHolder.getLocale()), null));
		} else {

			return ResponseEntity.status(HttpStatus.OK).body(new APIResponse(HttpStatus.OK.value(), true,
					messages.getMessage("Please check your school id..", null, LocaleContextHolder.getLocale()), null));
		}

	}

	@SuppressWarnings("unused")
	private ResponseEntity<APIResponse> callStudentServiceAndGetData_Fallback(@RequestBody @Valid Student student) {

		log.info("school Service is down!!! fallback route enabled...");
		return ResponseEntity.status(HttpStatus.OK)
				.body(new APIResponse(HttpStatus.OK.value(), true,
						messages.getMessage(
								"CIRCUIT BREAKER ENABLED!!! No Response From school Service at this moment......", null,
								LocaleContextHolder.getLocale()),
						null));

	}

	@SuppressWarnings("serial")
	@GetMapping("/getstudent")
	public ResponseEntity<APIResponse> getStudent() {

		log.info("fetch student data...");
		List<Student> students = studentService.getStudents();
		log.info("fetch student data successfully");
		return ResponseEntity.status(HttpStatus.OK).body(new APIResponse(HttpStatus.OK.value(), true, null,
				modelMapper.map(students, new TypeToken<List<Student>>() {
				}.getType())));
	}

	@GetMapping("/getschool/{id}")
	@HystrixCommand(fallbackMethod = "callSchoolNotFound")
	public ResponseEntity<APIResponse> getSchool(@PathVariable Long id) {

		APIResponse response = restTemplate.exchange("http://school-service/school/getschoolbyid/{schoolid}",
				HttpMethod.GET, null, new ParameterizedTypeReference<APIResponse>() {
				}, id).getBody();

		if (response.getStatus() == 200) {
			return ResponseEntity.status(HttpStatus.OK).body(new APIResponse(HttpStatus.OK.value(), true,
					messages.getMessage("School is available", null, LocaleContextHolder.getLocale()), null));
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(new APIResponse(HttpStatus.OK.value(), true,
					messages.getMessage("School is not available", null, LocaleContextHolder.getLocale()), null));
		}

	}

	@SuppressWarnings("unused")
	private ResponseEntity<APIResponse> callSchoolNotFound(Long id) {

		log.info("school Service is down!!! fallback route enabled...");
		return ResponseEntity.status(HttpStatus.OK)
				.body(new APIResponse(HttpStatus.OK.value(), true,
						messages.getMessage(
								"CIRCUIT BREAKER ENABLED!!! No Response From school Service at this moment......", null,
								LocaleContextHolder.getLocale()),
						null));

	}

	@DeleteMapping("/deletestudent/{id}")
	public ResponseEntity<APIResponse> deleteStudent(@PathVariable Long id) {

		log.info("delete Student data");
		studentService.deleteStudent(id);
		log.info("delete Student data successfully");
		return ResponseEntity.status(HttpStatus.OK).body(new APIResponse(HttpStatus.OK.value(), true,
				messages.getMessage("delete sucessfully", null, LocaleContextHolder.getLocale()), null));
	}

	@PutMapping("/updatestudent")
	public ResponseEntity<APIResponse> updateStudent(@RequestBody @Valid Student student) {

		log.info("start to update student data...");
		APIResponse response = restTemplate.exchange("http://school-service/school/getschoolbyid/{schoolid}",
				HttpMethod.GET, null, new ParameterizedTypeReference<APIResponse>() {
				}, student.getSchoolid()).getBody();

		if (response.getStatus() == 200) {

			studentService.save(student);
			log.info("student update successfully..");
			return ResponseEntity.status(HttpStatus.OK).body(new APIResponse(HttpStatus.OK.value(), true,
					messages.getMessage("student update sucessfully..", null, LocaleContextHolder.getLocale()), null));
		} else {

			return ResponseEntity.status(HttpStatus.OK).body(new APIResponse(HttpStatus.OK.value(), true,
					messages.getMessage("Please check your school id..", null, LocaleContextHolder.getLocale()), null));
		}


	}

	@GetMapping("/getstudentbyid/{id}")
	public ResponseEntity<APIResponse> getStudentById(@PathVariable Long id) {

		log.info("fetch student data...");
		Student student = studentService.getStudentsById(id);
		log.info("fetch student data successfully");
		return ResponseEntity.ok(new APIResponse(HttpStatus.OK.value(), true,
				this.messages.getMessage("get student success", null, LocaleContextHolder.getLocale()), student));
	}

	@GetMapping("/getstudentbyschoolid/{id}")
	public ResponseEntity<APIResponse> getStudentBySchoolId(@PathVariable Long id) {

		log.info("fetch school data...");
		List<Student> student = studentService.getStudentsBySchoolId(id);
		log.info("fetch school data successfully");
		return ResponseEntity.ok(new APIResponse(HttpStatus.OK.value(), true,
				this.messages.getMessage("get student success", null, LocaleContextHolder.getLocale()), student));
	}

	@SuppressWarnings("serial")
	@GetMapping("/getschoolbyschoolid")
	public ResponseEntity<APIResponse> getSchoolBySchoolId() {

		log.info("fetch school data...");
		List<StudentDto> studentdto = new ArrayList<>();
		List<Student> student = studentService.getStudentByStudentId();

		for (Student stud : student) {

			String response = restTemplate.exchange("http://school-service/school/getschoolbyschoolid/{schoolid}",
					HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
					}, stud.getSchoolid()).getBody();
			StudentDto studdto = new StudentDto(stud.getId(), stud.getFname(), stud.getLname(), response);
			studentdto.add(studdto);

		}
		log.info("fetch school data successfully");
		return ResponseEntity.status(HttpStatus.OK).body(new APIResponse(HttpStatus.OK.value(), true, null,
				modelMapper.map(studentdto, new TypeToken<List<StudentDto>>() {
				}.getType())));
	}
}
