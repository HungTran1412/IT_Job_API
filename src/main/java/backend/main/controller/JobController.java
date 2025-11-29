package backend.main.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Job:", description = "CRUD các bài tuyển dụng, và duyệt bài tuyển dụng")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createJob(@RequestBody JobRequest jobRequest) {
        var j = jobService.save(jobRequest);
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
                .code(Code.CREATE_JOB_SUCCESSFULL.getCode())
                .message(Code.CREATE_JOB_SUCCESSFULL.getMessage())
                .result(jobService.findAll(pageable))
                .build());
    }

    @GetMapping("/{title}")
    public ResponseEntity<Job> getJobByTitle(@PathVariable String title) {
        Optional<Job> job = jobService.findByTitle(title);
        return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteJob(@RequestBody DeleteRequest request) {
        jobService.deleteAllById(request.getIds());
        return ResponseEntity.ok(ApiResponse.builder()
                .code(Code.DELETED_SUCCESSFULLY.getCode())
                .message(Code.DELETED_SUCCESSFULLY.getMessage())
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

    @GetMapping("/search")
    public Page<Job> searchJobs(
            @RequestParam String keyword,
            @RequestParam String location,
            @RequestParam String salaryRange,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "jobId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return jobService.search(keyword, location, salaryRange, pageable);
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
