package backend.main.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.main.dto.request.noti.ReadNotiRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.enums.Code;
import backend.main.services.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/noti")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
	
	NotificationService notificationService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Object>> getAllNotiByUser(
			@RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(Code.GET_NOTI_SUCCESSFULL.getCode())
                .message(Code.GET_NOTI_SUCCESSFULL.getMessage())
                .result(notificationService.getNotiByUser(userId, pageable))
                .build());
    }
	
	@PostMapping("/read")
	public ResponseEntity<ApiResponse<Object>> readNoti(
			@RequestBody ReadNotiRequest readNotiRequest){
        return ResponseEntity.ok(ApiResponse.builder()
                .code(Code.READ_NOTI.getCode())
                .message(Code.READ_NOTI.getMessage())
                .result(notificationService.readNoti(readNotiRequest))
                .build());
    }
	
}
