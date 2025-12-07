package backend.main.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import backend.main.utils.JwtUtils;
import backend.main.dto.request.ChangePasswordRequest;
import backend.main.dto.request.employer.EmployerUpdateRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.dto.response.EmployerResponse;
import backend.main.entities.Candidate;
import backend.main.entities.Employer;
import backend.main.enums.Code;
import backend.main.exception.AppException;
import backend.main.repository.EmployerRepository;
import backend.main.services.EmployerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/company")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Employer:",description = "Lấy, thay đổi thông tin cho ntd, lấy danh sách công việc đã đăng")
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

    @GetMapping(value = "/info")
    public ResponseEntity<ApiResponse<EmployerResponse>> getEmployerProfile(
        @CookieValue(value = "jwt", required = true) String token
    ){
        //Kiem tra xem co cookie khong
        if(token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<EmployerResponse>builder()
                            .code(Code.TOKEN_INVALID.getCode())
                            .message("Missing token or user not logged in")
                            .build());
        }

        //Xác thực token
        if(!jwtUtils.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<EmployerResponse>builder()
                            .code(Code.TOKEN_INVALID.getCode())
                            .message(Code.TOKEN_INVALID.getMessage())
                            .build());
        }

        //giải mã token: decode token
        String id = jwtUtils.extractId(token);

        //Tim nha tuyen dung theo id
        Employer e = employerRepository.findById(id)
                .orElseThrow(() -> new AppException(Code.EMPLOYER_NOT_FOUND));

        EmployerResponse response = new EmployerResponse(
                e.getCompanyName(),
                e.getCity(),
                e.getAddress(),
                e.getCompanyModel(),
                e.getCompanyEmployees(),
                e.getWorkingTime(),
                e.getWorkingOvertime(),
                e.getDescription(),
                e.getPhone(),
                e.getLogo(),
                e.getRole()
        );

        return ResponseEntity.ok(ApiResponse.<EmployerResponse>builder()
                .code(Code.GET_INFO_SUCCEEDED.getCode())
                .message(Code.GET_INFO_SUCCEEDED.getMessage())
                .result(response)
                .build());
    }

    @PatchMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<Employer>> updateInfo(
                                                  @ModelAttribute EmployerUpdateRequest request,
                                                  @CookieValue(value = "jwt", required = true) String token) {
        System.out.println("===== [UPDATE EMPLOYER INFO] =====");
        System.out.println("Token: " + token);

        //Kiem tra token
        try {
            if(token == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<Employer>builder()
                                .code(Code.TOKEN_INVALID.getCode())
                                .message("Missing token or user not logged in")
                                .build());
            }

            //Xac thuc token
            if(!jwtUtils.validateToken(token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<Employer>builder()
                                .code(Code.TOKEN_INVALID.getCode())
                                .message(Code.TOKEN_INVALID.getMessage())
                                .build());
            }

            //Lay id tu token
            String id = jwtUtils.extractId(token);

            //Kiem tra nguoi dung co ton tai khong
            Employer e = employerRepository.findById(id)
                    .orElseThrow(() -> new AppException(Code.EMPLOYER_NOT_FOUND));

            //Cap nhat thong in
            Employer updated = employerService.updateInfo(id, request);

            return ResponseEntity.ok(ApiResponse.<Employer>builder()
                    .code(Code.UPDATE_INFO_SUCCEEDED.getCode())
                    .message(Code.UPDATE_INFO_SUCCEEDED.getMessage())
                    .build());
        } catch (AppException e) {
            System.out.println("Custom error: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Employer>builder()
                            .code(Code.UPDATE_INFO_FAILED.getCode())
                            .message(e.getMessage())
                            .build());
        }catch (Exception e){
            System.out.println("Unexpected error: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<Employer>builder()
                            .code(Code.UPDATE_INFO_FAILED.getCode())
                            .message("Unexpected error occurred")
                            .build());
        }
    }

    @PatchMapping(value = "/change-password")
    public ResponseEntity<ApiResponse> changePassword(@CookieValue(value = "jwt", required = false) String token,
                                                      @RequestBody ChangePasswordRequest req) {
        //Kiem tra token
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<Candidate>builder()
                            .code(Code.TOKEN_INVALID.getCode())
                            .message("Missing token or user not logged in")
                            .build());
        }

        boolean success = employerService.changePassword(jwtUtils.extractEmail(token), req.getOldPassword(), req.getNewPassword());

        if (success == true) {
            return ResponseEntity.ok(ApiResponse.builder()
                    .code(Code.PASSWORD_CHANGED.getCode())
                    .message(Code.PASSWORD_CHANGED.getMessage())
                    .build());
        }
        else{
            return ResponseEntity.ok(ApiResponse.builder()
                    .code(Code.UNCATEGORIZED_EXCEPTION.getCode())
                    .message(Code.UNCATEGORIZED_EXCEPTION.getMessage())
                    .build());
        }
    }
    
    @GetMapping("/get-jobs")
    public ResponseEntity<ApiResponse> getJobs(@CookieValue(name = "jwt", required = false) String token) {
    	//Kiem tra token
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<Candidate>builder()
                            .code(Code.TOKEN_INVALID.getCode())
                            .message("Missing token or user not logged in")
                            .build());
        } 
        
        return ResponseEntity.ok(ApiResponse.builder()
        		.code(Code.ACCOUNT_VERIFIED.getCode())
        		.result(employerService.getListJob(token))
        		.build());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Employer>> getEmployer(@PathVariable String id) {
        Employer employer = employerService.getById(id);
        return ResponseEntity.ok(ApiResponse.<Employer>builder()
                .code(Code.GET_INFO_SUCCEEDED.getCode())
                .message(Code.GET_INFO_SUCCEEDED.getMessage())
                .result(employer)
                .build());
    }
}
