package backend.main.controller;

import backend.main.configuration.JwtUtils;
import backend.main.dto.request.EmployerRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.dto.response.CandidateResponse;
import backend.main.dto.response.EmployerResponse;
import backend.main.entities.Employer;
import backend.main.enums.Code;
import backend.main.exception.AppException;
import backend.main.repository.EmployerRepository;
import backend.main.services.EmployerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/company")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployerController {
    @Autowired
    EmployerService employerService;
    @Autowired
    EmployerRepository employerRepository;
    @Autowired
    JwtUtils jwtUtils;

    public EmployerController(EmployerService employerService, EmployerRepository employerRepository, JwtUtils jwtUtils) {
        this.employerService = employerService;
        this.employerRepository = employerRepository;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping(value = "/info/{id}")
    public ResponseEntity<ApiResponse<EmployerResponse>> getInfo(
            @PathVariable String id,
            @RequestHeader("Authorization") String auth
    ){
        try {
            String token = auth.replace("Bearer ", "");

            // Kiểm tra tính hợp lệ
            if (!jwtUtils.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<EmployerResponse>builder()
                                .code(Code.TOKEN_INVALID.getCode())
                                .message(Code.TOKEN_INVALID.getMessage())
                                .build());
            }

            String email = jwtUtils.extractEmail(token);

            EmployerResponse response = employerService.getEmployerById(id);
            Employer e = employerRepository.findById(id)
                    .orElseThrow(() -> new AppException(Code.EMPLOYER_NOT_FOUND));

            if (!e.getEmail().equals(email)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<EmployerResponse>builder()
                                .code(Code.CANNOT_GET_ANOTHER_INFO.getCode())
                                .message(Code.CANNOT_GET_ANOTHER_INFO.getMessage())
                                .build());
            }
            return ResponseEntity.ok(
                    ApiResponse.<EmployerResponse>builder()
                            .code(Code.GET_INFO_SUCCEEDED.getCode())
                            .message(Code.GET_INFO_SUCCEEDED.getMessage())
                            .result(response)
                            .build()
            );
        } catch (AppException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<EmployerResponse>builder()
                            .code(Code.UNCATEGORIZED_EXCEPTION.getCode())
                            .message(Code.UNCATEGORIZED_EXCEPTION.getMessage())
                            .build());
        }
    }

    @PutMapping(value = "/update/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<Employer>> updateInfo(@PathVariable String id,
                                                  @ModelAttribute EmployerRequest request,
                                                  @RequestHeader(value = "Authorization") String authHeader) {
        System.out.println("===== [UPDATE EMPLOYER INFO] =====");
        System.out.println("Employer ID: " + id);
        System.out.println("Token: " + authHeader);

        //lay token
        try {
            String token = authHeader.replace("Bearer ", "");

            //Check valid
            if(!jwtUtils.validateToken(token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<Employer>builder()
                                .code(Code.TOKEN_INVALID.getCode())
                                .message(Code.TOKEN_INVALID.getMessage())
                                .build());
            }

            //Lay email va role
            String email = jwtUtils.extractEmail(token);

            //Chinh chinh nguoi dung moi duoc sua
            Employer employer = employerRepository.findById(id)
                            .orElseThrow(() -> new AppException(Code.EMPLOYER_NOT_FOUND));
            System.out.println("EmaiLLLLLLLL: " + employer.getEmail());

            if(!employer.getEmail().equals(email)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<Employer>builder()
                                .code(Code.CANNOT_UPDATE_ANOTHER_USER.getCode())
                                .message(Code.CANNOT_UPDATE_ANOTHER_USER.getMessage())
                                .build());
            }

            //Update
            Employer updated = employerService.updateInfo(id, request);
            return  ResponseEntity.ok(ApiResponse.<Employer>builder()
                    .code(Code.UPDATE_INFO_SUCCEEDED.getCode())
                    .message(Code.UPDATE_INFO_SUCCEEDED.getMessage())
                    .result(updated)
                    .build());
        } catch (AppException e) {
            System.out.println("Custom error: " + e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.<Employer>builder()
                    .code(Code.UPDATE_INFO_FAILED.getCode())
                    .message(Code.UPDATE_INFO_FAILED.getMessage())
                    .build());
        }catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            // Bắt lỗi bất ngờ
            return ResponseEntity.internalServerError().body(ApiResponse.<Employer>builder()
                    .code(Code.UPDATE_INFO_FAILED.getCode())
                    .message(Code.UPDATE_INFO_FAILED.getMessage())
                    .build());
        }
    }
}
