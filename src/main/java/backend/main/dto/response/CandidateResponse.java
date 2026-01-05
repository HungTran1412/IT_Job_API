package backend.main.dto.response;

import java.time.LocalDate;
import java.util.List;

import backend.main.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateResponse {
    String candidateId;
    String fullName;
    String email;
    String address;
    LocalDate dateOfBirth;
    String phone;
    String avatar;
    String cv;
    boolean isPrivate;
    Role role;
    String gender;
    String experience;
    String technologies;
    String softSkill;
    String desiredSalary;
    List<String> likedJobIds;
    List<String> appliedIds;

    public CandidateResponse(String candidateId, String fullName, String email, String address, LocalDate dateOfBirth, String phone, String avatar, boolean isPrivate, Role role, String gender, String experience, String technologies, String softSkill, String desiredSalary) {
        this.candidateId = candidateId;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.avatar = avatar;
        this.isPrivate = isPrivate;
        this.role = role;
        this.gender = gender;
        this.experience = experience;
        this.technologies = technologies;
        this.softSkill = softSkill;
        this.desiredSalary = desiredSalary;
    }
}
