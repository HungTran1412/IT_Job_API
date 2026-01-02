package backend.main.dto.response;

import java.util.List;

import backend.main.entities.Order;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatsResponse {
    long totalOrders;
    Double totalRevenue;
    List<Order> orders;
}
