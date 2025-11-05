package backend.main.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.main.entities.Job;
import backend.main.repository.JobRepository;
import backend.main.services.JobService;
import jakarta.transaction.Transactional;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public Job save(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public List<Job> findAll() {
        return (List<Job>) jobRepository.findAll();
    }

    @Override
    public Optional<Job> findById(String jobId) {
        return jobRepository.findById(jobId);
    }

    @Override
    public Optional<Job> findByTitle(String title) {
        return jobRepository.findByTitle(title);
    }

    @Override
    public void deleteById(String jobId) {
        jobRepository.deleteById(jobId);
    }

    @Override
    public List<Job> search(String keyword, String location, String salaryRange) {
        return jobRepository.findByTitleContainingAndLocationContaining(keyword, location);
    }


    @Override
    @Transactional
    public Job updateJob(Job job) {
        return jobRepository.save(job);
    }

	@Override
	public List<Job> findByStatus(String status) {
		return jobRepository.findByStatus(status);
	}
}
