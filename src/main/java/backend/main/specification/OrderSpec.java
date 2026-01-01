package backend.main.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import backend.main.entities.Order;

public class OrderSpec {

    public static Specification<Order> createdBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null && end == null) {
                return null;
            }
            if (start != null && end != null) {
                return cb.between(root.get("createdAt"), start, end);
            }
            if (start != null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), start);
            }
            return cb.lessThanOrEqualTo(root.get("createdAt"), end);
        };
    }
    
    public static Specification<Order> hasStatus(String status) {
        return (root, query, cb) -> 
            status == null || status.isBlank() 
                ? null 
                : cb.equal(root.get("status"), status);
    }
}
