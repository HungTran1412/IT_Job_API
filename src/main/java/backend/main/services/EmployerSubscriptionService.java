package backend.main.services;

import java.util.List;

import backend.main.dto.request.EmployerSubscriptionRequest;
import backend.main.entities.EmployerSubscription;

public interface EmployerSubscriptionService {
    EmployerSubscription createSubscription(EmployerSubscriptionRequest request);
    EmployerSubscription getSubscriptionById(Integer id);
    List<EmployerSubscription> getSubscriptionsByEmployerId(String employerId);
    List<EmployerSubscription> getAllSubscriptions();
}
