package backend.main.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.main.dto.response.ApiResponse;
import backend.main.enums.Code;
import backend.main.services.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/noti")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationController {
	
	NotificationService notificationService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Object>> getAllNotiByUser(
			@RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(Code.GET_JOB_SUCCESSFULL.getCode())
                .message(Code.GET_JOB_SUCCESSFULL.getMessage())
                .result(notificationService.getNotiByUser(userId, pageable))
                .build());
    }
	
}
