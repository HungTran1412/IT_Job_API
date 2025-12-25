package backend.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.main.dto.request.ChangePasswordRequest;
import backend.main.dto.request.candidate.CandidateRequest;
import backend.main.dto.request.candidate.CandidateSearchRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.dto.response.CandidateResponse;
import backend.main.entities.Candidate;
import backend.main.entities.Job;
import backend.main.enums.Code;
import backend.main.enums.Role;
import backend.main.exception.AppException;
import backend.main.repository.CandidateRepository;
import backend.main.services.CandidateService;
import backend.main.utils.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Candidate:", description = "Lấy, cập nhật thông tin, đổi mật khẩu cho ứng viên")
public class CandidateController {
	@Autowired
	CandidateService candidateService;
	@Autowired
	CandidateRepository candidateRepository;
	@Autowired
	JwtUtils jwtUtils;

	public CandidateController(CandidateService candidateService, CandidateRepository candidateRepository,
			JwtUtils jwtUtils) {
		this.candidateService = candidateService;
		this.candidateRepository = candidateRepository;
		this.jwtUtils = jwtUtils;
	}

	@GetMapping(value = "/info")
	public ResponseEntity<ApiResponse<?>> getProfile(
			@CookieValue(value = "jwt", required = false) String token) {
		// Kiểm tra xem có token không
		if (token == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<CandidateResponse>builder()
					.code(Code.TOKEN_INVALID.getCode()).message("Missing token or user not logged in").build());
		}

		// Xác thực token
		if (!jwtUtils.validateToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<CandidateResponse>builder()
					.code(Code.TOKEN_INVALID.getCode()).message(Code.TOKEN_INVALID.getMessage()).build());
		}

		// giải mã token: decode token
		String id = jwtUtils.extractId(token);

		// Tim ứng viên theo id
		
		return ResponseEntity.ok(ApiResponse.builder().code(Code.GET_INFO_SUCCEEDED.getCode())
				.message(Code.GET_INFO_SUCCEEDED.getMessage()).result(candidateService.getInfor(id)).build());
	}

	@PatchMapping(value = "/change-password")
	public ResponseEntity<ApiResponse> changePassword(@CookieValue(value = "jwt", required = false) String token,
			@RequestBody ChangePasswordRequest req) {
		// Kiem tra token
		if (token == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Candidate>builder()
					.code(Code.TOKEN_INVALID.getCode()).message("Missing token or user not logged in").build());
		}

		boolean success = candidateService.changePassword(jwtUtils.extractEmail(token), req.getOldPassword(),
				req.getNewPassword());

		if (success == true) {
			return ResponseEntity.ok(ApiResponse.builder().code(Code.PASSWORD_CHANGED.getCode())
					.message(Code.PASSWORD_CHANGED.getMessage()).build());
		} else {
			return ResponseEntity.ok(ApiResponse.builder().code(Code.UNCATEGORIZED_EXCEPTION.getCode())
					.message(Code.UNCATEGORIZED_EXCEPTION.getMessage()).build());
		}
	}

	@PatchMapping(value = "/update", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<ApiResponse<Candidate>> updateInfo(@ModelAttribute CandidateRequest candidateRequest,
			@CookieValue(value = "jwt", required = false) String token) {

		System.out.println("===== [UPDATE CANDIDATE INFO] =====");
		System.out.println("Token: " + token);

		try {
			// Kiểm tra xem có token không
			if (token == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Candidate>builder()
						.code(Code.TOKEN_INVALID.getCode()).message("Missing token or user not logged in").build());
			}

			// Xac thuc token
			if (!jwtUtils.validateToken(token)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Candidate>builder()
						.code(Code.TOKEN_INVALID.getCode()).message(Code.TOKEN_INVALID.getMessage()).build());
			}

			// Lay id tu token
			String id = jwtUtils.extractId(token);
			System.out.println("Candidate ID: " + id);

			// Kiem tra xem nguoi dung co ton tai khong
			Candidate c = candidateRepository.findById(id)
					.orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));

			// Log du lieu nguoi dung
			System.out.println("Fullname: " + candidateRequest.getFullname());
			System.out.println("Gender: " + candidateRequest.getGender());
			System.out.println("Phone: " + candidateRequest.getPhone());
			System.out.println("Avatar: "
					+ (candidateRequest.getAvatar() != null ? candidateRequest.getAvatar().getOriginalFilename()
							: "null"));

			// Cap nhat thong tin nguoi dung
			Candidate updated = candidateService.updateInfo(id, candidateRequest);

			// Tra ket qua
			return ResponseEntity.ok(ApiResponse.<Candidate>builder().code(Code.UPDATE_INFO_SUCCEEDED.getCode())
					.message(Code.UPDATE_INFO_SUCCEEDED.getMessage()).build());
		} catch (AppException e) {
			System.out.println("Custom error: " + e.getMessage());
			return ResponseEntity.badRequest().body(ApiResponse.<Candidate>builder()
					.code(Code.UPDATE_INFO_FAILED.getCode()).message(e.getMessage()).build());
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			return ResponseEntity.internalServerError().body(ApiResponse.<Candidate>builder()
					.code(Code.UPDATE_INFO_FAILED.getCode()).message("Unexpected error occurred").build());
		}

	}

	@PostMapping("/liked-job")
	public ResponseEntity<ApiResponse> addLikedJob(@CookieValue(value = "jwt", required = false) String token,
			@RequestParam String jobId) {
		//Kiem tra token
		if (token == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Candidate>builder()
					.code(Code.TOKEN_INVALID.getCode()).message("Missing token or user not logged in").build());
		}
		String id = jwtUtils.extractId(token);
		boolean success = candidateService.addLikedJob(jobId,id);

		if (success == true) {
			return ResponseEntity.ok(ApiResponse.builder().code(Code.LIKED_JOB_ADDED.getCode())
					.message(Code.LIKED_JOB_ADDED.getMessage()).build());
		} else {
			return ResponseEntity.ok(ApiResponse.builder().code(Code.UNCATEGORIZED_EXCEPTION.getCode())
					.message(Code.UNCATEGORIZED_EXCEPTION.getMessage()).build());
		}
	}
	
	@PostMapping("/unliked-job")
	public ResponseEntity<ApiResponse> unLikedJob(@CookieValue(value = "jwt", required = false) String token,
			@RequestParam String jobId) {
		//Kiem tra token
		if (token == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Candidate>builder()
					.code(Code.TOKEN_INVALID.getCode()).message("Missing token or user not logged in").build());
		}
		String id = jwtUtils.extractId(token);
		boolean success = candidateService.unLikedJob(jobId,id);

		if (success == true) {
			return ResponseEntity.ok(ApiResponse.builder().code(Code.DELETED_SUCCESSFULLY.getCode())
					.message(Code.DELETED_SUCCESSFULLY.getMessage()).build());
		} else {
			return ResponseEntity.ok(ApiResponse.builder().code(Code.UNCATEGORIZED_EXCEPTION.getCode())
					.message(Code.UNCATEGORIZED_EXCEPTION.getMessage()).build());
		}
	}
	
	@PostMapping("/get-applied")
	public ResponseEntity<ApiResponse> getApplied(@CookieValue(value = "jwt", required = false) String token) {
		//Kiem tra token
		if (token == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Candidate>builder()
					.code(Code.TOKEN_INVALID.getCode()).message("Missing token or user not logged in").build());
		}

		List<Job> success = candidateService.getApplied();

		if (success != null) {
			return ResponseEntity.ok(ApiResponse.builder().code(Code.GET_INFO_SUCCEEDED.getCode())
					.message(Code.GET_INFO_SUCCEEDED.getMessage()).result(success).build());
		} else {
			return ResponseEntity.ok(ApiResponse.builder().code(Code.UNCATEGORIZED_EXCEPTION.getCode())
					.message(Code.UNCATEGORIZED_EXCEPTION.getMessage()).build());
		}
	}
	
	@PostMapping("/get-liked")
	public ResponseEntity<ApiResponse> getLikedJobs(@CookieValue(value = "jwt", required = false) String token) {
		//Kiem tra token
		if (token == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<Candidate>builder()
					.code(Code.TOKEN_INVALID.getCode()).message("Missing token or user not logged in").build());
		}

		List<Job> success = candidateService.getLikedJobs();

		if (success != null) {
			return ResponseEntity.ok(ApiResponse.builder().code(Code.GET_INFO_SUCCEEDED.getCode())
					.message(Code.GET_INFO_SUCCEEDED.getMessage()).result(success).build());
		} else {
			return ResponseEntity.ok(ApiResponse.builder().code(Code.UNCATEGORIZED_EXCEPTION.getCode())
					.message(Code.UNCATEGORIZED_EXCEPTION.getMessage()).build());
		}
	}

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<CandidateResponse>>> searchCandidates(
            @CookieValue(value = "jwt", required = false) String token,
            @RequestBody CandidateSearchRequest request) {
        
        // Kiểm tra token
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<List<CandidateResponse>>builder()
                    .code(Code.TOKEN_INVALID.getCode()).message("Missing token or user not logged in").build());
        }

        // Xác thực token
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.<List<CandidateResponse>>builder()
                    .code(Code.TOKEN_INVALID.getCode()).message(Code.TOKEN_INVALID.getMessage()).build());
        }

        // Kiểm tra quyền (chỉ ROLE_EMPLOYER mới được phép)
        String role = jwtUtils.extractRole(token);
        if (!Role.ROLE_EMPLOYER.name().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.<List<CandidateResponse>>builder()
                    .code(Code.ACCESS_DENIED.getCode()).message(Code.ACCESS_DENIED.getMessage()).build());
        }

        List<CandidateResponse> result = candidateService.searchCandidates(request);
        
        return ResponseEntity.ok(ApiResponse.<List<CandidateResponse>>builder()
                .code(Code.GET_INFO_SUCCEEDED.getCode())
                .message(Code.GET_INFO_SUCCEEDED.getMessage())
                .result(result)
                .build());
    }

}
