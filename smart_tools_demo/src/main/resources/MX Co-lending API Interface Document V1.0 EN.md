
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
| Key Encryption | RSA-2048 | Encrypts the AES session key. Padding is fixed to `RSA/ECB/OAEPWithSHA-256AndMGF1Padding`: OAEP hash = SHA-256, MGF1 hash = SHA-256, label = empty. Both hashes must be SHA-256 — using the common MGF1-SHA1 default will cause decryption to fail |
| Data Encryption | AES-256-GCM | AEAD mode — ensures both confidentiality and integrity, tamper-resistant. Authentication tag length = 128 bits (16 bytes). The encrypted `data` value is `Base64( ciphertext \|\| authTag )`: the 16-byte tag is appended to the ciphertext and Base64-encoded as a single unit; the receiver splits the last 16 bytes as the tag. IV is 12 bytes, carried in `X-IV` |
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

> **Note**: The `data` value is `Base64( ciphertext || authTag )`, where `authTag` is the 16-byte (128-bit) GCM authentication tag appended to the ciphertext. The IV is carried separately in `X-IV` (12 bytes, Base64). The encryption algorithm is fixed as AES-256-GCM and the signature algorithm is fixed as SHA256withRSA. Neither needs to be included in each request.

## 4.3 Signature Specification

The signature covers all header fields and the full body to prevent tampering of any individual field. **The same signature formula is used for both requests and responses** — they differ only in which party signs and which body is hashed. This avoids any ambiguity between request-side and response-side signing.

**Unified Signature Formula:**

```
SignContent = X-App-Id|X-Timestamp|X-Request-Id|X-IV|X-Encrypted-Key|SHA256(body)
X-Sign      = Base64( SHA256withRSA(sender's RSA private key, SignContent) )
```

Where:

- `body` — the **raw HTTP message body bytes exactly as sent/received on the wire**. For a request it is the request body (`{"data":"..."}`); for a response it is the response body. No JSON re-serialization, reformatting, or whitespace normalization is performed by either side.
- `SHA256(body)` — the SHA-256 digest of `body`, expressed as a **lowercase hex string** before concatenation.
- Fields are concatenated using `|` as a fixed delimiter, in the **exact order shown above**. A field that is empty is kept as an empty string (producing two consecutive `|`); it is never omitted, so the field count is always fixed at 6.

**Request signature** (sent by the caller): signed with the caller's RSA private key; the server verifies it using the caller's RSA public key. Here `body` = request body.

**Response signature** (sent by the server): signed with the server's RSA private key; the caller verifies it using the server's RSA public key. Here `body` = response body. The response carries `X-App-Id` (echoing the request's value) so the very same unified formula applies.

**Worked example (request):**

```
Assume:
  X-App-Id        = PARTNER001
  X-Timestamp     = 1712534400000
  X-Request-Id    = 550e8400-e29b-41d4-a716-446655440000
  X-IV            = Base64(0x00112233445566778899aabbccddeeff)        // 12 bytes
  X-Encrypted-Key = Base64(...)
  requestBody     = {"data":"<Base64 ciphertext>"}                    // raw bytes on the wire
  SHA256(requestBody) = 9a3e...c1f0                                   // lowercase hex

SignContent = PARTNER001|1712534400000|550e8400-e29b-41d4-a716-446655440000|<X-IV>|<X-Encrypted-Key>|9a3e...c1f0
X-Sign      = Base64( SHA256withRSA(caller's RSA private key, SignContent) )
```

> **Note**: The body is hashed with SHA-256 before inclusion in the signature to avoid the performance overhead of signing large payloads directly, while still guaranteeing body integrity.

## 4.4 Encrypted Response Format

**Response Headers**

| Header | Type | Required | Description |
|---|---|---|---|
| X-App-Id | String | Yes | Echoes the request's `X-App-Id`. Included so the unified signature formula (§4.3) applies to responses identically to requests |
| X-Timestamp | String | Yes | Unix timestamp in milliseconds |
| X-Request-Id | String | Yes | Echoes the request's `X-Request-Id`, used to correlate request and response |
| X-IV | String | Yes | Initialization vector, Base64-encoded |
| X-Encrypted-Key | String | Yes | AES session key encrypted with the peer's RSA public key, Base64-encoded |
| X-Sign | String | Yes | Digital signature value, Base64-encoded, computed over the response per §4.3 |

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
3. Verify the request signature using the peer's RSA public key, following the
   unified formula in §4.3:
   verify X-Sign over  X-App-Id|X-Timestamp|X-Request-Id|X-IV|X-Encrypted-Key|SHA256(requestBody)
   where requestBody is the raw request body bytes received on the wire.
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
10. Construct response headers: X-App-Id (echo request value), X-Timestamp,
    X-Request-Id (echo request value), X-IV, X-Encrypted-Key; compute the
    response signature using the unified formula in §4.3 (with body = response
    body, signed with the local/server RSA private key) and write to X-Sign
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
