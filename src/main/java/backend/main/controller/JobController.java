package backend.main.controller;

import backend.main.entities.Candidate;
import backend.main.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import backend.main.dto.request.job.DeleteRequest;
import backend.main.dto.request.job.JobRequest;
import backend.main.dto.request.job.JobReviewRequest;
import backend.main.dto.request.job.JobSearchRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.dto.response.JobResponse;
import backend.main.entities.Job;
import backend.main.enums.Code;
import backend.main.enums.JobStatus;
import backend.main.services.JobService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Job:", description = "CRUD các bài tuyển dụng, và duyệt bài tuyển dụng")
public class JobController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createJob(@CookieValue(value = "jwt", required = false) String token,
                                                         @RequestBody JobRequest jobRequest) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Object>builder()
                    .code(Code.TOKEN_INVALID.getCode()).message("Missing token or user not logged in").build());
        }
//        System.out.println("Token: " + token);
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Object>builder()
                    .code(Code.TOKEN_INVALID.getCode()).message(Code.TOKEN_INVALID.getMessage()).build());
        }
        String email = jwtUtils.extractEmail(token);

        var j = jobService.save(jobRequest, email);

        return ResponseEntity.ok(ApiResponse.builder()
                        .code(Code.CREATE_JOB_SUCCESSFULL.getCode())
                        .message(Code.CREATE_JOB_SUCCESSFULL.getMessage())
                        .result(j)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "jobId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return ResponseEntity.ok(ApiResponse.builder()
                .code(Code.GET_JOB_SUCCESSFULL.getCode())
                .message(Code.GET_JOB_SUCCESSFULL.getMessage())
                .result(jobService.findAll(pageable))
                .build());
    }

//    @GetMapping("/{title}")
//    public ResponseEntity<Job> getJobByTitle(@PathVariable String title) {
//        Optional<Job> job = jobService.findByTitle(title);
//        return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Object>> deleteJob(@RequestBody DeleteRequest request) {
        jobService.deleteAllById(request.getIds());
        return ResponseEntity.ok(ApiResponse.builder()
                .code(Code.DELETED_SUCCESSFULLY.getCode())
                .message(Code.DELETED_SUCCESSFULLY.getMessage())
                .build()); 
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> getJob(@PathVariable String id) {
        JobResponse gotJob = jobService.getJob(id);
        return ResponseEntity.ok(ApiResponse.<JobResponse>builder()
                .code(Code.UPDATE_JOB_SUCCESSFULL.getCode())
                .message(Code.UPDATE_JOB_SUCCESSFULL.getMessage())
                .result(gotJob)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(@PathVariable String id, @RequestBody JobRequest jobRequest) {
        JobResponse updatedJob = jobService.updateJob(id, jobRequest);
        return ResponseEntity.ok(ApiResponse.<JobResponse>builder()
                .code(Code.UPDATE_JOB_SUCCESSFULL.getCode())
                .message(Code.UPDATE_JOB_SUCCESSFULL.getMessage())
                .result(updatedJob)
                .build());
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchJobs(
    		@RequestBody JobSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage()
        		, request.getSize()
        		, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(ApiResponse.builder()
                .code(Code.SEARCH_RESULT.getCode())
                .message(Code.SEARCH_RESULT.getMessage())
                .result(jobService.search(request,pageable))
                .build());
       
    }

    @GetMapping("/status/{status}")
    public Page<Job> getJobsByStatus(
            @PathVariable JobStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "jobId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return jobService.findAllByStatus(status, pageable);
    }

    @GetMapping("/approved")
    public Page<JobResponse> getApprovedJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "jobId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return jobService.findAllByStatusApproved(pageable);
    }
    
    @GetMapping("/pending")
    public Page<JobResponse> getPendingJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "jobId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return jobService.findAllByStatusPending(pageable);
    }

    @PutMapping("/review")
    public ResponseEntity<ApiResponse<Job>> reviewJob(@RequestBody JobReviewRequest request) {
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
}
