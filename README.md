# ITJobAPI

## Giới thiệu
**ITJobAPI** là một RESTful API được xây dựng bằng **Spring Boot** phục vụ cho nền tảng tuyển dụng IT, cho phép:
- Ứng viên (Candidate) đăng ký, đăng nhập, xem và cập nhật thông tin cá nhân.
- Nhà tuyển dụng (Employer) đăng ký, đăng nhập, xem và cập nhật thông tin công ty.
- Xác thực và phân quyền người dùng bằng **JWT** lưu trong **Cookie** an toàn.

---

## Kiến trúc & Công nghệ
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security** (JWT Authentication/ BCryptPasswordEncoder)
- **Spring Data JPA** (truy cập database)
- **MySQL**
- **Cloudinary** (lưu ảnh/avatar/CV)
- **Lombok**
- **Postman** (test API)

---

## 📂 Cấu trúc thư mục chính

```
src
 └── main
     ├── java
     │   └── backend.main
     │       ├── configuration        # Cấu hình bảo mật, JWT, Cloudinary
     │       ├── controller           # Các REST Controller (Candidate / Employer)
     │       ├── dto
     │       │   ├── request          # Dữ liệu đầu vào (Register, Login, Update)
     │       │   └── response         # Dữ liệu phản hồi (ApiResponse, User info)
     │       ├── entities             # Entity ánh xạ bảng DB
     │       ├── enums                # Enum định nghĩa mã lỗi, vai trò
     │       ├── exception            # Xử lý ngoại lệ toàn cục
     │       ├── repository           # DAO Layer - JPA Repository
     │       ├── services             # Business logic
     │       └── utils                # Class tiện ích
     └── resources
         ├── application.yaml         # Cấu hình ứng dụng
         └── static / templates       # (Tùy chọn)
```
---
##  Xác thực & Phân quyền

Ứng dụng sử dụng **JWT (JSON Web Token)** để xác thực và lưu token trong **HttpOnly Cookie** nhằm tăng tính bảo mật.

## API Endpoints
### Candidate APIs

| Method | Endpoint         | Mô tả                        | Yêu cầu JWT |
| ------ | ---------------- | ---------------------------- | ----------- |
| `POST` | `/user/register` | Đăng ký tài khoản ứng viên   | ❌           |
| `POST` | `/user/login`    | Đăng nhập, trả về cookie JWT | ❌           |
| `GET`  | `/user/info`     | Xem thông tin cá nhân        | ✅           |
| `PUT`  | `/user/update`   | Cập nhật thông tin cá nhân   | ✅           |

### Employer APIs
| Method | Endpoint            | Mô tả                        | Yêu cầu JWT |
| ------ | ------------------- | ---------------------------- | ----------- |
| `POST` | `/company/register` | Đăng ký tài khoản công ty    | ❌           |
| `POST` | `/company/login`    | Đăng nhập, trả về cookie JWT | ❌           |
| `GET`  | `/company/info`     | Xem thông tin công ty        | ✅           |
| `PUT`  | `/company/update`   | Cập nhật thông tin công ty   | ✅           |

## Mã ứng dụng phổ biến
| Code | Message                | Mô tả                           |
|------|------------------------| ------------------------------- |
| 2001 | Login succeeded!       | Đăng nhập thành công            |
| 2006 | Get info succeeded     | Lấy thông tin thành công        |
| 1006 | Token invalid          | Token không hợp lệ hoặc hết hạn |
| 1005 | Candidate not found    | Không tìm thấy người dùng       |
| 1836 | Uncategorized error    | Lỗi chưa được phân loại   |
