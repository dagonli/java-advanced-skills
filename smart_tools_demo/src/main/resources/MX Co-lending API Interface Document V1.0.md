
# 1. 概述

## 1.1 接口说明

本规范所涉及的接口均使用 HTTPS 进行安全传输，数据格式为 JSON，HTTP 方法统一采用 POST。

- **通信协议**：HTTPS（TLS 1.2 及以上）
- **数据格式**：`application/json`
- **字符编码**：UTF-8
- **加解密要求**：所有业务请求与响应的敏感数据需按后文规范进行加密、签名和验签。

---

# 2. 安全机制

## 2.1 双重保护机制

为确保数据安全，采用**加密 + 签名**双重保护机制：


| 保护类型   | 作用                    |
| ------ | --------------------- |
| **加密** | 保护数据隐私，防止数据泄露         |
| **签名** | 确保数据完整性，防止数据篡改，验证数据来源 |


## 2.2 分层安全架构

```
┌─────────────────────────────────────────┐
│ 网络层：TLS 1.2+（保护传输链路机密性与完整性） │
├─────────────────────────────────────────┤
│ 协议层：报文安全机制（加密、签名、验签、防重放） │
├─────────────────────────────────────────┤
│ 业务层：业务语义安全（字段校验、权限、幂等）    │
└─────────────────────────────────────────┘
```

---

# 3. 加解密算法规范

## 3.1 算法套件

采用 **RSA-2048 + AES-256-GCM + SHA256withRSA** 组合：


| 类型   | 算法            | 说明                          |
| ---- | ------------- | --------------------------- |
| 密钥加密 | RSA-2048      | 用于加密AES数据密钥                 |
| 数据加密 | AES-256-GCM   | AEAD认证加密模式，同时保证机密性和完整性，防止篡改 |
| 签名算法 | SHA256withRSA | 消息签名防篡改                     |


---

# 4. 数据格式规范

## 4.1 数据加密范围

所有业务数据统一在请求体（Body）中加密传输，不区分字段，统一作为整体加密处理。

协议元数据（身份标识、防重放参数、签名、加密参数）统一通过请求头传递，请求体仅包含加密后的业务密文。

## 4.2 加密请求格式

**请求头**


| Header          | 类型     | 必填  | 说明                                    |
| --------------- | ------ | --- | ------------------------------------- |
| X-App-Id        | String | 是   | 应用唯一标识                                |
| X-Timestamp     | String | 是   | Unix毫秒时间戳，如：1712534400000，用于防重放时间窗口校验 |
| X-Request-Id   | String | 是   | 请求唯一标识（UUID），用于请求追踪和防重放               |
| X-IV            | String | 是   | 初始向量，Base64编码，GCM模式为12字节              |
| X-Encrypted-Key | String | 是   | RSA加密后的AES会话密钥，Base64编码               |
| X-Sign          | String | 是   | 签名值，Base64编码                          |


**请求体**（`Content-Type: application/json`）

```json
{
  "data": "Base64(AES-256-GCM加密后的业务数据)"
}
```

> **说明**：加密算法固定为 AES-256-GCM，签名算法固定为 SHA256withRSA，无需每次传递。

## 4.3 签名计算规范

签名覆盖所有请求头参数与完整请求体，防止任意字段被篡改：

```
签名原文 = X-App-Id|X-Timestamp|X-Request-Id|X-IV|X-Encrypted-Key|SHA256(requestBody)
X-Sign  = Base64( SHA256withRSA(本端RSA私钥, 签名原文) )
```

> **说明**：各字段之间使用 `|` 作为固定分隔符，防止字段值拼接歧义；对请求体取 SHA256 哈希后纳入签名，避免大报文直接拼接的性能问题，同时保证请求体不可篡改。

## 4.4 加密响应格式

**响应头**


| Header          | 类型     | 必填  | 说明                                    |
| --------------- | ------ | --- | ------------------------------------- |
| X-Timestamp     | String | 是   | Unix毫秒时间戳，如：1712534400000，用于防重放时间窗口校验 |
| X-Request-Id         | String | 是   | 回传请求的 X-Request-Id，用于关联请求与响应               |
| X-IV            | String | 是   | 初始向量，Base64编码                         |
| X-Encrypted-Key | String | 是   | RSA加密后的AES会话密钥，Base64编码               |
| X-Sign          | String | 是   | 签名值，Base64编码                          |


**响应体**（`Content-Type: application/json`）

```json
{
  "flag": "S/F",
  "code": "string",
  "msg": "string",
  "data": "Base64(AES-256-GCM加密后的业务数据)"
}
```

## 4.5 字段说明


| 字段   | 类型     | 必填  | 说明                    |
| ---- | ------ | --- | --------------------- |
| flag | String | 是   | 请求处理结果标识，S/F |
| code | String | 是   | 错误码                   |
| msg  | String | 否   | 错误信息                  |
| data | String | 否   | 加密后的业务数据，Base64编码       |


---

# 5. 加解密流程

## 5.1.1 请求方处理流程

```
1. 构建原始请求数据
        ↓
2. 生成随机 AES 会话密钥和 IV（12字节）
        ↓
3. 使用 AES-256-GCM 加密请求体数据，得到加密数据
        ↓
4. 使用对方 RSA 公钥加密 AES 会话密钥，得到 encryptedKey
        ↓
5. 构造请求头：X-App-Id、X-Timestamp、X-Request-Id（UUID）、X-IV、X-Encrypted-Key（均 Base64编码）
        ↓
6. 计算签名：SHA256withRSA(X-App-Id|X-Timestamp|X-Request-Id|X-IV|X-Encrypted-Key|SHA256(requestBody))，写入 X-Sign
        ↓
7. 发送请求（Header 携带元数据，Body 携带 data 字段）
```

## 5.1.2 响应方处理流程

```
1. 接收加密响应
        ↓
2. 从响应头提取 X-Timestamp、X-Request-Id、X-IV、X-Encrypted-Key、X-Sign
        ↓
3. 验证响应签名：SHA256withRSA(X-Timestamp|X-Request-Id|X-IV|X-Encrypted-Key|SHA256(responseBody))（使用对方 RSA 公钥）
        ↓
4. 使用本端 RSA 私钥解密 X-Encrypted-Key，得到 AES 会话密钥
        ↓
5. 使用 AES 会话密钥和 X-IV 解密响应体密文
        ↓
6. 得到解密后的业务数据
        ↓
7. 执行业务处理
        ↓
8. 生成随机 AES 会话密钥和 IV，使用 AES-256-GCM 加密响应数据
        ↓
9. 使用对方 RSA 公钥加密 AES 会话密钥，得到 encryptedKey
        ↓
10. 构造响应头：X-Timestamp、X-Request-Id（回传请求值）、X-IV、X-Encrypted-Key，计算签名写入 X-Sign
        ↓
11. 返回加密响应（Header 携带元数据，Body 携带密文）
```

---

# 6. 接口列表

所有业务接口定义（路径、请求参数、响应字段、业务规则）统一维护在以下在线文档中：

> [MX Co-lending API Interface List](https://docs.google.com/spreadsheets/d/your-doc-id)

---

# 7. 响应错误码


| 错误码              | 说明    |
|------------------| ----- |
| GWS_COMMON_S0001 | System error, please try again later  |
| GWS_COMMON_S0002 | Param illegal  |
| GWS_COMMON_S0003 | 解密失败  |
| GWS_COMMON_S0004 | 验签失败  |


---

# 8. 接入准备

正式对接前，双方需通过邮件完成以下信息交换：

**合作方发送给 QFIN：**

| 信息 | 说明 |
|------|------|
| 合作方 RSA 公钥 | 使用 QFIN 提供的密钥工具类在本地生成，将公钥通过邮件发送，私钥自行保管 |

**QFIN 发送给合作方：**

| 信息 | 说明 |
|------|------|
| QFIN RSA 公钥 | 合作方用于加密 AES 会话密钥及验证 QFIN 签名 |
| AppId | 合作方唯一标识，每次请求通过 `X-App-Id` 传递 |
| 接口调用地址前缀 | 所有接口的 Base URL，如：`https://api.qfin.com/co-lending/v1` |

---
