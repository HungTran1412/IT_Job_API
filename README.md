# ITJobAPI

## Giới thiệu
**ITJobAPI** là hệ thống Backend RESTful API phục vụ cho nền tảng tuyển dụng việc làm IT. Hệ thống kết nối Ứng viên (Candidate) và Nhà tuyển dụng (Employer), cung cấp các tính năng quản lý tin tuyển dụng, ứng tuyển, gói dịch vụ VIP và thanh toán trực tuyến.

Dự án được xây dựng trên nền tảng **Spring Boot**, bảo mật với **Spring Security & JWT**, và tích hợp nhiều dịch vụ bên thứ ba như **Cloudinary** (lưu trữ media), **VNPay** (thanh toán), **Gmail SMTP** (gửi mail).

---

## 🚀 Công nghệ sử dụng
- **Core**: Java 21, Spring Boot 3.x
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA, Hibernate
- **Security**: Spring Security, JWT (HttpOnly Cookie), BCrypt
- **Real-time**: Server-Sent Events (SSE)
- **Payment**: VNPay Integration
- **Storage**: Cloudinary
- **Mail**: Java Mail Sender
- **Documentation**: OpenAPI / Swagger UI
- **Tools**: Maven, Docker, Docker Compose, Lombok

---

## 📦 Cài đặt & Chạy ứng dụng

### Yêu cầu
- JDK 21
- Maven
- Docker & Docker Compose (tùy chọn)
- MySQL

### Cấu hình biến môi trường
Tạo file `.env` từ file `.env.example` và điền các thông tin cấu hình:
```properties
# Database
CONNECTION_STRING=jdbc:mysql://localhost:3306/itjob_db
CONNECTION_USERNAME=root
CONNECTION_PASSWORD=root

# JWT
JWT_SECRET=your_super_secret_key

# Cloudinary
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# Mail
SPRING_MAIL_USERNAME=your_email@gmail.com
SPRING_MAIL_PASSWORD=your_app_password

# VNPay
TMN_CODE=your_tmn_code
SECRET_KEY=your_secret_key
PAY_URL=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
API_URL=https://sandbox.vnpayment.vn/merchant_webapi/api/transaction
RETURN_URL=/api/payment/vnpay-return

# Frontend URLs
BASE_URL=http://localhost:8080
FE_URL = http://localhost:3000
VERIFY_TOKEN_CANDIDATE=...
VERIFY_TOKEN_EMPLOYER=...
VERIFIED_CANDIDATE_URL=...
VERIFIED_EMPLOYER_URL=...
```

### Chạy với Docker Compose
```bash
docker-compose up -d --build
```

### Chạy thủ công
1. Cài đặt dependencies:
```bash
mvn clean install
```
2. Chạy ứng dụng:
```bash
mvn spring-boot:run
```

---

## 📚 API Documentation
Sau khi chạy ứng dụng, truy cập Swagger UI để xem tài liệu API chi tiết:
- **Local**: `http://localhost:8080/swagger-ui/index.html`
- **Ngrok**: `https://<your-ngrok-url>/swagger-ui/index.html`

---

## 🌟 Tính năng chính & Endpoints

### 1. Xác thực & Phân quyền (Auth)
Hệ thống sử dụng JWT lưu trong HttpOnly Cookie.
- **Candidate**: Đăng ký, Đăng nhập, Xác thực email, Quên mật khẩu.
- **Employer**: Đăng ký, Đăng nhập, Xác thực email, Quên mật khẩu.
- **Admin**: Đăng nhập quản trị.

### 2. Ứng viên (Candidate)
- `GET /user/info`: Xem thông tin cá nhân.
- `PUT /user/update`: Cập nhật hồ sơ (Avatar, CV).
- `POST /user/search`: Tìm kiếm ứng viên (dành cho Employer).
- `POST /user/liked-job`: Lưu công việc yêu thích.
- `POST /api/applications`: Ứng tuyển công việc.

### 3. Nhà tuyển dụng (Employer)
- `GET /company/info`: Xem thông tin công ty.
- `PUT /company/update`: Cập nhật thông tin công ty.
- `POST /api/jobs`: Đăng tin tuyển dụng.
- `GET /company/get-jobs`: Quản lý danh sách tin đã đăng.
- `GET /api/orders/employer/{id}`: Xem lịch sử đơn hàng.

### 4. Tin tuyển dụng (Job)
- `GET /api/jobs`: Danh sách tin tuyển dụng (phân trang, sắp xếp).
- `POST /api/jobs/search`: Tìm kiếm tin tuyển dụng.
- `PUT /api/jobs/review`: Duyệt tin tuyển dụng (Admin).

### 5. Dịch vụ VIP & Thanh toán (Payment)
- `GET /api/vip-packages`: Danh sách gói VIP.
- `POST /api/orders`: Tạo đơn hàng mua gói VIP.
- `GET /api/payment/create-payment`: Tạo URL thanh toán VNPay.
- `GET /api/payment/vnpay-callback`: Xử lý kết quả thanh toán từ VNPay.

### 6. Thông báo & Real-time
- `GET /sse/subscribe`: Đăng ký nhận thông báo realtime (SSE).
- `POST /noti`: Lấy danh sách thông báo.

---

## 📂 Cấu trúc dự án
```
src/main/java/backend/main
├── configuration    # Config Security, Swagger, Cloudinary...
├── controller       # API Controllers
├── dto              # Data Transfer Objects (Request/Response)
├── entities         # JPA Entities
├── enums            # Enumerations (Role, Status, Code...)
├── exception        # Global Exception Handling
├── repository       # JPA Repositories
├── services         # Business Logic Layer
└── utils            # Utilities (JWT, VNPay, SSE...)
```
