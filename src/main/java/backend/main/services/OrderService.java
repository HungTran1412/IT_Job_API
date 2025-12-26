package backend.main.services;

import java.util.List;

import backend.main.dto.request.OrderRequest;
import backend.main.entities.Order;

public interface OrderService {
    Order createOrder(OrderRequest request);
    Order getOrderById(String id);
    List<Order> getOrdersByEmployerId(String employerId);
    List<Order> getAllOrders();
    Order updateOrderStatus(String id, String status); // Dùng để update thủ công hoặc test
}
