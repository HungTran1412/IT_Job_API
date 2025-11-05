package backend.main.services;

public interface JobRepository{
    Job save(Job job);
    List<Job> findAll();
    Optional<Job> findByTilte(String tilte);
    void deleteById(String jobId);
    List<Job> search(String keyword, String location, String salaryRange);
    List<Job> findByStatus(String status);

}
