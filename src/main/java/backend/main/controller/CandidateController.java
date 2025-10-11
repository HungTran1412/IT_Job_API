package backend.main.controller;

import backend.main.configuration.JwtUtils;
import backend.main.dto.request.CandidateRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.dto.response.CandidateResponse;
import backend.main.entities.Candidate;
import backend.main.enums.Code;
import backend.main.exception.AppException;
import backend.main.repository.CandidateRepository;
import backend.main.services.CandidateService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class CandidateController {
    @Autowired
    CandidateService candidateService;
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    JwtUtils jwtUtils;

    public CandidateController(CandidateService candidateService, CandidateRepository candidateRepository, JwtUtils jwtUtils) {
        this.candidateService = candidateService;
        this.candidateRepository = candidateRepository;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping(value = "/info/{id}")
    public ResponseEntity<ApiResponse<CandidateResponse>> getInfo(
            @PathVariable String id,
            @RequestHeader("Authorization")  String auth) {
        try {
            String token = auth.replace("Bearer ", "");

            // Kiểm tra tính hợp lệ
            if (!jwtUtils.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CandidateResponse>builder()
                                .code(Code.TOKEN_INVALID.getCode())
                                .message(Code.TOKEN_INVALID.getMessage())
                                .build());
            }

            String email = jwtUtils.extractEmail(token);

            CandidateResponse response = candidateService.getCandidateById(id);
            Candidate candidate = candidateRepository.findById(id)
                    .orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));

            if (!candidate.getEmail().equals(email)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<CandidateResponse>builder()
                                .code(Code.CANNOT_GET_ANOTHER_INFO.getCode())
                                .message(Code.CANNOT_GET_ANOTHER_INFO.getMessage())
                                .build());
            }

            return ResponseEntity.ok(
                    ApiResponse.<CandidateResponse>builder()
                            .code(Code.GET_INFO_SUCCEEDED.getCode())
                            .message(Code.GET_INFO_SUCCEEDED.getMessage())
                            .result(response)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<CandidateResponse>builder()
                            .code(Code.UNCATEGORIZED_EXCEPTION.getCode())
                            .message(Code.UNCATEGORIZED_EXCEPTION.getMessage())
                            .build());
        }
    }

    @PutMapping(value = "/update/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<Candidate>> updateInfo(
            @PathVariable String id,
            @ModelAttribute CandidateRequest candidateRequest,
            @RequestHeader("Authorization") String authHeader) {

        System.out.println("===== [UPDATE CANDIDATE INFO] =====");
        System.out.println("Candidate ID: " + id);
        System.out.println("Token: " + authHeader);

        try {
            // Lấy token từ header
            String token = authHeader.replace("Bearer ", "");

            // Kiểm tra tính hợp lệ
            if (!jwtUtils.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<Candidate>builder()
                                .code(Code.TOKEN_INVALID.getCode())
                                .message(Code.TOKEN_INVALID.getMessage())
                                .build());
            }

            // Lấy email từ token
            String email = jwtUtils.extractEmail(token);

            // Chỉ chính chủ mới được sửa
            Candidate candidate = candidateRepository.findById(id)
                    .orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));

            if (!candidate.getEmail().equals(email)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<Candidate>builder()
                                .code(Code.CANNOT_UPDATE_ANOTHER_USER.getCode())
                                .message(Code.CANNOT_UPDATE_ANOTHER_USER.getMessage())
                                .build());
            }

            // Ghi log dữ liệu gửi lên
            System.out.println("Fullname: " + candidateRequest.getFullname());
            System.out.println("Gender: " + candidateRequest.getGender());
            System.out.println("Phone: " + candidateRequest.getPhone());
            System.out.println("Avatar: " + (candidateRequest.getAvatar() != null
                    ? candidateRequest.getAvatar().getOriginalFilename()
                    : "null"));

            // Cập nhật thông tin
            Candidate updated = candidateService.updateInfo(id, candidateRequest);

            // Trả về kết quả thành công
            return ResponseEntity.ok(ApiResponse.<Candidate>builder()
                    .code(Code.UPDATE_INFO_SUCCEEDED.getCode())
                    .message(Code.UPDATE_INFO_SUCCEEDED.getMessage())
                    .result(updated)
                    .build());

        } catch (AppException e) {
            System.out.println("Custom error: " + e.getMessage());
            // Bắt lỗi custom
            return ResponseEntity.badRequest().body(ApiResponse.<Candidate>builder()
                    .code(Code.UPDATE_INFO_FAILED.getCode())
                    .message(Code.UPDATE_INFO_FAILED.getMessage())
                    .build());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            // Bắt lỗi bất ngờ
            return ResponseEntity.internalServerError().body(ApiResponse.<Candidate>builder()
                    .code(Code.UPDATE_INFO_FAILED.getCode())
                    .message(Code.UPDATE_INFO_FAILED.getMessage())
                    .build());
        }
    }

}
