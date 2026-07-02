
# 1. 概述

## 1.1 接口说明

本规范定义的所有接口均通过 HTTPS 传输，数据格式为 JSON。所有接口的 HTTP 方法统一为 **POST**。

- **协议**：HTTPS（TLS 1.2 及以上）
- **Content-Type**：`application/json`
- **编码**：UTF-8
- **安全要求**：所有业务请求和响应报文必须按照本文档定义的安全规范进行加密、签名和验签。

---

# 2. SDK 快速开始

> 以下示例基于 Java SDK（`qfin-sdk-java`），封装了本文档定义的加密、签名流程，调用方无需关心底层密码学细节。

## 2.1 添加依赖

```xml
<dependency>
    <groupId>com.qfin.overseas.finance.gws</groupId>
    <artifactId>qfin-sdk-java</artifactId>
    <version>1.5.0</version>
</dependency>
```

## 2.2 发送加密请求

```java
// 1. 生成 RSA-2048 密钥对
CredentialConfig.KeyPair keyPair = QfinClient.generateKeyPair();
// 公钥发给 QFIN 注册，私钥自己保密

// 2. 收到 QFIN 公钥和 AppId 后，构造凭证配置
CredentialConfig config = new CredentialConfig(
    "your-app-id",                              // QFIN 分配的 AppId
    new CredentialConfig.KeyPair(
        yourPublicKey,                          // 自己的 RSA 公钥
        yourPrivateKey                          // 自己的 RSA 私钥
    ),
    new CredentialConfig.PeerKey(
        qfinPublicKey                           // QFIN 的 RSA 公钥
    )
);

// 3. 发送请求
QfinClient client = new QfinClient(config);     // 建议单例

String response = client.send(
    "https://api.qfin.com/co-lending/v1/xxx",   // 接口地址
    "{\"orderId\":\"ORD-001\"}"                  // 业务 JSON
);
// response 为解密后的业务响应明文
```

> SDK 内部自动完成：生成 AES 密钥 → 加密 body → 加密 AES 密钥 → 签名 → 发送 → 验签 → 解密响应。

---

# 3. 安全机制

## 3.1 双重保护

为确保数据安全，采用**加密 + 数字签名**双重保护方案：

| 保护类型 | 作用 |
|---|---|
| **加密** | 保护数据机密性，防止数据泄露 |
| **数字签名** | 确保数据完整性，防止篡改，并验证消息来源 |

## 3.2 分层安全架构

```
┌──────────────────────────────────────────────────────────────┐
│ 网络层   ：TLS 1.2+（传输通道的机密性与完整性）                │
├──────────────────────────────────────────────────────────────┤
│ 协议层   ：报文级安全（加密、签名、验签、防重放）              │
├──────────────────────────────────────────────────────────────┤
│ 业务层   ：业务语义级安全（字段校验、授权、幂等）             │
└──────────────────────────────────────────────────────────────┘
```

---

# 4. 密码学算法规范

## 4.1 算法套件

采用以下算法组合：**RSA-2048 + AES-256-GCM + SHA256withRSA**

| 类型 | 算法 | 说明 |
|---|---|---|
| 密钥加密 | RSA-2048 | 用于加密 AES 会话密钥。填充方式固定为 `RSA/ECB/OAEPWithSHA-256AndMGF1Padding`：OAEP 哈希 = SHA-256，MGF1 哈希 = SHA-256，label = 空。两个哈希都必须是 SHA-256 —— 使用常见的 MGF1-SHA1 默认值将导致解密失败 |
| 数据加密 | AES-256-GCM | AEAD 模式 —— 同时保证机密性和完整性，防篡改。认证标签长度为 128 位（16 字节）。加密后的 `data` 值为 `Base64( ciphertext \|\| authTag )`：将 16 字节的标签追加到密文后，整体进行 Base64 编码；接收方拆分最后 16 字节作为标签。IV 为 12 字节，通过 `X-IV` 传递 |
| 签名 | SHA256withRSA | 消息签名，防止篡改 |

---

# 5. 数据格式规范

## 5.1 加密范围

所有业务数据在请求体（Body）中作为整体进行加密。不做字段级区分 —— 整个报文作为一个整体进行加密处理。

协议元数据（身份标识、防重放参数、签名和加密参数）通过请求头传递。请求体仅包含加密后的业务密文。

## 5.2 加密请求格式

**请求头**

| Header | 类型 | 必填 | 说明 |
|---|---|---|---|
| X-App-Id | String | 是 | 应用唯一标识 |
| X-Timestamp | String | 是 | Unix 毫秒时间戳，如 `1712534400000`。用于防重放。接收方必须拒绝 `X-Timestamp` 与接收方本地时钟偏差超过 **±5 分钟** 的请求。双方必须通过 NTP 保持服务器时钟同步，以确保该时间窗口的一致执行 |
| X-Request-Id | String | 是 | 请求唯一标识（UUID）。用于请求追踪和防重放 |
| X-IV | String | 是 | 初始向量，Base64 编码，GCM 模式为 12 字节 |
| X-Encrypted-Key | String | 是 | 使用对方 RSA 公钥加密后的 AES 会话密钥，Base64 编码 |
| X-Sign | String | 是 | 数字签名值，Base64 编码 |

**请求体**（`Content-Type: application/json`）

```json
{
  "data": "Base64(AES-256-GCM 加密后的业务数据)"
}
```

> **说明**：`data` 值为 `Base64( ciphertext || authTag )`，其中 `authTag` 是 16 字节（128 位）的 GCM 认证标签，追加在密文之后。IV 通过 `X-IV` 单独传递（12 字节，Base64 编码）。加密算法固定为 AES-256-GCM，签名算法固定为 SHA256withRSA，每次请求无需重复传递。

## 5.3 签名规范

签名覆盖所有请求头字段和完整请求体，防止任意单个字段被篡改。**请求和响应使用相同的签名公式** —— 区别仅在于由谁签名以及对哪个 body 取哈希。这样可以避免请求侧和响应侧签名时的歧义。

**统一签名公式：**

```
签名原文 = X-App-Id|X-Timestamp|X-Request-Id|X-IV|X-Encrypted-Key|SHA256(body)
X-Sign  = Base64( SHA256withRSA(签名方 RSA 私钥, 签名原文) )
```

其中：

- `body` —— **网络上实际发送/接收的原始 HTTP 报文字节**。对于请求，即为请求体（`{"data":"..."}`）；对于响应，即为响应体。任何一方都不进行 JSON 重新序列化、重新格式化或空白字符规范化。
- `SHA256(body)` —— 对 `body` 计算 SHA-256 摘要，在拼接前表示为**小写十六进制字符串**。
- 字段使用 `|` 作为固定分隔符，按**上述确切顺序**拼接。空字段保留为空字符串（产生两个连续的 `|`），绝不省略，因此字段数始终固定为 6 个。
- **字符编码**：`签名原文` 在送入 `SHA256withRSA` 之前必须以 **UTF-8** 编码。无论使用何种实现语言，双方必须统一使用 UTF-8，否则对相同逻辑内容计算的签名将不匹配。
- **Base64 变体**：本规范中所有 Base64 值（`X-IV`、`X-Encrypted-Key`、`X-Sign` 和 `data` 字段）均使用**标准 Base64 编码**（RFC 4648 §4，含 `+` / `/` 和 `=` 填充）——**不使用** URL 安全变体。

**请求签名**（由调用方发送）：使用调用方的 RSA 私钥签名；服务端使用调用方的 RSA 公钥验签。此处 `body` = 请求体。

**响应签名**（由服务端发送）：使用服务端的 RSA 私钥签名；调用方使用服务端的 RSA 公钥验签。此处 `body` = 响应体。响应携带 `X-App-Id`（回传请求中的值），因此同一统一公式同样适用。

**示例（请求）：**

```
假设：
  X-App-Id        = PARTNER001
  X-Timestamp     = 1712534400000
  X-Request-Id    = 550e8400-e29b-41d4-a716-446655440000
  X-IV            = Base64(0x00112233445566778899aabbccddeeff)        // 12 字节
  X-Encrypted-Key = Base64(...)
  requestBody     = {"data":"<Base64 ciphertext>"}                    // 网络上的原始字节
  SHA256(requestBody) = 9a3e...c1f0                                   // 小写十六进制

签名原文 = PARTNER001|1712534400000|550e8400-e29b-41d4-a716-446655440000|<X-IV>|<X-Encrypted-Key>|9a3e...c1f0
X-Sign   = Base64( SHA256withRSA(调用方 RSA 私钥, 签名原文) )
```

> **说明**：请求体在纳入签名前先经过 SHA-256 哈希，以避免直接对大报文签名带来的性能开销，同时仍然保证请求体的完整性。

## 5.4 加密响应格式

**响应头**

| Header | 类型 | 必填 | 说明 |
|---|---|---|---|
| X-App-Id | String | 是 | 回传请求的 `X-App-Id`。包含此字段使得统一签名公式（§5.3）对响应的适用方式与请求完全一致 |
| X-Timestamp | String | 是 | Unix 毫秒时间戳 |
| X-Request-Id | String | 是 | 回传请求的 `X-Request-Id`，用于关联请求与响应 |
| X-IV | String | 是 | 初始向量，Base64 编码 |
| X-Encrypted-Key | String | 是 | 使用对方 RSA 公钥加密后的 AES 会话密钥，Base64 编码 |
| X-Sign | String | 是 | 数字签名值，Base64 编码，按 §5.3 对响应计算 |

**响应体**（`Content-Type: application/json`）

```json
{
  "flag": "S/F",
  "code": "string",
  "msg": "string",
  "data": "Base64(AES-256-GCM 加密后的业务数据)"
}
```

## 5.5 响应体字段说明

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| flag | String | 是 | 处理结果标识：`S` = 请求成功，`F` = 请求失败（被网关层拦截）|
| code | String | 否 | 错误码。仅 `flag=F` 时有值，用于区分不同的失败原因，错误码详见第 8 节 |
| msg | String | 否 | 错误码描述 |
| data | String | 否 | 加密后的业务数据，Base64 编码 |

> **设计说明**：`flag`、`code`、`msg` 三个字段为明文传输，这是有意设计。当 `flag=F` 时，表示请求在网关层即被拦截（如签名验证失败、解密失败、参数无效等），此时无需也无法对业务数据进行加密，直接以明文返回错误信息便于调用方快速定位问题。当 `flag=S` 时，业务数据通过 `data` 字段加密返回。

---

# 6. 加解密流程

## 6.1 请求方处理流程

```
1. 构建原始业务请求数据
        ↓
2. 生成随机 AES 会话密钥和 IV（12 字节）
        ↓
3. 使用 AES-256-GCM 加密请求体数据 → encryptedData
        ↓
4. 使用对方 RSA 公钥加密 AES 会话密钥 → encryptedKey
        ↓
5. 构造请求头：X-App-Id、X-Timestamp、X-Request-Id（UUID）、
   X-IV、X-Encrypted-Key（均 Base64 编码）
        ↓
6. 计算签名：
   SHA256withRSA(X-App-Id|X-Timestamp|X-Request-Id|X-IV|X-Encrypted-Key|SHA256(requestBody))
   → 写入 X-Sign
        ↓
7. 发送请求（Header 携带元数据，Body 携带 "data" 字段）
```

## 6.2 响应方处理流程

```
1. 接收加密请求
        ↓
2. 从请求头提取 X-Timestamp、X-Request-Id、X-IV、X-Encrypted-Key、X-Sign
        ↓
3. 使用对方 RSA 公钥验证请求签名，遵循 §5.3 的统一公式：
   验证 X-Sign 覆盖  X-App-Id|X-Timestamp|X-Request-Id|X-IV|X-Encrypted-Key|SHA256(requestBody)
   其中 requestBody 为网络上接收到的原始请求体字节。
        ↓
4. 使用本端 RSA 私钥解密 X-Encrypted-Key → AES 会话密钥
        ↓
5. 使用 AES 会话密钥和 X-IV 解密请求体密文
        ↓
6. 获得明文业务数据
        ↓
7. 执行业务逻辑
        ↓
8. 生成新的随机 AES 会话密钥和 IV，使用 AES-256-GCM 加密响应数据
        ↓
9. 使用对方 RSA 公钥加密新的 AES 会话密钥 → encryptedKey
        ↓
10. 构造响应头：X-App-Id（回传请求值）、X-Timestamp、
    X-Request-Id（回传请求值）、X-IV、X-Encrypted-Key；使用 §5.3 的统一公式
    计算响应签名（body = 响应体，使用本端/服务端 RSA 私钥签名）并写入 X-Sign
        ↓
11. 返回加密响应（Header 携带元数据，Body 携带密文）
```

---

# 7. 接口列表

所有业务接口定义（路径、请求参数、响应字段和业务规则）维护在以下在线文档中：

> [MX Co-lending API Interface List](https://docs.google.com/spreadsheets/d/your-doc-id)

---

# 8. 错误码

| 错误码              | 说明                   |
|------------------|----------------------|
| GWS_COMMON_S0001 | 系统错误，请稍后重试           |
| GWS_COMMON_S0002 | 参数无效                 |
| GWS_COMMON_S0003 | 请求路径无效               |
| GWS_COMMON_S0004 | 验签或解密失败              |
| GWS_COMMON_S0005 | AppId 无效或未授权         |
| GWS_COMMON_S0006 | 请求重复 |
| GWS_COMMON_S0007 | 接口限流，请稍后重试           |

---

# 9. 接入准备

正式对接前，双方必须通过邮件交换以下信息：

**合作方 → QFIN：**

| 项目 | 说明 |
|---|---|
| 合作方 RSA 公钥 | 使用 QFIN 提供的密钥生成工具在本地生成。仅通过邮件发送公钥；私钥须严格保密 |

**QFIN → 合作方：**

| 项目 | 说明 |
|---|---|
| QFIN RSA 公钥 | 供合作方用于加密 AES 会话密钥及验证 QFIN 的签名 |
| AppId | 合作方的唯一应用标识，每次请求通过 `X-App-Id` 传递 |
| API Base URL | 所有接口的 Base URL 前缀，如 `https://api.qfin.com/co-lending/v1` |

---
