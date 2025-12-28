package backend.main.specification;

import org.springframework.data.jpa.domain.Specification;
import backend.main.entities.Candidate;

public class CandidateSpec {

    public static Specification<Candidate> fullname(String fullname) {
        return (root, query, cb) ->
                fullname == null || fullname.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("fullname")), "%" + fullname.toLowerCase() + "%");
    }

    public static Specification<Candidate> email(String email) {
        return (root, query, cb) ->
                email == null || email.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<Candidate> softSkill(String softSkill) {
        return (root, query, cb) ->
                softSkill == null || softSkill.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("softSkill")), "%" + softSkill.toLowerCase() + "%");
    }

    public static Specification<Candidate> experience(String experience) {
        return (root, query, cb) ->
                experience == null || experience.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("experience")), "%" + experience.toLowerCase() + "%");
    }

    public static Specification<Candidate> technologies(String technologies) {
        return (root, query, cb) ->
                technologies == null || technologies.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("technologies")), "%" + technologies.toLowerCase() + "%");
    }

    public static Specification<Candidate> desiredSalary(String desiredSalary) {
        return (root, query, cb) ->
                desiredSalary == null || desiredSalary.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("desiredSalary")), "%" + desiredSalary.toLowerCase() + "%");
    }
    
    public static Specification<Candidate> isPrivate(Boolean isPrivate) {
        return (root, query, cb) ->
                isPrivate == null
                        ? null
                        : cb.equal(root.get("isPrivate"), isPrivate);
    }
}
