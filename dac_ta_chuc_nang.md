# Đặc Tả Yêu Cầu Chức Năng - IT Job API

Tài liệu này mô tả chi tiết các chức năng của hệ thống IT Job API, dựa trên các yêu cầu đã được xác định.

---

## Phần 1: Chức năng cho Người dùng không xác thực (Guest)

### **F1.1: Xem danh sách công việc**

-   **ID:** F1.1
-   **Tên chức năng:** Xem danh sách các công việc đang tuyển dụng (có phân trang).
-   **Actor:** Guest (Người dùng không xác thực).
-   **Mô tả:** Chức năng cho phép bất kỳ người dùng nào cũng có thể xem danh sách các công việc đang ở trạng thái hoạt động trên hệ thống. Kết quả được trả về theo từng trang (phân trang) để tối ưu trải nghiệm và hiệu năng.
-   **Điều kiện tiên quyết (Preconditions):** Không có.
-   **Kết quả sau cùng (Postconditions):**
    -   Hệ thống hiển thị danh sách các công việc hợp lệ.
    -   Dữ liệu trả về bao gồm thông tin phân trang (tổng số mục, tổng số trang, trang hiện tại).
-   **Luồng xử lý chính (Main Flow):**
    1.  Người dùng gửi yêu cầu đến endpoint API để lấy danh sách công việc (ví dụ: `GET /api/jobs`).
    2.  Hệ thống truy vấn cơ sở dữ liệu để lấy danh sách các công việc có trạng thái "đã được duyệt" và "còn hạn".
    3.  Hệ thống trả về một danh sách các công việc, mỗi công việc bao gồm các thông tin cơ bản (Tên công việc, Tên công ty, Địa điểm, Mức lương) cùng với dữ liệu phân trang.
-   **Luồng xử lý phụ và lỗi (Alternative/Exception Flows):**
    -   **AE1:** Nếu không có công việc nào tồn tại, hệ thống trả về một danh sách rỗng (`[]`) và thông tin phân trang tương ứng.
    -   **AE2:** Nếu tham số phân trang (ví dụ: `page`, `size`) không hợp lệ, hệ thống trả về lỗi `400 Bad Request`.
-   **Dữ liệu xử lý:**
    -   **Đầu vào (Input):** (Tùy chọn) Query params `page` (số trang), `size` (số mục mỗi trang).
    -   **Đầu ra (Output):** Đối tượng JSON chứa mảng dữ liệu công việc và các thuộc tính phân trang.

---

### **F1.2: Tìm kiếm và lọc công việc**

-   **ID:** F1.2
-   **Tên chức năng:** Tìm kiếm và lọc công việc theo các tiêu chí cơ bản.
-   **Actor:** Guest.
-   **Mô tả:** Cho phép người dùng tìm kiếm công việc dựa trên từ khóa và lọc kết quả theo địa điểm, ngành nghề.
-   **Điều kiện tiên quyết:** Không có.
-   **Kết quả sau cùng:** Hệ thống trả về danh sách các công việc phù hợp với tiêu chí tìm kiếm và lọc.
-   **Luồng xử lý chính:**
    1.  Người dùng nhập từ khóa tìm kiếm và chọn các bộ lọc (địa điểm, ngành nghề) trên giao diện.
    2.  Giao diện gửi yêu cầu đến API (ví dụ: `GET /api/jobs/search?keyword=Java&location=Hanoi`).
    3.  Hệ thống xây dựng câu truy vấn động dựa trên các tham số nhận được.
    4.  Hệ thống trả về danh sách các công việc thỏa mãn điều kiện, có hỗ trợ phân trang.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** Nếu không tìm thấy công việc nào phù hợp, hệ thống trả về danh sách rỗng.
-   **Dữ liệu xử lý:**
    -   **Đầu vào (Input):** Query params như `keyword`, `location`, `category`, `page`, `size`.
    -   **Đầu ra (Output):** Đối tượng JSON chứa mảng dữ liệu công việc và thông tin phân trang.

---

### **F1.3: Xem chi tiết công việc**

-   **ID:** F1.3
-   **Tên chức năng:** Xem chi tiết một tin tuyển dụng.
-   **Actor:** Guest.
-   **Mô tả:** Hiển thị toàn bộ thông tin chi tiết về một công việc cụ thể.
-   **Điều kiện tiên quyết:** Công việc phải tồn tại và đang ở trạng thái hoạt động.
-   **Kết quả sau cùng:** Hệ thống hiển thị trang chi tiết của công việc.
-   **Luồng xử lý chính:**
    1.  Người dùng chọn một công việc từ danh sách.
    2.  Hệ thống gửi yêu cầu đến API với ID của công việc (ví dụ: `GET /api/jobs/{jobId}`).
    3.  Hệ thống truy vấn CSDL để lấy toàn bộ thông tin của công việc đó.
    4.  Hệ thống trả về đối tượng JSON chứa đầy đủ chi tiết: mô tả công việc, yêu cầu, phúc lợi, thông tin công ty, hạn nộp hồ sơ...
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** Nếu `jobId` không tồn tại, hệ thống trả về lỗi `404 Not Found`.
-   **Dữ liệu xử lý:**
    -   **Đầu vào (Input):** Path variable `jobId`.
    -   **Đầu ra (Output):** Đối tượng JSON chứa chi tiết công việc.

---

### **F1.4: Đăng ký tài khoản**

-   **ID:** F1.4
-   **Tên chức năng:** Đăng ký tài khoản mới.
-   **Actor:** Guest.
-   **Mô tả:** Cho phép người dùng mới tạo tài khoản với vai trò là Ứng viên (Candidate) hoặc Nhà tuyển dụng (Employer).
-   **Điều kiện tiên quyết:** Người dùng chưa có tài khoản với email sắp đăng ký.
-   **Kết quả sau cùng:**
    -   Tài khoản mới được tạo trong hệ thống.
    -   Hệ thống gửi email xác thực (nếu có).
    -   Người dùng được chuyển đến trang đăng nhập hoặc trang chào mừng.
-   **Luồng xử lý chính:**
    1.  Người dùng chọn vai trò và điền thông tin vào form đăng ký (tên, email, mật khẩu).
    2.  Hệ thống gửi yêu cầu đến API (ví dụ: `POST /api/auth/register/candidate`).
    3.  Hệ thống kiểm tra tính hợp lệ của dữ liệu (email đúng định dạng, mật khẩu đủ mạnh).
    4.  Hệ thống kiểm tra xem email đã tồn tại trong CSDL chưa.
    5.  Hệ thống mã hóa mật khẩu.
    6.  Hệ thống lưu thông tin người dùng mới vào CSDL.
    7.  Hệ thống trả về thông báo đăng ký thành công.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** Nếu email đã tồn tại, hệ thống trả về lỗi `409 Conflict` với thông báo "Email đã được sử dụng".
    -   **AE2:** Nếu dữ liệu đầu vào không hợp lệ (ví dụ: email sai định dạng), hệ thống trả về lỗi `400 Bad Request`.
-   **Dữ liệu xử lý:**
    -   **Đầu vào (Input):** Đối tượng JSON chứa thông tin đăng ký (ví dụ: `email`, `password`, `fullName`...).
    -   **Đầu ra (Output):** Đối tượng JSON chứa thông báo thành công.

---

## Phần 2: Chức năng cho Ứng viên (Candidate)

*Ghi chú: Các chức năng trong phần này yêu cầu người dùng phải đăng nhập với vai trò "Candidate". Mọi yêu cầu API phải đính kèm token xác thực (JWT).*

### **F2.1.1: Đăng nhập, Đăng xuất, Đổi mật khẩu**

-   **ID:** F2.1.1
-   **Tên chức năng:** Quản lý phiên đăng nhập và mật khẩu.
-   **Actor:** Candidate.
-   **Mô tả:**
    -   **Đăng nhập:** Xác thực người dùng và cấp token truy cập.
    -   **Đăng xuất:** Hủy bỏ token hiện tại.
    -   **Đổi mật khẩu:** Cho phép người dùng thay đổi mật khẩu sau khi đã đăng nhập.
-   **Điều kiện tiên quyết:**
    -   *Đăng nhập:* Người dùng phải có tài khoản.
    -   *Đăng xuất, Đổi mật khẩu:* Người dùng phải đang đăng nhập.
-   **Kết quả sau cùng:**
    -   *Đăng nhập:* Người dùng nhận được token và được phép truy cập các tài nguyên dành cho Candidate.
    -   *Đăng xuất:* Token không còn hợp lệ.
    -   *Đổi mật khẩu:* Mật khẩu mới được cập nhật trong CSDL.
-   **Luồng xử lý chính:**
    -   **Đăng nhập (`POST /api/auth/login`):**
        1. Người dùng nhập email và mật khẩu.
        2. Hệ thống kiểm tra thông tin.
        3. Nếu hợp lệ, hệ thống tạo JWT và trả về cho người dùng.
    -   **Đăng xuất (`POST /api/auth/logout`):**
        1. Người dùng gửi yêu cầu đăng xuất.
        2. Hệ thống thêm token vào danh sách đen (blacklist) hoặc sử dụng cơ chế khác để vô hiệu hóa token.
        3. Hệ thống trả về thông báo thành công.
    -   **Đổi mật khẩu (`PUT /api/candidate/change-password`):**
        1. Người dùng nhập mật khẩu cũ, mật khẩu mới, và xác nhận mật khẩu mới.
        2. Hệ thống kiểm tra mật khẩu cũ có khớp không.
        3. Nếu khớp, hệ thống mã hóa và cập nhật mật khẩu mới.
        4. Hệ thống trả về thông báo thành công.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1 (Đăng nhập):** Sai email hoặc mật khẩu -> Lỗi `401 Unauthorized`.
    -   **AE2 (Đổi mật khẩu):** Mật khẩu cũ không đúng -> Lỗi `400 Bad Request`.
    -   **AE3 (Đổi mật khẩu):** Mật khẩu mới không khớp với xác nhận -> Lỗi `400 Bad Request`.

---

### **F2.1.2: Quản lý hồ sơ cá nhân**

-   **ID:** F2.1.2
-   **Tên chức năng:** Quản lý (thêm, sửa, xóa) thông tin hồ sơ cá nhân.
-   **Actor:** Candidate.
-   **Mô tả:** Cho phép ứng viên cập nhật các thông tin cá nhân làm cơ sở cho nhà tuyển dụng xem xét.
-   **Điều kiện tiên quyết:** Người dùng đã đăng nhập.
-   **Kết quả sau cùng:** Thông tin hồ sơ của ứng viên trong CSDL được cập nhật.
-   **Luồng xử lý chính:**
    1.  Người dùng truy cập trang quản lý hồ sơ.
    2.  Hệ thống hiển thị thông tin hiện tại (`GET /api/candidate/profile`).
    3.  Người dùng chỉnh sửa thông tin (thông tin liên hệ, học vấn, kinh nghiệm, kỹ năng) và lưu lại.
    4.  Hệ thống gửi yêu cầu cập nhật đến API (`PUT /api/candidate/profile`).
    5.  Hệ thống kiểm tra và lưu thông tin mới vào CSDL.
    6.  Hệ thống trả về thông báo thành công cùng hồ sơ đã được cập nhật.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** Dữ liệu đầu vào không hợp lệ -> Lỗi `400 Bad Request`.
-   **Dữ liệu xử lý:**
    -   **Đầu vào (Input):** Đối tượng JSON chứa các trường thông tin cần cập nhật.
    -   **Đầu ra (Output):** Đối tượng JSON chứa hồ sơ đã được cập nhật.

---

### **F2.2.2: Nộp hồ sơ ứng tuyển**

-   **ID:** F2.2.2
-   **Tên chức năng:** Nộp hồ sơ ứng tuyển vào một công việc.
-   **Actor:** Candidate.
-   **Mô tả:** Cho phép ứng viên nộp CV của mình cho một tin tuyển dụng cụ thể.
-   **Điều kiện tiên quyết:**
    -   Người dùng đã đăng nhập.
    -   Ứng viên đã tải lên ít nhất một CV.
    -   Công việc đang ở trạng thái "còn hạn" tuyển dụng.
-   **Kết quả sau cùng:**
    -   Một bản ghi "ứng tuyển" mới được tạo, liên kết giữa ứng viên, công việc và CV được sử dụng.
    -   Trạng thái đơn ứng tuyển được đặt là "Chờ duyệt".
    -   (Tùy chọn) Gửi thông báo cho nhà tuyển dụng về lượt ứng tuyển mới.
-   **Luồng xử lý chính:**
    1.  Ứng viên ở trang chi tiết công việc và nhấn nút "Ứng tuyển".
    2.  Hệ thống hiển thị tùy chọn để ứng viên chọn một trong các CV đã tải lên.
    3.  Ứng viên xác nhận nộp hồ sơ.
    4.  Hệ thống gửi yêu cầu đến API (`POST /api/applications`).
    5.  Hệ thống kiểm tra xem ứng viên đã ứng tuyển công việc này trước đó chưa.
    6.  Hệ thống tạo một bản ghi `Application` mới trong CSDL.
    7.  Hệ thống trả về thông báo ứng tuyển thành công.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** Nếu ứng viên đã ứng tuyển công việc này rồi -> Lỗi `409 Conflict`.
    -   **AE2:** Nếu công việc không còn tồn tại hoặc đã hết hạn -> Lỗi `404 Not Found` hoặc `400 Bad Request`.
    -   **AE3:** Nếu CV được chọn không tồn tại -> Lỗi `404 Not Found`.

---

### **F2.1.3: Tải lên và quản lý CV**

-   **ID:** F2.1.3
-   **Tên chức năng:** Tải lên và quản lý CV.
-   **Actor:** Candidate.
-   **Mô tả:** Cho phép ứng viên tải lên nhiều tệp CV (ví dụ: CV tiếng Anh, CV tiếng Việt) và quản lý chúng (xem danh sách, xóa).
-   **Điều kiện tiên quyết:** Người dùng đã đăng nhập.
-   **Kết quả sau cùng:** Tệp CV của ứng viên được lưu trữ an toàn và thông tin về tệp được ghi vào CSDL.
-   **Luồng xử lý chính:**
    1.  **Tải lên (`POST /api/candidate/cvs`):**
        -   Người dùng chọn một tệp (PDF, DOCX) từ máy tính.
        -   Hệ thống gửi tệp dưới dạng `multipart/form-data`.
        -   API nhận tệp, xác thực định dạng và kích thước.
        -   Hệ thống lưu tệp vào dịch vụ lưu trữ (ví dụ: Cloudinary, S3) và lấy về URL.
        -   Hệ thống lưu URL và các thông tin meta (tên tệp, ngày tải lên) vào CSDL, gắn với tài khoản ứng viên.
        -   Hệ thống trả về thông tin của CV vừa tải lên.
    2.  **Xem danh sách (`GET /api/candidate/cvs`):**
        -   Hệ thống truy vấn và trả về danh sách các CV của ứng viên.
    3.  **Xóa (`DELETE /api/candidate/cvs/{cvId}`):**
        -   Người dùng chọn CV cần xóa.
        -   Hệ thống xóa bản ghi trong CSDL và tệp trên dịch vụ lưu trữ.
        -   Hệ thống trả về thông báo thành công.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1 (Tải lên):** Tệp không đúng định dạng hoặc quá lớn -> Lỗi `400 Bad Request`.
    -   **AE2 (Xóa):** `cvId` không tồn tại hoặc không thuộc sở hữu của người dùng -> Lỗi `404 Not Found`.

---

### **F2.2.1: Tìm kiếm nâng cao**

-   **ID:** F2.2.1
-   **Tên chức năng:** Tìm kiếm và lọc công việc nâng cao.
-   **Actor:** Candidate.
-   **Mô tả:** Mở rộng chức năng tìm kiếm cơ bản, cho phép lọc thêm theo các tiêu chí như mức lương, cấp bậc, loại hình công việc (full-time, part-time).
-   **Luồng xử lý:** Tương tự **F1.2** nhưng có thêm các tham số lọc trong yêu cầu API.
-   **Dữ liệu xử lý:**
    -   **Đầu vào (Input):** Bổ sung các query params như `minSalary`, `maxSalary`, `level`, `workType`.
    -   **Đầu ra (Output):** Đối tượng JSON chứa mảng dữ liệu công việc và thông tin phân trang.

---

### **F2.2.3 & F2.2.4: Lưu và xem các công việc quan tâm**

-   **ID:** F2.2.3, F2.2.4
-   **Tên chức năng:** Lưu lại công việc quan tâm và xem danh sách đã lưu.
-   **Actor:** Candidate.
-   **Mô tả:** Cho phép ứng viên "thích" một công việc để xem lại sau.
-   **Điều kiện tiên quyết:** Người dùng đã đăng nhập.
-   **Kết quả sau cùng:**
    -   Một liên kết giữa ứng viên và công việc được tạo/xóa trong bảng `liked_jobs`.
    -   Hệ thống hiển thị đúng danh sách các công việc đã lưu.
-   **Luồng xử lý chính:**
    1.  **Lưu/Bỏ lưu (`POST /api/candidate/liked-jobs`):**
        -   Người dùng nhấn nút "Thích" trên một công việc.
        -   Hệ thống gửi yêu cầu chứa `jobId`.
        -   API kiểm tra nếu chưa thích thì sẽ tạo liên kết, nếu đã thích thì sẽ xóa liên kết (toggle).
        -   API trả về trạng thái mới.
    2.  **Xem danh sách (`GET /api/candidate/liked-jobs`):**
        -   Hệ thống truy vấn CSDL để lấy danh sách các `jobId` mà ứng viên đã thích.
        -   Hệ thống trả về danh sách chi tiết các công việc tương ứng.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** `jobId` không tồn tại -> Lỗi `404 Not Found`.

---

### **F2.2.5: Xem lịch sử ứng tuyển**

-   **ID:** F2.2.5
-   **Tên chức năng:** Xem lịch sử các công việc đã ứng tuyển và trạng thái.
-   **Actor:** Candidate.
-   **Mô tả:** Cung cấp cho ứng viên một danh sách các công việc họ đã nộp hồ sơ, cùng với trạng thái của từng đơn (ví dụ: Chờ duyệt, Đã xem, Phù hợp, Từ chối).
-   **Điều kiện tiên quyết:** Người dùng đã đăng nhập.
-   **Kết quả sau cùng:** Hệ thống hiển thị danh sách các đơn ứng tuyển và trạng thái tương ứng.
-   **Luồng xử lý chính:**
    1.  Người dùng truy cập trang "Việc làm đã ứng tuyển".
    2.  Hệ thống gửi yêu cầu đến API (`GET /api/applications/candidate`).
    3.  API truy vấn bảng `applications` để lấy tất cả các bản ghi thuộc về ứng viên hiện tại.
    4.  Hệ thống trả về một danh sách, mỗi mục bao gồm thông tin công việc và trạng thái của đơn ứng tuyển.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** Nếu chưa ứng tuyển công việc nào, hệ thống trả về danh sách rỗng.

---

## Phần 3: Chức năng cho Nhà tuyển dụng (Employer)

*Ghi chú: Các chức năng trong phần này yêu cầu người dùng phải đăng nhập với vai trò "Employer".*

### **F3.1.2: Quản lý thông tin công ty**

-   **ID:** F3.1.2
-   **Tên chức năng:** Quản lý (thêm, sửa) thông tin công ty.
-   **Actor:** Employer.
-   **Mô tả:** Cho phép nhà tuyển dụng cập nhật thông tin về công ty của họ (tên, địa chỉ, logo, mô tả).
-   **Luồng xử lý:** Tương tự **F2.1.2**, nhưng với các endpoint và dữ liệu dành cho Employer (ví dụ: `GET /api/employer/profile`, `PUT /api/employer/profile`).

---

### **F3.2.1: Đăng tin tuyển dụng mới**

-   **ID:** F3.2.1
-   **Tên chức năng:** Đăng tin tuyển dụng mới.
-   **Actor:** Employer.
-   **Mô tả:** Cho phép nhà tuyển dụng tạo một tin tuyển dụng mới với đầy đủ thông tin chi tiết.
-   **Điều kiện tiên quyết:** Nhà tuyển dụng đã đăng nhập và đã cập nhật thông tin công ty.
-   **Kết quả sau cùng:**
    -   Một tin tuyển dụng mới được tạo trong CSDL với trạng thái "Chờ duyệt" (pending).
    -   (Tùy chọn) Gửi thông báo cho Admin về tin đăng mới.
-   **Luồng xử lý chính:**
    1.  Nhà tuyển dụng điền form thông tin tuyển dụng.
    2.  Hệ thống gửi yêu cầu `POST /api/jobs`.
    3.  API xác thực dữ liệu.
    4.  Hệ thống lưu tin tuyển dụng mới vào CSDL với `status = 'PENDING'`.
    5.  Hệ thống trả về thông báo thành công.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** Dữ liệu không hợp lệ -> Lỗi `400 Bad Request`.

---

### **F3.2.2: Quản lý các tin đã đăng**

-   **ID:** F3.2.2
-   **Tên chức năng:** Quản lý các tin đã đăng (xem, sửa, ẩn/hiện, xóa).
-   **Actor:** Employer.
-   **Mô tả:** Cung cấp cho nhà tuyển dụng một giao diện để quản lý vòng đời các tin tuyển dụng của họ.
-   **Điều kiện tiên quyết:** Đã đăng nhập.
-   **Luồng xử lý chính:**
    1.  **Xem danh sách (`GET /api/employer/jobs`):** Lấy tất cả các tin tuyển dụng do nhà tuyển dụng hiện tại tạo.
    2.  **Sửa (`PUT /api/jobs/{jobId}`):** Cập nhật thông tin của một tin đăng.
    3.  **Ẩn/Hiện (`PATCH /api/jobs/{jobId}/status`):** Thay đổi trạng thái của tin đăng (ví dụ: từ `OPEN` sang `CLOSED` và ngược lại).
    4.  **Xóa (`DELETE /api/jobs/{jobId}`):** Xóa mềm (soft delete) một tin tuyển dụng.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** `jobId` không tồn tại hoặc không thuộc sở hữu của nhà tuyển dụng -> Lỗi `404 Not Found`.

---

### **F3.2.3 & F3.2.4: Xem danh sách ứng viên và chi tiết hồ sơ**

-   **ID:** F3.2.3, F3.2.4
-   **Tên chức năng:** Xem danh sách ứng viên và xem chi tiết hồ sơ.
-   **Actor:** Employer.
-   **Mô tả:** Cho phép nhà tuyển dụng xem những ai đã ứng tuyển vào công việc của họ và xem hồ sơ chi tiết của từng người.
-   **Điều kiện tiên quyết:** Đã đăng nhập.
-   **Luồng xử lý chính:**
    1.  **Xem danh sách (`GET /api/applications/job/{jobId}`):**
        -   Nhà tuyển dụng chọn một tin đăng.
        -   API trả về danh sách các ứng viên đã nộp hồ sơ cho tin đó.
    2.  **Xem chi tiết (`GET /api/applications/{applicationId}/candidate-profile`):**
        -   Nhà tuyển dụng chọn một ứng viên từ danh sách.
        -   API trả về hồ sơ chi tiết của ứng viên đó, bao gồm cả link để tải CV.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** `jobId` hoặc `applicationId` không hợp lệ/không thuộc sở hữu -> Lỗi `404 Not Found`.

---

### **F3.2.5: Thay đổi trạng thái đơn ứng tuyển**

-   **ID:** F3.2.5
-   **Tên chức năng:** Thay đổi trạng thái của một đơn ứng tuyển.
-   **Actor:** Employer.
-   **Mô tả:** Cho phép nhà tuyển dụng cập nhật trạng thái quá trình tuyển dụng của một ứng viên (ví dụ: "Đã xem", "Phù hợp", "Từ chối").
-   **Điều kiện tiên quyết:** Đã đăng nhập.
-   **Kết quả sau cùng:** Trạng thái của bản ghi `Application` được cập nhật. Ứng viên nhận được thông báo về sự thay đổi.
-   **Luồng xử lý chính:**
    1.  Nhà tuyển dụng chọn một hành động (ví dụ: "Từ chối") trên hồ sơ của ứng viên.
    2.  Hệ thống gửi yêu cầu `PATCH /api/applications/{applicationId}/status`.
    3.  API cập nhật trạng thái trong CSDL.
    4.  Hệ thống gửi thông báo (notification) tới ứng viên.
    5.  API trả về thông báo thành công.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** `applicationId` không hợp lệ -> Lỗi `404 Not Found`.
    -   **AE2:** Trạng thái mới không hợp lệ -> Lỗi `400 Bad Request`.

---

## Phần 4: Chức năng cho Quản trị viên (Admin)

*Ghi chú: Các chức năng trong phần này yêu cầu người dùng phải đăng nhập với vai trò "Admin".*

### **F4.1: Quản lý người dùng**

-   **ID:** F4.1.1, F4.1.2, F4.1.3
-   **Tên chức năng:** Xem, tìm kiếm, lọc và khóa/mở khóa người dùng.
-   **Actor:** Admin.
-   **Mô tả:** Cung cấp công cụ cho Admin để giám sát và quản lý tất cả người dùng trong hệ thống.
-   **Luồng xử lý chính:**
    1.  **Xem/Tìm kiếm (`GET /api/admin/users`):**
        -   Admin truy cập trang quản lý người dùng.
        -   API trả về danh sách tất cả người dùng (phân trang), hỗ trợ lọc theo `role`, `email`...
    2.  **Khóa/Mở khóa (`PATCH /api/admin/users/{userId}/lock`):**
        -   Admin chọn một người dùng và thực hiện hành động.
        -   API cập nhật trạng thái `isLocked` của người dùng trong CSDL.
        -   Nếu khóa, các token của người dùng đó sẽ bị vô hiệu hóa.
        -   API trả về thông tin người dùng đã được cập nhật.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** `userId` không tồn tại -> Lỗi `404 Not Found`.

---

### **F4.2: Quản lý tin tuyển dụng**

-   **ID:** F4.2.1, F4.2.2, F4.2.3
-   **Tên chức năng:** Xem, phê duyệt, từ chối và xóa tin tuyển dụng.
-   **Actor:** Admin.
-   **Mô tả:** Đảm bảo chất lượng các tin tuyển dụng trên hệ thống.
-   **Luồng xử lý chính:**
    1.  **Xem/Lọc (`GET /api/admin/jobs`):** Admin có thể xem tất cả các tin tuyển dụng, có thể lọc theo trạng thái (ví dụ `status=PENDING` để xem các tin chờ duyệt).
    2.  **Phê duyệt/Từ chối (`PATCH /api/admin/jobs/{jobId}/review`):**
        -   Admin xem xét một tin `PENDING`.
        -   Admin gửi yêu cầu với quyết định (`approved = true/false`).
        -   API cập nhật trạng thái tin tuyển dụng thành `OPEN` hoặc `REJECTED`.
        -   Hệ thống gửi thông báo cho nhà tuyển dụng về kết quả.
    3.  **Xóa (`DELETE /api/admin/jobs/{jobId}`):** Admin xóa các tin vi phạm.
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** `jobId` không tồn tại -> Lỗi `404 Not Found`.

---

### **F4.3.1: Xem Dashboard**

-   **ID:** F4.3.1
-   **Tên chức năng:** Xem bảng điều khiển (dashboard) với các thống kê tổng quan.
-   **Actor:** Admin.
-   **Mô tả:** Cung cấp cái nhìn tổng quan về tình hình hoạt động của hệ thống.
-   **Luồng xử lý chính:**
    1.  Admin truy cập trang Dashboard.
    2.  Hệ thống gửi yêu cầu đến `GET /api/admin/dashboard/stats`.
    3.  API thực hiện các truy vấn tổng hợp để lấy dữ liệu:
        -   Số lượng người dùng mới (theo ngày/tuần/tháng).
        -   Số lượng tin đăng mới.
        -   Số lượng lượt ứng tuyển.
        -   Doanh thu từ các gói dịch vụ.
    4.  API trả về một đối tượng JSON chứa các số liệu thống kê.

---

### **F4.3.2: Quản lý gói dịch vụ VIP**

-   **ID:** F4.3.2
-   **Tên chức năng:** Quản lý (thêm, sửa, xóa) các gói dịch vụ VIP.
-   **Actor:** Admin.
-   **Mô tả:** Cho phép Admin định cấu hình các gói dịch vụ mà nhà tuyển dụng có thể mua.
-   **Luồng xử lý chính:**
    1.  **Xem danh sách (`GET /api/vip-packages`):** Lấy danh sách các gói VIP hiện có.
    2.  **Tạo mới (`POST /api/admin/vip-packages`):** Admin điền thông tin (tên gói, giá, thời hạn, quyền lợi) và tạo gói mới.
    3.  **Cập nhật (`PUT /api/admin/vip-packages/{packageId}`):** Chỉnh sửa thông tin của một gói.
    4.  **Xóa (`DELETE /api/admin/vip-packages/{packageId}`):** Xóa một gói (chỉ nên cho phép nếu chưa có ai đăng ký).
-   **Luồng xử lý phụ và lỗi:**
    -   **AE1:** Dữ liệu không hợp lệ -> Lỗi `400 Bad Request`.
    -   **AE2:** `packageId` không tồn tại -> Lỗi `404 Not Found`.
