package backend.main.dto.request;

import backend.main.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageRequestDto {
    int page = 0;
    int size = 10;
    String sortBy = "createdAt";
    String sortDirection = "DESC";
    String role; // Thêm trường role để lọc

    public Pageable toPageable() {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}
