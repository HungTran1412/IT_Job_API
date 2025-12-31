package backend.main.services.Impl;

import org.springframework.stereotype.Service;

import backend.main.dto.response.DashboardStatsResponse;
import backend.main.repository.CandidateRepository;
import backend.main.repository.EmployerRepository;
import backend.main.repository.JobRepository;
import backend.main.repository.OrderRepository;
import backend.main.services.DashboardService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final OrderRepository orderRepository;
    private final JobRepository jobRepository;
    private final EmployerRepository employerRepository;
    private final CandidateRepository candidateRepository;

    @Override
    public DashboardStatsResponse getDashboardStats() {
        long totalOrders = orderRepository.count();
        long totalJobs = jobRepository.count();
        long totalEmployers = employerRepository.count();
        long totalCandidates = candidateRepository.count();

        return DashboardStatsResponse.builder()
                .totalOrders(totalOrders)
                .totalJobs(totalJobs)
                .totalEmployers(totalEmployers)
                .totalCandidates(totalCandidates)
                .build();
    }
}
