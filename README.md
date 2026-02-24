# EcomStore

**Hệ thống thương mại điện tử B2C** — Full-stack web application với phân quyền Admin / Staff / Shipper / User, tìm kiếm sản phẩm (Elasticsearch), khuyến mãi theo biến thể, thanh toán PayOS, và chat hỗ trợ.

---

## Tổng quan

EcomStore là dự án e-commerce hoàn chỉnh gồm **Backend Spring Boot** (REST API) và **Frontend React + TypeScript**. Hệ thống hỗ trợ nhiều vai trò người dùng, tìm kiếm full-text bằng Elasticsearch, áp dụng khuyến mãi theo từng biến thể sản phẩm, tích hợp thanh toán trực tuyến (PayOS), quản lý đơn hàng & giao hàng, và module chat giữa khách hàng và nhân viên.

---

## Công nghệ sử dụng

### Backend
| Công nghệ | Mục đích |
|-----------|----------|
| **Java 21** | Ngôn ngữ chính |
| **Spring Boot 3.5** | Framework backend |
| **Spring Data JPA** | ORM, truy vấn database |
| **Spring Security + JWT** | Xác thực, phân quyền |
| **MySQL / PostgreSQL** | Cơ sở dữ liệu chính |
| **Elasticsearch** | Tìm kiếm full-text sản phẩm |
| **Redis** | Cache |
| **RabbitMQ** | Message queue (đồng bộ index Elasticsearch) |
| **MapStruct** | Map DTO ↔ Entity |
| **Spring AI (Qdrant, Google GenAI)** | Vector store & LLM (embedding, chat) |
| **PayOS (vn.payos)** | Cổng thanh toán trực tuyến |
| **MinIO** | Lưu trữ file / ảnh |
| **Spring Mail** | Gửi email (OTP, thông báo) |
| **WebSocket (STOMP)** | Chat real-time |
| **Springdoc OpenAPI** | Tài liệu API (Swagger) |
| **Apache POI** | Import/Export Excel (dashboard, báo cáo) |
| **Micrometer + Prometheus** | Metrics, monitoring |

### Frontend
| Công nghệ | Mục đích |
|-----------|----------|
| **React 19** | UI library |
| **TypeScript** | Type-safe JavaScript |
| **Vite 7** | Build tool, dev server |
| **React Router 7** | Điều hướng SPA |
| **Tailwind CSS 4** | Styling |
| **Radix UI** | Component primitives (Dialog, Dropdown, Tabs, …) |
| **TanStack React Query** | Quản lý server state, cache API |
| **React Hook Form + Zod** | Form & validation |
| **Axios** | HTTP client |
| **Lucide React** | Icons |
| **Recharts** | Biểu đồ (dashboard) |
| **STOMP / SockJS** | WebSocket client (chat) |
| **Sonner** | Toast thông báo |

---

## Tính năng chính

### Khách hàng (User)
- Đăng ký / Đăng nhập (JWT, OAuth2 Google)
- Duyệt sản phẩm theo danh mục, thương hiệu; tìm kiếm full-text (Elasticsearch)
- Xem chi tiết sản phẩm, biến thể, giá sau khuyến mãi
- Giỏ hàng, wishlist
- Checkout, chọn địa chỉ; thanh toán PayOS hoặc COD
- Lịch sử đơn hàng, chi tiết đơn, trạng thái thanh toán
- Voucher / mã giảm giá; hạng thành viên (Smember)
- Tin tức / bài viết; chính sách bảo hành, đổi trả
- Đánh giá & câu hỏi sản phẩm
- Chat hỗ trợ với nhân viên (WebSocket)
- Hồ sơ cá nhân, quản lý địa chỉ

### Nhân viên (Staff)
- Bán hàng tại quầy: tìm sản phẩm (search), tạo đơn, xử lý thanh toán
- Gán đơn cho shipper; xem trạng thái giao hàng
- Dashboard công việc

### Shipper
- Danh sách đơn được giao; cập nhật trạng thái giao hàng
- Dashboard giao hàng

### Admin
- **Sản phẩm:** CRUD sản phẩm, biến thể, ảnh; import/export Excel
- **Danh mục, thương hiệu, nhà cung cấp, tiêu chí lọc**
- **Khuyến mãi:** Tạo/sửa khuyến mãi theo sản phẩm/biến thể; analytics khuyến mãi
- **Voucher:** CRUD voucher; thống kê sử dụng
- **Đơn hàng & phiếu nhập:** Xem, duyệt, chi tiết đơn; quản lý phiếu nhập kho
- **Khách hàng, giỏ hàng**
- **Tin tức:** Bài viết, danh mục tin
- **Banner, cài đặt**
- **Dashboard:** Thống kê doanh thu, đơn hàng; export Excel
- **Chat:** Quản lý hội thoại với khách
- **Câu hỏi sản phẩm, phản hồi đánh giá**
- **Nhân sự:** Quản lý staff

---

## Kiến trúc & kỹ thuật nổi bật

- **REST API** chuẩn, phân tách Controller → Service → Repository; DTO request/response rõ ràng.
- **Tìm kiếm sản phẩm:**  
  - Elasticsearch index sản phẩm; đồng bộ từ MySQL qua **RabbitMQ** (event khi tạo/cập nhật sản phẩm).  
  - API search trả về **ProductSearchResponse** (best variant + giá đã áp khuyến mãi), dùng chung cho trang chủ, tìm kiếm, danh mục, bán tại quầy.
- **Khuyến mãi:**  
  - **PromotionResolver** áp dụng khuyến mãi theo biến thể; tính **displayPrice**, **discountPercent**; batch query promotion để tối ưu.
- **Phân quyền:** JWT + Spring Security; route frontend bảo vệ theo vai trò (Admin / Staff / Shipper / User).
- **Real-time:** WebSocket (STOMP) cho chat; có thể mở rộng cho thông báo đơn hàng.
- **Observability:** Actuator + Prometheus metrics.

---

## Cấu trúc thư mục

```
EcomStore/
├── Back-End/                    # Spring Boot
│   └── src/main/java/iuh/fit/ecommerce/
│       ├── configurations/      # Security, JPA, Elasticsearch, RabbitMQ, Redis, PayOS, ...
│       ├── controllers/        # REST API
│       ├── services/ & impl/   # Business logic
│       ├── repositories/       # JPA + Elasticsearch
│       ├── entities/           # JPA entities
│       ├── dtos/               # Request/Response
│       ├── messaging/rabbitmq/ # Producer/Consumer
│       └── ...
├── Front-End/                  # React + Vite
│   └── src/
│       ├── components/         # UI components (user, admin, staff, common)
│       ├── pages/              # user, admin, staff, shipper, auth, error
│       ├── services/           # API client (axios)
│       ├── hooks/              # useQuery, useMutation, useWishlist, ...
│       ├── context/             # UserContext, ImportContext
│       ├── layouts/             # AdminLayout, UserLayout, ...
│       └── routes/             # useRouteElements, ProtectedRoute
└── README.md
```

---

## Yêu cầu hệ thống

- **JDK 21+** (Backend)
- **Node.js 18+** (Frontend)
- **MySQL 8** hoặc **PostgreSQL**
- **Elasticsearch 8.x**
- **Redis**
- **RabbitMQ**
- (Tùy chọn) **MinIO** cho upload ảnh; cấu hình **PayOS** cho thanh toán

---

## Cách chạy dự án

### Backend
1. Cấu hình `application.properties` / `application-*.yml`: database, Elasticsearch, Redis, RabbitMQ, PayOS, MinIO, JWT secret, …
2. Chạy Elasticsearch, Redis, RabbitMQ (Docker hoặc cài local).
3. Trong thư mục `Back-End`:  
   `./mvnw spring-boot:run` (hoặc chạy từ IDE).

### Frontend
1. Trong thư mục `Front-End`:  
   `npm install` → `npm run dev`
2. Cấu hình base URL API (biến môi trường / file config) trỏ tới backend.

### API docs
- Swagger UI: `http://localhost:8080/swagger-ui.html` (tùy cấu hình `api.prefix` và port).

---

## Tác giả & mục đích

Dự án được phát triển với mục đích **học tập và portfolio** — áp dụng kiến trúc phần mềm, Spring Boot, React, tìm kiếm, khuyến mãi, thanh toán và real-time chat trong bối cảnh thương mại điện tử thực tế.

