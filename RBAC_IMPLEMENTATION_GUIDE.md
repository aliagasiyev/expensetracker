# 🔐 RBAC Implementation Guide - Expense Tracker

## 📋 Overview

Bu sənəd Expense Tracker mikroservis arxitekturasında Role-Based Access Control (RBAC) implementasiyasını təsvir edir.

## 🎯 Roles və Permissions

### 👤 USER Role
- Yalnız öz məlumatları ilə işləyə bilər
- Öz xərclərini yarada, oxuya, yeniləyə və silə bilər
- Öz analitik məlumatlarını görə bilər
- Kateqoriyaları oxuya bilər (yarada bilməz)

### 👑 ADMIN Role
- Bütün sistem məlumatlarına çıxış
- İstifadəçi idarəetməsi
- Kateqoriya idarəetməsi
- Sistem analitikası
- Bütün xərcləri görə və idarə edə bilər

## 🏗️ Mikroservis Security Konfiqurasiyaları

### 1. 🔐 MS-AUTH Service

#### Security Features:
- JWT-based authentication
- Method-level security
- Custom security annotations
- Exception handling

#### Endpoints:
```
✅ Public: /auth/register, /auth/login
👤 User: /v1/users/me, /v1/users/profile, /v1/users/change-password
👑 Admin: /v1/admin/**, /v1/users/all, /v1/users/{id}
```

#### Key Implementation:
```java
@PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
```

### 2. 💸 MS-EXPENSE Service

#### Security Features:
- Ownership validation
- Category management restrictions
- User-specific data filtering

#### Endpoints:
```
👤 User: /api/expenses (CRUD own expenses)
👤 User: /api/categories (READ only)
👑 Admin: /api/expenses/all, /api/categories (CRUD)
```

#### Key Implementation:
```java
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public ResponseEntity<List<ExpenseResponse>> getUserExpenses() {
    return ResponseEntity.ok(expenseService.getUserExpenses());
}
```

### 3. 📈 MS-ANALYTICS Service

#### Security Features:
- User-specific analytics
- Admin system overview
- Ownership validation for reports

#### Endpoints:
```
👤 User: /analytics/summary/{userId}, /analytics/monthly-summary
👑 Admin: /analytics/system-overview, /analytics/all-users-summary
```

#### Key Implementation:
```java
@PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
```

### 4. 🌐 MS-API-GATEWAY Service

#### Security Features:
- Route-based security
- WebFlux security configuration
- CORS configuration

#### Route Security:
```yaml
Public: /auth/**, /actuator/health
Admin: /admin/**, /users/all, /expenses/all
User: /users/**, /expenses/**, /analytics/**
```

## 🛡️ Security Layers

### 1. HTTP Level Security
```java
.requestMatchers("/v1/admin/**").hasRole("ADMIN")
.requestMatchers("/v1/users/**").hasAnyRole("USER", "ADMIN")
```

### 2. Method Level Security
```java
@PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
```

### 3. Service Level Validation
```java
public void validateOwnership(Long resourceId, Long userId, boolean isAdmin) {
    if (isAdmin) return;
    if (!resource.getUserId().equals(userId)) {
        throw new AccessDeniedException("Bu resursa çıxışınız yoxdur");
    }
}
```

## 🔧 Custom Security Annotations

### @AdminOnly
```java
@AdminOnly
public ResponseEntity<List<UserResponse>> getAllUsers() {
    // Only admins can access
}
```

### @OwnerOrAdmin
```java
@OwnerOrAdmin
public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
    // Admin can access any user, User can access only their own
}
```

## 🚨 Exception Handling

### Security Exceptions:
- `AccessDeniedException` → 403 Forbidden
- `AuthenticationException` → 401 Unauthorized
- `AuthenticationCredentialsNotFoundException` → 401 Unauthorized

### Error Response Format:
```json
{
    "message": "Bu əməliyyata icazəniz yoxdur",
    "code": "ACCESS_DENIED",
    "status": 403,
    "timestamp": "2024-01-15T10:30:00",
    "path": "/api/users/123"
}
```

## 🧪 Testing Security

### Unit Tests:
```java
@Test
@WithMockUser(roles = "USER")
void userCanAccessOwnData() {
    // Test implementation
}

@Test
@WithMockUser(roles = "ADMIN")
void adminCanAccessAllData() {
    // Test implementation
}
```

### Integration Tests:
```java
@Test
void userCannotAccessOthersData() {
    // Test with real HTTP requests
}
```

## 📊 Security Matrix

| Endpoint | USER | ADMIN | Notes |
|----------|------|-------|-------|
| `POST /auth/register` | ✅ | ✅ | Public |
| `GET /users/me` | ✅ | ✅ | Own profile |
| `GET /users/{id}` | ❌ | ✅ | Admin only |
| `GET /users/all` | ❌ | ✅ | Admin only |
| `POST /expenses` | ✅ | ✅ | Own expenses |
| `GET /expenses` | ✅* | ✅ | *Own expenses only |
| `GET /expenses/all` | ❌ | ✅ | Admin only |
| `POST /categories` | ❌ | ✅ | Admin only |
| `GET /categories` | ✅ | ✅ | Read only for users |
| `GET /analytics/summary/{userId}` | ✅* | ✅ | *Own data only |
| `GET /analytics/system-overview` | ❌ | ✅ | Admin only |

## 🔄 Implementation Checklist

### ✅ Completed:
- [x] SecurityConfig for all microservices
- [x] Method-level security annotations
- [x] Custom security annotations
- [x] Exception handling
- [x] CORS configuration
- [x] API Gateway security

### 🔄 Next Steps:
- [ ] JWT token validation between services
- [ ] Rate limiting
- [ ] Audit logging
- [ ] Security testing automation
- [ ] Performance monitoring

## 🚀 Best Practices

1. **Principle of Least Privilege**: Hər rol yalnız lazım olan minimum icazələrə malikdir
2. **Defense in Depth**: Çoxlu təhlükəsizlik qatları
3. **Fail Secure**: Şübhəli hallarda çıxışı rədd et
4. **Audit Everything**: Bütün təhlükəsizlik hadisələrini qeyd et
5. **Regular Reviews**: Təhlükəsizlik konfiqurasiyalarını müntəzəm yoxla
