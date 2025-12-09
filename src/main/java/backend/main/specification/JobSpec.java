package backend.main.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import backend.main.entities.Job;
import jakarta.persistence.criteria.Join;

public class JobSpec {

    public static Specification<Job> keyword(String keyword) {
        return (root, query, cb) ->
                keyword == null || keyword.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Job> hasLocations(List<String> locations) {
        return (root, query, cb) -> {
            if (locations == null || locations.isEmpty()) {
				return null;
			}
            Join<Job, String> joinLocation = root.join("location");
            return joinLocation.in(locations);
        };
    }


    public static Specification<Job> position(String position) {
        return (root, query, cb) ->
                position == null || position.isBlank()
                        ? null
                        : cb.equal(root.get("position"), position);
    }

    public static Specification<Job> language(String language) {
        return (root, query, cb) ->
                language == null || language.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("language")), "%" + language.toLowerCase() + "%");
    }

    public static Specification<Job> salaryRange(String salaryRange) {
        return (root, query, cb) -> {
            if (salaryRange == null || salaryRange.isBlank()) {
                return null;
            }
            // Ví dụ salaryRange = "1000-2000"
            String[] parts = salaryRange.split("-");
            int min = Integer.parseInt(parts[0]);
            int max = Integer.parseInt(parts[1]);

            return cb.and(
                    cb.greaterThanOrEqualTo(root.get("salaryMin"), min),
                    cb.lessThanOrEqualTo(root.get("salaryMax"), max)
            );
        };
    }

    public static Specification<Job> workingFrom(String workingFrom) {
        return (root, query, cb) ->
                workingFrom == null || workingFrom.isBlank()
                        ? null
                        : cb.equal(root.get("workingFrom"), workingFrom);
    }
}

