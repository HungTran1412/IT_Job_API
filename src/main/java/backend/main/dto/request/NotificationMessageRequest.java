package backend.main.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessageRequest {
    private String to; // "admin" or specific user ID
    private String from;
    private String content;
}
