package backend.main.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import backend.main.entities.Job;
import backend.main.services.JobService;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobService.save(job);
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.findAll();
    }

    @GetMapping("/{title}")
    public ResponseEntity<Job> getJobByTitle(@PathVariable String title) {
        Optional<Job> job = jobService.findByTitle(title);
        return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable String id) {
        jobService.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable String id, @RequestBody Job job) {
        Optional<Job> existingJob = jobService.findById(id);
        if (existingJob.isPresent()) {
            job.setJobId(id); // Ensure the ID is set for update
            return ResponseEntity.ok(jobService.updateJob(job));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<Job> searchJobs(@RequestParam String keyword, @RequestParam String location, @RequestParam String salaryRange) {
        return jobService.search(keyword, location, salaryRange);
    }

    @GetMapping("/status/{status}")
    public List<Job> getJobsByStatus(@PathVariable String status) {
        return jobService.findByStatus(status);
    }
}
