package backend.main.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import backend.main.dto.request.OrderRequest;
import backend.main.dto.request.PageRequestDto;
import backend.main.dto.response.OrderStatsResponse;
import backend.main.entities.Order;

public interface OrderService {
    Order createOrder(OrderRequest request);
    Order getOrderById(Integer id);
    List<Order> getOrdersByEmployerId(String employerId);
    Page<Order> getAllOrders(PageRequestDto pageRequestDto);
    Order updateOrderStatus(Integer id, String status); 
    OrderStatsResponse getOrderStats(LocalDate startDate, LocalDate endDate);
}
