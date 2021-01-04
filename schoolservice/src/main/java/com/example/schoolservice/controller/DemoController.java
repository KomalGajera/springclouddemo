package com.example.schoolservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

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

import com.example.schoolservice.dto.APIResponse;
import com.example.schoolservice.model.School;
import com.example.schoolservice.service.SchoolService;

@RestController
@RequestMapping("school")
public class DemoController {

	@Autowired
	private MessageSource messages;

	@Autowired
	private SchoolService schoolService;

	@SuppressWarnings("deprecation")
	@GetMapping(value = "/getschooldatabyid/schools", produces = { "application/hal+json" })
	public ResponseEntity<APIResponse> getSchooldataById() {

		List<School> schools = schoolService.getSchool();
		for (final School school : schools) {
			Link selfLink = linkTo(methodOn(SchoolController.class).getSchoolById(school.getId())).withSelfRel();
			school.add(selfLink);
		}
		Link link = linkTo(methodOn(SchoolController.class).getSchool()).withSelfRel();

		CollectionModel<School> school12 = new CollectionModel<>(schools, link);
		return ResponseEntity.ok(new APIResponse(HttpStatus.OK.value(), true,
				this.messages.getMessage("get school success", null, LocaleContextHolder.getLocale()), school12));
	}

}
