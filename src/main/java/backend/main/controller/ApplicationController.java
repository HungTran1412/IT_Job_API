package backend.main.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import backend.main.dto.response.ApiResponse;
import backend.main.entities.Application;
import backend.main.entities.Candidate;
import backend.main.enums.ApplicationStatus;
import backend.main.enums.Code;
import backend.main.exception.AppException;
import backend.main.repository.CandidateRepository;
import backend.main.repository.JobRepository;
import backend.main.services.ApplicationService;
import backend.main.utils.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/applications")
@Tag(name = "Application:", description = "CRUD cho ứng tuyển công việc")
public class ApplicationController {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping
	public ResponseEntity<ApiResponse<Object>> createApplication(@RequestParam String name, @RequestParam String phone,
			@RequestParam String email, @RequestParam MultipartFile cv, @RequestParam String jobId,@CookieValue(value = "jwt", required = false) String token) {

		
		if (token == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.builder()
					.code(Code.TOKEN_INVALID.getCode()).message("Missing token or user not logged in").build());
		}

		// Xác thực token
		if (!jwtUtils.validateToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.builder()
					.code(Code.TOKEN_INVALID.getCode()).message(Code.TOKEN_INVALID.getMessage()).build());
		}
		var a = applicationService.save(name, phone, email, cv, jobId);
		if (a != null) {
			return ResponseEntity.ok(ApiResponse.builder().code(Code.APPLY_SUCCESSFUL.getCode())
					.message(Code.APPLY_SUCCESSFUL.getMessage()).build());
		} else {
			return ResponseEntity.ok(ApiResponse.builder().code(Code.APPLY_FAIL.getCode())
					.message(Code.APPLY_FAIL.getMessage()).build());
		}
	}

	@GetMapping
	public List<Application> getAllApplications() {
		return applicationService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Application> getApplicationById(@PathVariable String id) {
		Optional<Application> application = applicationService.findById(id);
		return application.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/candidate/{candidateId}")
	public List<Application> getApplicationsByCandidate(@PathVariable String candidateEmail) {
		Candidate candidate = candidateRepository.findByEmail(candidateEmail)
				.orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));
		return applicationService.findByCandidate(candidate);
	}

	@PutMapping("/{id}/status")
	public void updateApplicationStatus(@PathVariable String id, @RequestBody ApplicationStatus status) {
		applicationService.updateStatus(id, status);
	}

	@DeleteMapping("/{id}")
	public void deleteApplication(@PathVariable String id) {
		applicationService.delete(id);
	}
	
	
}
