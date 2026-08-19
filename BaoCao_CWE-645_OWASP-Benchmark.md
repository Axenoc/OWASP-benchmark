# BÁO CÁO LỖ HỔNG CWE-645 TRONG OWASP BENCHMARK

## Overly Restrictive Account Lockout Mechanism

> **Mục đích:** Mô tả test case CWE-645 được cố ý nhúng vào codebase OWASP Benchmark để con người, LLM và công cụ kiểm thử bảo mật có thể đọc, tái hiện và đánh giá.
>
> **Phạm vi dữ liệu:** Báo cáo này mô tả chính xác mã nguồn cục bộ của `BenchmarkTestCWE645`; đây không phải là một CVE hoặc advisory mới của sản phẩm thực tế.
>
> **Ngày lập báo cáo:** 2026-08-19
>
> **Trạng thái:** Lỗ hổng tồn tại có chủ đích (`vulnerable by design`).

---

## 0. TÓM TẮT NHANH CHO LLM

- **CWE:** CWE-645 — Overly Restrictive Account Lockout Mechanism.
- **Loại tác động:** Denial of Service ở tầng xác thực; ảnh hưởng tính sẵn sàng của tài khoản.
- **Endpoint:** `POST /benchmark/cwe-645/BenchmarkTestCWE645`.
- **Tham số đầu vào:** `BenchmarkTestCWE645` với định dạng `username,password`.
- **Tài khoản mẫu:** `victim@example.com`.
- **Mật khẩu hợp lệ trong benchmark:** `CorrectPassword123!`.
- **Ngưỡng khóa:** 3 lần xác thực sai.
- **Yêu cầu xác thực của kẻ tấn công:** Không.
- **Điều kiện khai thác:** Biết hoặc đoán được username của nạn nhân.
- **Nguyên nhân gốc:** Bộ đếm và trạng thái khóa được gắn toàn cục với username do request cung cấp; hệ thống không phân biệt IP, session, thiết bị hoặc nguồn gửi request.
- **Kết quả:** Sau 3 request sai, tài khoản bị khóa cứng. Mật khẩu đúng cũng không thể đăng nhập.
- **Thời gian khóa:** Không giới hạn trong vòng đời JVM; mã không có chức năng tự mở khóa.
- **Phạm vi trạng thái:** Dùng chung giữa mọi client và session trong cùng tiến trình ứng dụng.
- **Expected result của Benchmark:** `BenchmarkTestCWE645,cwe645,true,645`.
- **Đánh giá CVSS tham khảo:** 5.3 Medium — `CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:L`. Đây là ước lượng cho test case, không phải điểm CVSS chính thức.

---

## 1. THÔNG TIN TEST CASE

| Trường | Giá trị |
|---|---|
| Test case | `BenchmarkTestCWE645` |
| CWE ID | `645` |
| CWE name | `Overly Restrictive Account Lockout Mechanism` |
| Category trong expected results | `cwe645` |
| Vulnerable | `true` |
| Ngôn ngữ/backend | Java Servlet |
| Frontend | HTML form |
| HTTP method khai thác | `POST` |
| Servlet mapping | `/cwe-645/BenchmarkTestCWE645` |
| Authentication required | `false` |
| User interaction required | `false` |
| Security property bị ảnh hưởng | Availability |
| Kiểu tấn công | Account lockout DoS |

### 1.1. Tệp liên quan

| Tệp | Vai trò |
|---|---|
| `src/main/java/org/owasp/benchmark/testcode/BenchmarkTestCWE645.java` | Servlet chứa logic xác thực và khóa tài khoản có chủ đích |
| `src/main/webapp/cwe-645/BenchmarkTestCWE645.html` | Form gửi username và password tới servlet |
| `src/main/webapp/cwe645.html` | Trang index của test case CWE-645 |
| `expectedresults-1.2.csv` | Khai báo ground truth: test case là vulnerable và thuộc CWE-645 |
| `README.md` | Liên kết truy cập test case |

---

## 2. MÔ HÌNH LỖ HỔNG

Ứng dụng duy trì hai cấu trúc dữ liệu `static` dùng chung toàn tiến trình:

```java
private static final ConcurrentHashMap<String, AtomicInteger> FAILED_ATTEMPTS =
        new ConcurrentHashMap<>();
private static final Set<String> LOCKED_ACCOUNTS = ConcurrentHashMap.newKeySet();
```

Username lấy trực tiếp từ request trở thành khóa của hai cấu trúc trên. Khi bất kỳ client nào nhập sai mật khẩu cho một username, bộ đếm của chính username đó tăng lên. Nguồn request không được đưa vào quyết định khóa.

```text
Request chưa xác thực
        │
        ▼
username,password từ tham số BenchmarkTestCWE645
        │
        ▼
Kiểm tra LOCKED_ACCOUNTS theo username
        │
        ├── Đã khóa ──► HTTP 403, kể cả khi password đúng
        │
        └── Chưa khóa
                │
                ├── Đúng credentials ──► HTTP 200, xóa bộ đếm lỗi
                │
                └── Sai credentials ──► tăng FAILED_ATTEMPTS[username]
                                              │
                                              ├── < 3 ──► HTTP 401
                                              └── >= 3 ─► thêm username vào LOCKED_ACCOUNTS,
                                                          HTTP 403
```

### 2.1. Điểm phát sinh CWE-645

Các vị trí quan trọng trong servlet:

- Dòng 24: đặt ngưỡng khóa thấp và cố định là 3.
- Dòng 28–30: lưu bộ đếm và trạng thái khóa toàn cục theo username.
- Dòng 45: nhận credentials từ request không yêu cầu xác thực.
- Dòng 56–62: từ chối ngay nếu username đã nằm trong tập khóa.
- Dòng 71–76: tăng bộ đếm theo username và khóa khi đạt ngưỡng.
- Không có logic rate limit theo IP/session/device.
- Không có thời gian hết hạn của trạng thái khóa.
- Không có luồng tự mở khóa hoặc xác minh chủ tài khoản.

### 2.2. Vì sao đây là CWE-645

Cơ chế khóa được thiết kế để ngăn brute force, nhưng quyết định khóa lại dựa hoàn toàn vào định danh của nạn nhân. Một bên thứ ba có thể chủ động tạo đủ số lần thất bại cho username đó. Vì vậy, biện pháp phòng vệ bị biến thành công cụ DoS nhắm vào tài khoản hợp lệ.

### 2.3. Những lỗi không thuộc test case

Mã chủ động tránh một số lỗi phụ để giữ ground truth tập trung vào CWE-645:

- Username được encode bằng OWASP ESAPI trước khi ghi vào HTML, giảm nguy cơ reflected XSS.
- Input thiếu hoặc sai định dạng trả về HTTP 400 thay vì gây `NullPointerException` hoặc `ArrayIndexOutOfBoundsException`.
- Bộ đếm dùng `ConcurrentHashMap` và `AtomicInteger` để tránh mất cập nhật khi có request đồng thời.
- Password không được phản chiếu vào response.

---

## 3. ĐIỀU KIỆN VÀ GIẢ ĐỊNH KHAI THÁC

### 3.1. Điều kiện bắt buộc

1. Ứng dụng OWASP Benchmark đang chạy.
2. Endpoint CWE-645 có thể được truy cập qua mạng.
3. Kẻ tấn công biết hoặc đoán được username `victim@example.com`.

### 3.2. Không yêu cầu

- Không cần tài khoản riêng.
- Không cần đăng nhập trước.
- Không cần cookie hoặc session hợp lệ.
- Không cần biết mật khẩu của nạn nhân.
- Không cần nạn nhân tương tác.

### 3.3. Giới hạn của mô hình benchmark

- Chỉ `victim@example.com` với mật khẩu `CorrectPassword123!` được xem là credentials hợp lệ.
- Trạng thái nằm trong bộ nhớ, không được lưu vào database.
- Restart/redeploy ứng dụng hoặc JVM sẽ xóa trạng thái khóa.
- Trong khi JVM còn chạy, mã không cung cấp endpoint mở khóa.
- Test case mô phỏng account lockout DoS; không mô phỏng một hệ thống danh tính hoàn chỉnh.

---

## 4. HƯỚNG DẪN TÁI HIỆN

### 4.1. Qua giao diện web

1. Truy cập `https://localhost:8443/benchmark/cwe645.html`.
2. Mở `BenchmarkTestCWE645`.
3. Giữ giá trị `victim@example.com,WrongPassword`.
4. Nhấn **Login** ba lần.
5. Đổi giá trị thành `victim@example.com,CorrectPassword123!`.
6. Nhấn **Login** lần nữa.
7. Quan sát tài khoản vẫn trả về `ACCOUNT LOCKED` với HTTP 403.

### 4.2. Qua HTTP request

PowerShell/Windows sử dụng `curl.exe` để tránh alias `Invoke-WebRequest`:

```powershell
curl.exe -k -i -X POST "https://localhost:8443/benchmark/cwe-645/BenchmarkTestCWE645" `
  -H "Content-Type: application/x-www-form-urlencoded" `
  --data-urlencode "BenchmarkTestCWE645=victim@example.com,WrongPassword"
```

Gửi request trên ba lần, sau đó thử mật khẩu đúng:

```powershell
curl.exe -k -i -X POST "https://localhost:8443/benchmark/cwe-645/BenchmarkTestCWE645" `
  -H "Content-Type: application/x-www-form-urlencoded" `
  --data-urlencode "BenchmarkTestCWE645=victim@example.com,CorrectPassword123!"
```

### 4.3. Kết quả mong đợi

| Request | Credentials | HTTP status | Trạng thái nội bộ | Nội dung chính |
|---:|---|---:|---|---|
| 1 | Sai | 401 | `FAILED_ATTEMPTS=1` | `Login Failed`; còn 2 lần |
| 2 | Sai | 401 | `FAILED_ATTEMPTS=2` | `Login Failed`; còn 1 lần |
| 3 | Sai | 403 | tài khoản được thêm vào `LOCKED_ACCOUNTS` | `ACCOUNT LOCKED` |
| 4 | Đúng | 403 | vẫn nằm trong `LOCKED_ACCOUNTS` | `ACCOUNT LOCKED` |

Kết quả của request thứ tư là bằng chứng khai thác quan trọng: chủ tài khoản không thể đăng nhập bằng credentials đúng sau khi một bên khác tạo đủ số lần thất bại.

### 4.4. Cách reset môi trường thử nghiệm

Restart hoặc redeploy ứng dụng để tạo JVM/application context mới. Không có chức năng reset qua HTTP trong test case vì trạng thái khóa vĩnh viễn là một phần của mô hình dễ bị tổn thương.

---

## 5. PHÂN TÍCH NGUYÊN NHÂN GỐC

### 5.1. Khóa theo mục tiêu thay vì theo nguồn tấn công

Toàn bộ request nhắm tới cùng một username chia sẻ một bộ đếm, bất kể chúng đến từ IP, session hay thiết bị nào. Kẻ tấn công kiểm soát giá trị username và có thể tăng bộ đếm của nạn nhân từ xa.

### 5.2. Hard lockout không có thời hạn

Khi username đã được thêm vào `LOCKED_ACCOUNTS`, kiểm tra khóa diễn ra trước kiểm tra mật khẩu. Không có timestamp, timeout hoặc cơ chế phục hồi. Mật khẩu đúng không thể giải phóng tài khoản.

### 5.3. Không có lớp giảm tốc trước xác thực

Endpoint không có rate limit, exponential backoff, CAPTCHA hoặc quota theo nguồn. Do đó attacker có thể đạt ngưỡng ba request với chi phí rất thấp.

### 5.4. Trạng thái chia sẻ toàn cục

Các collection là `static`, nên việc khóa do một client tạo ra ảnh hưởng mọi client khác trong cùng JVM. Đây là điều làm cho tấn công trở thành DoS thực tế thay vì chỉ là lỗi cục bộ của session tấn công.

---

## 6. TÁC ĐỘNG BẢO MẬT

| Thuộc tính | Mức ảnh hưởng | Giải thích |
|---|---|---|
| Confidentiality | Không | Test case không làm lộ dữ liệu hoặc mật khẩu |
| Integrity | Không trực tiếp | Không sửa dữ liệu nghiệp vụ của nạn nhân |
| Availability | Thấp đến trung bình | Tài khoản hợp lệ bị từ chối đăng nhập cho tới khi ứng dụng restart |
| Scope | Unchanged | Tác động nằm trong ứng dụng Benchmark hiện tại |

Các hệ quả có thể có trong hệ thống thực tế nếu mẫu thiết kế này được sử dụng:

- Khóa hàng loạt tài khoản người dùng nếu username/email dễ đoán.
- Khóa tài khoản quản trị hoặc tài khoản mới tạo trước lần đăng nhập đầu tiên.
- Gây gián đoạn vận hành, tăng tải hỗ trợ và yêu cầu mở khóa thủ công.
- Nếu kết nối với LDAP/AD, các lần thử được chuyển tiếp có thể làm tài khoản bị khóa trên nhiều dịch vụ dùng chung hệ thống danh tính.

---

## 7. ĐÁNH GIÁ MỨC ĐỘ

### 7.1. CVSS tham khảo

```text
CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:L = 5.3 (Medium)
```

| Metric | Giá trị | Lý do |
|---|---|---|
| AV | Network | Endpoint được gọi từ xa qua HTTP |
| AC | Low | Chỉ cần ba request sai |
| PR | None | Không cần xác thực |
| UI | None | Nạn nhân không cần thao tác |
| S | Unchanged | Ảnh hưởng giới hạn trong ứng dụng |
| C | None | Không làm lộ thông tin |
| I | None | Không sửa dữ liệu nghiệp vụ |
| A | Low | Làm mất khả năng đăng nhập của một tài khoản |

Điểm này chỉ phục vụ phân tích test case. Test case OWASP Benchmark cục bộ không có CVE/GHSA riêng và không nên được trình bày như một advisory sản phẩm.

---

## 8. DẤU HIỆU NHẬN DIỆN

### 8.1. Static analysis

Công cụ hoặc LLM nên tìm tổ hợp dấu hiệu sau thay vì chỉ tìm một dòng riêng lẻ:

1. Username/email lấy từ request.
2. Bộ đếm thất bại được index chỉ bằng username/email.
3. Trạng thái khóa dùng chung giữa các request hoặc session.
4. Ngưỡng thất bại dẫn tới hard lockout.
5. Thiếu dimension theo IP/session/device hoặc thiếu rate limit trước xác thực.
6. Thiếu TTL, backoff hoặc luồng khôi phục an toàn.
7. Kiểm tra lockout xảy ra trước xác minh credentials đúng.

### 8.2. Dynamic analysis

Một scanner có thể:

1. Chọn một username kiểm thử có credentials đúng đã biết.
2. Xác nhận credentials đúng trả HTTP 200 trên trạng thái sạch.
3. Gửi ba lần password sai từ một client/session khác.
4. Thử lại credentials đúng.
5. Báo CWE-645 nếu request đúng bị HTTP 403 do trạng thái do client khác gây ra.

Để tránh gây DoS ngoài ý muốn, dynamic scanner chỉ nên thực hiện kiểm thử này trên tài khoản và môi trường được cấp phép.

---

## 9. KHUYẾN NGHỊ KHẮC PHỤC

Đối với ứng dụng sản xuất, không nên chỉ tăng ngưỡng khóa. Cần thay đổi mô hình kiểm soát:

1. Áp dụng rate limit kết hợp theo IP, session/device và username.
2. Dùng exponential backoff hoặc trì hoãn tăng dần thay cho hard lockout vô thời hạn.
3. Nếu phải khóa, dùng soft lockout có TTL ngắn và lưu timestamp hết hạn.
4. Bổ sung CAPTCHA hoặc bước xác minh bổ sung khi hành vi bất thường.
5. Cho phép chủ tài khoản tự mở khóa qua kênh đã xác minh như MFA hoặc email.
6. Cảnh báo người dùng và đội vận hành khi có chuỗi đăng nhập thất bại bất thường.
7. Không tiết lộ cho client việc username có tồn tại hay không.
8. Với LDAP/AD/SSO, throttle ở tầng ứng dụng trước khi chuyển tiếp request tới identity provider.
9. Theo dõi việc kẻ tấn công luân chuyển IP; rate limit theo IP đơn lẻ không đủ.

Một thiết kế an toàn hơn có thể dùng khóa tổng hợp và thời gian hết hạn:

```text
sourceKey  = hash(sourceIp + session/device signal)
accountKey = normalizedUsername

if sourceRateExceeded(sourceKey):
    requireAdditionalVerification()

if accountFailuresHigh(accountKey):
    applyShortBackoffWithExpiry()
    alertAccountOwner()

never create an indefinite account lock solely from unauthenticated failures
```

---

## 10. TIÊU CHÍ XÁC NHẬN BẢN VÁ

Bản vá được xem là đạt nếu thỏa tất cả điều kiện sau:

- Ba lần nhập sai từ attacker không thể khóa vĩnh viễn tài khoản nạn nhân.
- Credentials đúng của nạn nhân vẫn có đường phục hồi an toàn.
- Hạn chế request có dimension theo nguồn, không chỉ theo username mục tiêu.
- Trạng thái tạm khóa có TTL hoặc quy trình mở khóa xác minh được.
- Request đồng thời không làm vượt hoặc sai lệch chính sách.
- Response không tiết lộ username có tồn tại.
- Với backend LDAP/AD, số request được chuyển tiếp bị giới hạn trước khi chạm identity provider.

---

## 11. KHỐI DỮ LIỆU MÁY ĐỌC

```json
{
  "schema_version": "1.0",
  "report_type": "intentionally_vulnerable_benchmark_testcase",
  "generated_date": "2026-08-19",
  "project": "OWASP Benchmark for Java 1.2",
  "test_case": {
    "id": "BenchmarkTestCWE645",
    "cwe_id": 645,
    "cwe_name": "Overly Restrictive Account Lockout Mechanism",
    "category": "cwe645",
    "expected_vulnerable": true,
    "is_real_product_advisory": false,
    "cve": null,
    "ghsa": null
  },
  "endpoint": {
    "method": "POST",
    "path": "/benchmark/cwe-645/BenchmarkTestCWE645",
    "content_type": "application/x-www-form-urlencoded",
    "parameter": "BenchmarkTestCWE645",
    "parameter_format": "username,password",
    "authentication_required": false
  },
  "fixture": {
    "valid_username": "victim@example.com",
    "valid_password": "CorrectPassword123!",
    "example_wrong_password": "WrongPassword",
    "maximum_failed_attempts": 3
  },
  "vulnerability": {
    "root_cause": "Global account lockout is keyed only by attacker-supplied username and does not distinguish request source.",
    "attack_type": "account_lockout_denial_of_service",
    "required_privileges": "none",
    "user_interaction": "none",
    "attacker_precondition": "Know or guess a valid username",
    "affected_property": "availability",
    "lock_duration": "until JVM restart or application redeploy",
    "state_scope": "all clients and sessions in the same JVM",
    "automatic_unlock": false
  },
  "exploit_sequence": [
    {
      "step": 1,
      "credentials": "victim@example.com,WrongPassword",
      "expected_http_status": 401,
      "failed_attempts": 1,
      "locked": false
    },
    {
      "step": 2,
      "credentials": "victim@example.com,WrongPassword",
      "expected_http_status": 401,
      "failed_attempts": 2,
      "locked": false
    },
    {
      "step": 3,
      "credentials": "victim@example.com,WrongPassword",
      "expected_http_status": 403,
      "failed_attempts": 3,
      "locked": true
    },
    {
      "step": 4,
      "credentials": "victim@example.com,CorrectPassword123!",
      "expected_http_status": 403,
      "locked": true,
      "proof": "Correct credentials are rejected after attacker-induced failures"
    }
  ],
  "cvss_estimate": {
    "version": "3.1",
    "score": 5.3,
    "severity": "medium",
    "vector": "CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:L",
    "official": false
  },
  "code_locations": [
    {
      "file": "src/main/java/org/owasp/benchmark/testcode/BenchmarkTestCWE645.java",
      "relevant_lines": [24, 28, 29, 30, 45, 56, 57, 58, 59, 71, 72, 73, 74, 75, 76]
    },
    {
      "file": "src/main/webapp/cwe-645/BenchmarkTestCWE645.html",
      "relevant_lines": [12, 14, 15, 18, 20]
    },
    {
      "file": "expectedresults-1.2.csv",
      "relevant_lines": [2750]
    }
  ],
  "secondary_weaknesses_intentionally_avoided": [
    "reflected_xss",
    "null_pointer_exception_on_missing_input",
    "array_index_out_of_bounds_on_malformed_input",
    "lost_updates_in_concurrent_counter"
  ],
  "recommended_controls": [
    "rate_limit_by_source_and_account",
    "exponential_backoff",
    "temporary_lock_with_expiry",
    "additional_verification",
    "verified_self_service_unlock",
    "security_alerting",
    "pre_throttle_before_ldap_or_identity_provider"
  ],
  "verification": {
    "maven_compile": "passed",
    "maven_test": "passed",
    "automated_test_sources_present": false
  }
}
```

---

## 12. KẾT LUẬN

`BenchmarkTestCWE645` là một test case dương tính có chủ đích cho CWE-645. Điểm cốt lõi không phải là việc ứng dụng có bộ đếm đăng nhập sai, mà là việc một request chưa xác thực có thể tăng trạng thái khóa toàn cục của một username do attacker lựa chọn. Sau ba lần sai, chủ tài khoản bị từ chối ngay cả khi cung cấp mật khẩu đúng, tạo ra account-level Denial of Service.

LLM hoặc công cụ phân tích nên phân loại test case này là **true positive CWE-645**, tập trung vào sự kết hợp giữa state toàn cục theo username, hard lockout, thiếu phân biệt nguồn request và thiếu cơ chế hết hạn/khôi phục.
