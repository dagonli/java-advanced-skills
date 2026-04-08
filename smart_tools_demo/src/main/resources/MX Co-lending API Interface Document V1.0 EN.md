
# 1. Overview

## 1.1 Interface Description

All interfaces defined in this specification are transmitted over HTTPS with JSON as the data format. The HTTP method is uniformly **POST** for all endpoints.

- **Protocol**: HTTPS (TLS 1.2 or above)
- **Content-Type**: `application/json`
- **Encoding**: UTF-8
- **Security Requirement**: All business request and response payloads must be encrypted, signed, and verified in accordance with the security specification defined in this document.

---

# 2. Security Mechanism

## 2.1 Dual-Layer Protection

To ensure data security, a **Encryption + Digital Signature** dual-layer protection scheme is adopted:

| Protection Type | Purpose |
|---|---|
| **Encryption** | Protects data confidentiality and prevents data leakage |
| **Digital Signature** | Ensures data integrity, prevents tampering, and authenticates the message origin |

## 2.2 Layered Security Architecture

```
┌──────────────────────────────────────────────────────────────┐
│ Network Layer : TLS 1.2+ (confidentiality & integrity of     │
│                the transport channel)                        │
├──────────────────────────────────────────────────────────────┤
│ Protocol Layer: Message-level security (encryption,          │
│                 signature, verification, replay prevention)  │
├──────────────────────────────────────────────────────────────┤
│ Business Layer: Semantic-level security (field validation,   │
│                 authorization, idempotency)                  │
└──────────────────────────────────────────────────────────────┘
```

---

# 3. Cryptographic Algorithm Specification

## 3.1 Algorithm Suite

The following algorithm combination is adopted: **RSA-2048 + AES-256-GCM + SHA256withRSA**

| Type | Algorithm | Description |
|---|---|---|
| Key Encryption | RSA-2048 | Encrypts the AES session key |
| Data Encryption | AES-256-GCM | AEAD mode — ensures both confidentiality and integrity, tamper-resistant |
| Signature | SHA256withRSA | Message signing to prevent tampering |

---

# 4. Data Format Specification

## 4.1 Encryption Scope

All business data is encrypted as a whole within the request body (Body). No field-level distinction is made — the entire payload is treated as a single unit for encryption.

Protocol metadata (identity, replay-prevention parameters, signature, and encryption parameters) is carried in the request headers. The request body contains only the encrypted business ciphertext.

## 4.2 Encrypted Request Format

**Request Headers**

| Header | Type | Required | Description |
|---|---|---|---|
| X-App-Id | String | Yes | Unique application identifier |
| X-Timestamp | String | Yes | Unix timestamp in milliseconds, e.g. `1712534400000`. Used for replay attack prevention (time window validation) |
| X-Request-Id | String | Yes | Unique request identifier (UUID). Used for request tracing and replay prevention |
| X-IV | String | Yes | Initialization vector, Base64-encoded, 12 bytes for GCM mode |
| X-Encrypted-Key | String | Yes | AES session key encrypted with the peer's RSA public key, Base64-encoded |
| X-Sign | String | Yes | Digital signature value, Base64-encoded |

**Request Body** (`Content-Type: application/json`)

```json
{
  "data": "Base64(AES-256-GCM encrypted business payload)"
}
```

> **Note**: The encryption algorithm is fixed as AES-256-GCM and the signature algorithm is fixed as SHA256withRSA. Neither needs to be included in each request.

## 4.3 Signature Specification

The signature covers all request header fields and the full request body to prevent tampering of any individual field:

```
SignContent = X-App-Id|X-Timestamp|X-Request-Id|X-IV|X-Encrypted-Key|SHA256(requestBody)
X-Sign      = Base64( SHA256withRSA(sender's RSA private key, SignContent) )
```

> **Note**: Fields are concatenated using `|` as a fixed delimiter to avoid ambiguity. The request body is hashed with SHA256 before inclusion in the signature to avoid performance overhead from large payloads, while still guaranteeing body integrity.

## 4.4 Encrypted Response Format

**Response Headers**

| Header | Type | Required | Description |
|---|---|---|---|
| X-Timestamp | String | Yes | Unix timestamp in milliseconds |
| X-Request-Id | String | Yes | Echoes the request's `X-Request-Id`, used to correlate request and response |
| X-IV | String | Yes | Initialization vector, Base64-encoded |
| X-Encrypted-Key | String | Yes | AES session key encrypted with the peer's RSA public key, Base64-encoded |
| X-Sign | String | Yes | Digital signature value, Base64-encoded |

**Response Body** (`Content-Type: application/json`)

```json
{
  "flag": "S/F",
  "code": "string",
  "msg": "string",
  "data": "Base64(AES-256-GCM encrypted business payload)"
}
```

## 4.5 Response Body Fields

| Field | Type | Required | Description |
|---|---|---|---|
| flag | String | Yes | Processing result indicator: `S` = Success, `F` = Failure |
| code | String | Yes | Error code |
| msg | String | No | Error message |
| data | String | No | Encrypted business payload, Base64-encoded |

---

# 5. Encryption / Decryption Flow

## 5.1.1 Request Side Processing Flow

```
1. Build the original business request payload
        ↓
2. Generate a random AES session key and IV (12 bytes)
        ↓
3. Encrypt the request payload using AES-256-GCM → encryptedData
        ↓
4. Encrypt the AES session key using the peer's RSA public key → encryptedKey
        ↓
5. Construct request headers: X-App-Id, X-Timestamp, X-Request-Id (UUID),
   X-IV, X-Encrypted-Key (all Base64-encoded)
        ↓
6. Compute signature:
   SHA256withRSA(X-App-Id|X-Timestamp|X-Request-Id|X-IV|X-Encrypted-Key|SHA256(requestBody))
   → write to X-Sign
        ↓
7. Send request (headers carry metadata, body carries the "data" field)
```

## 5.1.2 Response Side Processing Flow

```
1. Receive the encrypted request
        ↓
2. Extract X-Timestamp, X-Request-Id, X-IV, X-Encrypted-Key, X-Sign from headers
        ↓
3. Verify request signature using the peer's RSA public key:
   SHA256withRSA(X-Timestamp|X-Request-Id|X-IV|X-Encrypted-Key|SHA256(responseBody))
        ↓
4. Decrypt X-Encrypted-Key using the local RSA private key → AES session key
        ↓
5. Decrypt the request body ciphertext using the AES session key and X-IV
        ↓
6. Obtain the plaintext business payload
        ↓
7. Execute business logic
        ↓
8. Generate a new random AES session key and IV, encrypt the response payload
   using AES-256-GCM
        ↓
9. Encrypt the new AES session key using the peer's RSA public key → encryptedKey
        ↓
10. Construct response headers: X-Timestamp, X-Request-Id (echo request value),
    X-IV, X-Encrypted-Key; compute signature and write to X-Sign
        ↓
11. Return encrypted response (headers carry metadata, body carries ciphertext)
```

---

# 6. Interface List

All business interface definitions (paths, request parameters, response fields, and business rules) are maintained in the following online document:

> [MX Co-lending API Interface List](https://docs.google.com/spreadsheets/d/your-doc-id)

---

# 7. Error Codes

| Error Code | Description |
|---|---|
| GWS_COMMON_S0001 | System error, please try again later |
| GWS_COMMON_S0002 | Invalid parameter |
| GWS_COMMON_S0003 | Decryption failed |
| GWS_COMMON_S0004 | Signature verification failed |

---

# 8. Onboarding Preparation

Before formal integration, both parties must exchange the following information via email:

**Partner → QFIN:**

| Item | Description |
|---|---|
| Partner RSA Public Key | Generated locally using the key generation tool provided by QFIN. Send only the public key via email; keep the private key strictly confidential |

**QFIN → Partner:**

| Item | Description |
|---|---|
| QFIN RSA Public Key | Used by the partner to encrypt the AES session key and verify QFIN's signature |
| AppId | The partner's unique application identifier, passed in every request via `X-App-Id` |
| API Base URL | The base URL prefix for all endpoints, e.g. `https://api.qfin.com/co-lending/v1` |

---
