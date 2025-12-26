package backend.main.services.Impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import backend.main.dto.request.OrderRequest;
import backend.main.entities.Employer;
import backend.main.entities.Order;
import backend.main.entities.VipPackage;
import backend.main.repository.EmployerRepository;
import backend.main.repository.OrderRepository;
import backend.main.repository.VipPackageRepository;
import backend.main.services.OrderService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final EmployerRepository employerRepository;
    private final VipPackageRepository vipPackageRepository;

    @Override
    public Order createOrder(OrderRequest request) {
        Employer employer = employerRepository.findById(request.getEmployerId())
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        VipPackage vipPackage = vipPackageRepository.findById(request.getVipPackageId())
                .orElseThrow(() -> new RuntimeException("Vip Package not found"));

        // Nếu request không gửi amount thì lấy giá của gói VIP
        Double amount = request.getAmount() != null ? request.getAmount() : vipPackage.getPrice();

        Order order = Order.builder()
                .employer(employer)
                .vipPackage(vipPackage)
                .amount(amount)
                .status("PENDING")
                .orderInfo(request.getOrderInfo())
                // Tạo mã giao dịch unique cho VNPay
                .vnpTxnRef(UUID.randomUUID().toString()) 
                .build();

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getOrdersByEmployerId(String employerId) {
        return orderRepository.findByEmployer_EmployerId(employerId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(String id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
