package backend.main.services.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import backend.main.dto.request.PageRequestDto;
import backend.main.dto.response.OrderStatsResponse;
import backend.main.specification.OrderSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import backend.main.dto.request.OrderRequest;
import backend.main.entities.Employer;
import backend.main.entities.Order;
import backend.main.entities.VipPackage;
import backend.main.enums.Code;
import backend.main.exception.AppException;
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
                .orElseThrow(() -> new AppException(Code.EMPLOYER_NOT_FOUND));

        VipPackage vipPackage = vipPackageRepository.findById(request.getVipPackageId())
                .orElseThrow(() -> new AppException(Code.VIP_PACKAGE_NOT_FOUND));

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
                // Tạo mã đơn hàng unique
                .code(UUID.randomUUID().toString())
                .build();

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new AppException(Code.ORDER_NOT_FOUND));
    }

    @Override
    public List<Order> getOrdersByEmployerId(String employerId) {
        return orderRepository.findByEmployer_EmployerId(employerId);
    }

    @Override
    public Page<Order> getAllOrders(PageRequestDto pageRequestDto) {
        Pageable pageable = pageRequestDto.toPageable();
        return orderRepository.findAll(pageable);
    }

    @Override
    public Order updateOrderStatus(Integer id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public OrderStatsResponse getOrderStats(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new AppException(Code.INVALID_DATE_RANGE);
        }

        LocalDateTime start = null;
        LocalDateTime end = null;

        // Nếu không truyền startDate, lấy ngày tạo của đơn hàng đầu tiên
        if (startDate == null) {
            Order firstOrder = orderRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt")).stream().findFirst().orElse(null);
            if (firstOrder != null) {
                start = firstOrder.getCreatedAt();
            }
        } else {
            start = startDate.atStartOfDay();
        }
        
        // Nếu không truyền endDate, lấy thời điểm hiện tại
        if (endDate == null) {
            end = LocalDateTime.now();
        } else {
            end = endDate.atTime(23, 59, 59);
        }

        // Chỉ lấy các đơn hàng thành công
        Specification<Order> spec = Specification.where(OrderSpec.hasStatus("SUCCESS"))
                .and(OrderSpec.createdBetween(start, end));

        List<Order> orders = orderRepository.findAll(spec);

        long totalOrders = orders.size();
        double totalRevenue = orders.stream()
                .mapToDouble(Order::getAmount)
                .sum();

        return OrderStatsResponse.builder()
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .orders(orders)
                .build();
    }
}
