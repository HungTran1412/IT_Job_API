package backend.main.services;

import java.util.List;

import backend.main.dto.request.OrderRequest;
import backend.main.entities.Order;

public interface OrderService {
    Order createOrder(OrderRequest request);
    Order getOrderById(Integer id);
    List<Order> getOrdersByEmployerId(String employerId);
    List<Order> getAllOrders();
    Order updateOrderStatus(Integer id, String status); 
}
