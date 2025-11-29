package backend.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import backend.main.dto.request.job.DeleteRequest;
import backend.main.dto.request.job.JobRequest;
import backend.main.dto.request.job.JobReviewRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.dto.response.JobResponse;
import backend.main.entities.Job;
import backend.main.enums.Code;
import backend.main.enums.JobStatus;
import backend.main.services.JobService;
import backend.main.utils.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Job:", description = "CRUD các bài tuyển dụng, và duyệt bài tuyển dụng")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createJob(@CookieValue(value = "jwt", required = true) String token,@RequestBody JobRequest jobRequest) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.builder()
                    .code(Code.UNAUTHORIZED.getCode())
                    .message(Code.UNAUTHORIZED.getMessage())
                    .build());
        }
        var j = jobService.save(jobRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                        .code(Code.CREATE_JOB_SUCCESSFULL.getCode())
                        .message(Code.CREATE_JOB_SUCCESSFULL.getMessage())
                        .result(j)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAllJobs(
			@CookieValue(value = "jwt", required = true) String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "jobId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.builder()
                    .code(Code.UNAUTHORIZED.getCode())
                    .message(Code.UNAUTHORIZED.getMessage())
                    .build());
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return ResponseEntity.ok(ApiResponse.builder()
                .code(Code.CREATE_JOB_SUCCESSFULL.getCode())
                .message(Code.CREATE_JOB_SUCCESSFULL.getMessage())
                .result(jobService.findAll(pageable))
                .build());
    }

//    @GetMapping("/{title}")
//    public ResponseEntity<ApiResponse<?>> getJobByTitle(@CookieValue(value = "jwt", required = true) String token,@PathVariable String title) {
//        if (!jwtUtils.validateToken(token)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.builder()
//                    .code(Code.UNAUTHORIZED.getCode())
//                    .message(Code.UNAUTHORIZED.getMessage())
//                    .build());
//        }
//        Optional<Job> job = jobService.findByTitle(title);
//        return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Object>> deleteJob(@CookieValue(value = "jwt", required = true) String token,@RequestBody DeleteRequest request) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.builder()
                    .code(Code.UNAUTHORIZED.getCode())
                    .message(Code.UNAUTHORIZED.getMessage())
                    .build());
        }
        jobService.deleteAllById(request.getIds());
        return ResponseEntity.ok(ApiResponse.builder()
                .code(Code.DELETED_SUCCESSFULLY.getCode())
                .message(Code.DELETED_SUCCESSFULLY.getMessage())
                .build()); 
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getJob(@CookieValue(value = "jwt", required = true) String token,@PathVariable String id) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.builder()
                    .code(Code.UNAUTHORIZED.getCode())
                    .message(Code.UNAUTHORIZED.getMessage())
                    .build());
        }
        Job job = jobService.findById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(Code.GET_JOB_SUCCESSFULL.getCode())
                .message(Code.GET_JOB_SUCCESSFULL.getMessage())
                .result(job)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateJob(@CookieValue(value = "jwt", required = true) String token,@PathVariable String id, @RequestBody JobRequest jobRequest) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.builder()
                    .code(Code.UNAUTHORIZED.getCode())
                    .message(Code.UNAUTHORIZED.getMessage())
                    .build());
        }
        JobResponse updatedJob = jobService.updateJob(id, jobRequest);
        return ResponseEntity.ok(ApiResponse.<JobResponse>builder()
                .code(Code.UPDATE_JOB_SUCCESSFULL.getCode())
                .message(Code.UPDATE_JOB_SUCCESSFULL.getMessage())
                .result(updatedJob)
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Job>>> searchJobs(
			@CookieValue(value = "jwt", required = true) String token,
            @RequestParam String keyword,
            @RequestParam String location,
            @RequestParam String salaryRange,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "jobId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Page<Job>>builder()
                    .code(Code.UNAUTHORIZED.getCode())
                    .message(Code.UNAUTHORIZED.getMessage())
                    .build());
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<Job> jobs = jobService.search(keyword, location, salaryRange, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Job>>builder()
                .code(Code.GET_JOB_SUCCESSFULL.getCode())
                .message(Code.GET_JOB_SUCCESSFULL.getMessage())
                .result(jobs)
                .build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<Job>>> getJobsByStatus(
			@CookieValue(value = "jwt", required = true) String token,
            @PathVariable JobStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "jobId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Page<Job>>builder()
                    .code(Code.UNAUTHORIZED.getCode())
                    .message(Code.UNAUTHORIZED.getMessage())
                    .build());
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<Job> jobs = jobService.findAllByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Job>>builder()
                .code(Code.GET_JOB_SUCCESSFULL.getCode())
                .message(Code.GET_JOB_SUCCESSFULL.getMessage())
                .result(jobs)
                .build());
    }

    @GetMapping("/approved")
    public ResponseEntity<ApiResponse<Page<JobResponse>>> getApprovedJobs(
			@CookieValue(value = "jwt", required = true) String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "jobId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Page<JobResponse>>builder()
                    .code(Code.UNAUTHORIZED.getCode())
                    .message(Code.UNAUTHORIZED.getMessage())
                    .build());
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<JobResponse> jobs = jobService.findAllByStatusApproved(pageable);
        return ResponseEntity.ok(ApiResponse.<Page<JobResponse>>builder()
                .code(Code.GET_JOB_SUCCESSFULL.getCode())
                .message(Code.GET_JOB_SUCCESSFULL.getMessage())
                .result(jobs)
                .build());
    }

    @PutMapping("/review")
    public ResponseEntity<ApiResponse<Job>> reviewJob(@CookieValue(value = "jwt", required = true) String token,@RequestBody JobReviewRequest request) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Job>builder()
                    .code(Code.UNAUTHORIZED.getCode())
                    .message(Code.UNAUTHORIZED.getMessage())
                    .build());
        }
        if(jobService.reviewJob(request)) {
			return ResponseEntity.ok(ApiResponse.<Job>builder()
                .code(Code.JOB_APROVED.getCode())
                .message(Code.JOB_APROVED.getMessage())
                .build());
		} else {
        	return ResponseEntity.ok(ApiResponse.<Job>builder()
                    .code(Code.UNCATEGORIZED_EXCEPTION.getCode())
                    .message(Code.UNCATEGORIZED_EXCEPTION.getMessage())
                    .build());
        }
    }
    
    @GetMapping("/employer/{employerId}")
    public ResponseEntity<ApiResponse<Page<JobResponse>>> getJobsByEmployer(
            @CookieValue(value = "jwt", required = true) String token,
            @PathVariable String employerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "jobId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Page<JobResponse>>builder()
                    .code(Code.UNAUTHORIZED.getCode())
                    .message(Code.UNAUTHORIZED.getMessage())
                    .build());
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<JobResponse> jobs = jobService.findJobsByEmployer(employerId, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<JobResponse>>builder()
                .code(Code.GET_JOB_SUCCESSFULL.getCode())
                .message(Code.GET_JOB_SUCCESSFULL.getMessage())
                .result(jobs)
                .build());
    }
    
    @GetMapping("/employer/{employerId}/status/{status}")
    public ResponseEntity<ApiResponse<Page<JobResponse>>> getJobsByEmployerAndStatus(
            @CookieValue(value = "jwt", required = true) String token,
            @PathVariable String employerId,
            @PathVariable JobStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "jobId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Page<JobResponse>>builder()
                    .code(Code.UNAUTHORIZED.getCode())
                    .message(Code.UNAUTHORIZED.getMessage())
                    .build());
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<JobResponse> jobs = jobService.findJobsByEmployerAndStatus(employerId, status, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<JobResponse>>builder()
                .code(Code.GET_JOB_SUCCESSFULL.getCode())
                .message(Code.GET_JOB_SUCCESSFULL.getMessage())
                .result(jobs)
                .build());
    }
    
    @GetMapping("/employer/{employerId}/search")
    public ResponseEntity<ApiResponse<Page<JobResponse>>> searchJobsByEmployer(
            @CookieValue(value = "jwt", required = true) String token,
            @PathVariable String employerId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "jobId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Page<JobResponse>>builder()
                    .code(Code.UNAUTHORIZED.getCode())
                    .message(Code.UNAUTHORIZED.getMessage())
                    .build());
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<JobResponse> jobs = jobService.searchJobsByEmployer(employerId, keyword, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<JobResponse>>builder()
                .code(Code.GET_JOB_SUCCESSFULL.getCode())
                .message(Code.GET_JOB_SUCCESSFULL.getMessage())
                .result(jobs)
                .build());
    }
}
