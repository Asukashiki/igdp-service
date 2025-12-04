# Union/Cooperative Registration API Documentation

## Overview
This document describes the API interfaces for Union/Cooperative registration and audit management.

---

## 1. Registration Management APIs

### 1.1 Add Registration Application

**Endpoint:** `POST /api/seed/registration/add`

**Description:** Zone submits Union registration or Woreda submits Cooperative registration

**Request Body:**

```json
{
  "enterpriseName": "string, required, max 200 chars",
  "enterpriseRegistrationId": "string, optional, max 100 chars",
  "unifiedSocialCreditCode": "string, optional, max 50 chars",
  "seedEnterpriseLicenseNumber": "string, required, max 100 chars",
  "licenseValidityStart": "date, required, format: yyyy-MM-dd",
  "licenseValidityEnd": "date, required, format: yyyy-MM-dd",
  "enterpriseType": "string, required, max 50 chars",
  "orgType": "string, required, enum: union|cooperative",
  "inputTypes": ["string"], "required, array, enum: seed|fertilizer|pesticide",
  "salesRegions": ["string"], "required, array of region codes",
  "remark": "string, optional, max 500 chars",
  "location": {
    "region": "string, optional, max 100 chars",
    "zone": "string, optional, max 100 chars",
    "woreda": "string, required, max 100 chars",
    "kebele": "string, required, max 100 chars",
    "fullAddress": "string, required, max 500 chars",
    "gpsLatitude": "number, optional",
    "gpsLongitude": "number, optional",
    "businessScope": "string, optional, max 500 chars",
    "annualProductionCapacity": "number, optional"
  },
  "licenses": [
    {
      "licenseType": "string, required, enum: business_license|seed_license|tax_certificate|factory_permit",
      "licenseNumber": "string, optional, max 100 chars",
      "licenseFileUrl": "string, required, max 500 chars",
      "issueDate": "date, optional, format: yyyy-MM-dd",
      "expiryDate": "date, optional, format: yyyy-MM-dd"
    }
  ]
}
```

**Response:**

```json
{
  "code": 200,
  "msg": "Operation successful",
  "data": {
    "id": "string, enterprise ID"
  }
}
```

**Business Rules:**
1. Validates required fields
2. Validates license validity dates (start <= end, end >= today)
3. Validates input types are valid enums (seed/fertilizer/pesticide)
4. Validates sales region codes
5. Generates UUID for primary key
6. Initializes application status as "draft"
7. Saves enterprise info, location, and licenses

---

### 1.2 Update Registration Application

**Endpoint:** `POST /api/seed/registration/update`

**Description:** Update draft or rejected registration application

**Request Body:**

```json
{
  "id": "string, required, enterprise ID",
  "version": "integer, required, version number",
  "enterpriseName": "string, required",
  "enterpriseRegistrationId": "string, optional",
  "unifiedSocialCreditCode": "string, optional",
  "seedEnterpriseLicenseNumber": "string, required",
  "licenseValidityStart": "date, required",
  "licenseValidityEnd": "date, required",
  "enterpriseType": "string, required",
  "orgType": "string, required",
  "inputTypes": ["string"], "required",
  "salesRegions": ["string"], "required",
  "remark": "string, optional",
  "location": {
    "id": "string, optional (for update)",
    "region": "string, optional",
    "zone": "string, optional",
    "woreda": "string, required",
    "kebele": "string, required",
    "fullAddress": "string, required",
    "gpsLatitude": "number, optional",
    "gpsLongitude": "number, optional",
    "businessScope": "string, optional",
    "annualProductionCapacity": "number, optional"
  },
  "licenses": [
    {
      "id": "string, optional (for update)",
      "licenseType": "string, required",
      "licenseNumber": "string, optional",
      "licenseFileUrl": "string, required",
      "issueDate": "date, optional",
      "expiryDate": "date, optional"
    }
  ]
}
```

**Response:**

```json
{
  "code": 200,
  "msg": "Operation successful",
  "data": null
}
```

**Business Rules:**
1. Validates enterprise ID exists and not deleted
2. Validates status is "draft" or "rejected"
3. Validates required fields
4. Uses optimistic locking with version number
5. Updates enterprise info, location, and licenses
6. Rejected applications revert to "draft" status after update

---

### 1.3 Submit Registration Application

**Endpoint:** `POST /api/seed/registration/submit`

**Description:** Submit draft application for review

**Request Body:**

```json
{
  "id": "string, required, enterprise ID",
  "version": "integer, required, version number"
}
```

**Response:**

```json
{
  "code": 200,
  "msg": "Submitted successfully",
  "data": null
}
```

**Business Rules:**
1. Validates enterprise ID exists and not deleted
2. Validates status is "draft"
3. Validates all required fields are complete
4. Updates status to "pending"
5. Uses optimistic locking

---

### 1.4 Get Registration Detail

**Endpoint:** `GET /api/seed/registration/detail?id={id}`

**Description:** Query registration application details

**Parameters:**
- `id` (required): Enterprise ID

**Response:**

```json
{
  "code": 200,
  "msg": "Operation successful",
  "data": {
    "id": "string, enterprise ID",
    "enterpriseName": "string, enterprise name",
    "enterpriseRegistrationId": "string, registration ID",
    "unifiedSocialCreditCode": "string, credit code",
    "seedEnterpriseLicenseNumber": "string, license number",
    "licenseValidityStart": "date, license start date",
    "licenseValidityEnd": "date, license end date",
    "enterpriseType": "string, enterprise type",
    "orgType": "string, organization type",
    "inputTypes": ["string, input type array"],
    "salesRegions": ["string, sales region array"],
    "applicationStatus": "string, application status",
    "version": "integer, version number",
    "remark": "string, remark",
    "createdTime": "datetime, creation time",
    "location": {
      "id": "string, location ID",
      "region": "string, region",
      "zone": "string, zone",
      "woreda": "string, woreda",
      "kebele": "string, kebele",
      "fullAddress": "string, full address",
      "gpsLatitude": "number, GPS latitude",
      "gpsLongitude": "number, GPS longitude",
      "businessScope": "string, business scope",
      "annualProductionCapacity": "number, annual production capacity"
    },
    "licenses": [
      {
        "id": "string, license ID",
        "licenseType": "string, license type",
        "licenseTypeName": "string, license type name",
        "licenseNumber": "string, license number",
        "licenseFileUrl": "string, license file URL",
        "issueDate": "date, issue date",
        "expiryDate": "date, expiry date"
      }
    ],
    "auditRecords": [
      {
        "id": "string, audit record ID",
        "auditUserName": "string, auditor name",
        "auditTime": "datetime, audit time",
        "auditResult": "string, audit result",
        "auditResultName": "string, audit result name",
        "auditOpinion": "string, audit opinion"
      }
    ]
  }
}
```

---

### 1.5 Query Registration Applications (Paginated)

**Endpoint:** `POST /api/seed/registration/page`

**Description:** Paginated query of registration applications

**Request Body:**

```json
{
  "pageNum": "integer, required, page number (default: 1)",
  "pageSize": "integer, required, page size (default: 10)",
  "enterpriseName": "string, optional, fuzzy search",
  "orgType": "string, optional, organization type",
  "applicationStatus": "string, optional, application status",
  "inputTypes": "string, optional, input type",
  "woreda": "string, optional, woreda",
  "zone": "string, optional, zone",
  "createdTimeStart": "datetime, optional, creation time start",
  "createdTimeEnd": "datetime, optional, creation time end"
}
```

**Response:**

```json
{
  "code": 200,
  "msg": "Operation successful",
  "data": {
    "records": [
      {
        "id": "string, enterprise ID",
        "enterpriseName": "string, enterprise name",
        "orgType": "string, organization type",
        "orgTypeName": "string, organization type name",
        "inputTypes": "string, input types",
        "applicationStatus": "string, application status",
        "applicationStatusName": "string, status name",
        "woreda": "string, woreda",
        "zone": "string, zone",
        "createdTime": "datetime, creation time"
      }
    ],
    "total": "long, total records",
    "size": "long, page size",
    "current": "long, current page",
    "pages": "long, total pages"
  }
}
```

**Business Rules:**
1. Filters by current user's administrative region
2. Supports multi-condition combined query
3. Orders by creation time descending

---

### 1.6 Delete Registration Application

**Endpoint:** `POST /api/seed/registration/delete?id={id}`

**Description:** Delete draft registration application

**Parameters:**
- `id` (required): Enterprise ID

**Response:**

```json
{
  "code": 200,
  "msg": "Deleted successfully",
  "data": null
}
```

**Business Rules:**
1. Validates enterprise ID exists
2. Validates status is "draft" (other statuses cannot be deleted)
3. Logically deletes enterprise info
4. Logically deletes associated location info
5. Logically deletes associated licenses

---

## 2. Registration Audit APIs

### 2.1 Query Pending Audits (Paginated)

**Endpoint:** `POST /api/seed/registration/audit/page`

**Description:** Paginated query of pending audit applications

**Request Body:**

```json
{
  "pageNum": "integer, required, page number",
  "pageSize": "integer, required, page size",
  "enterpriseName": "string, optional, fuzzy search",
  "orgType": "string, optional, organization type",
  "createdTimeStart": "datetime, optional, application time start",
  "createdTimeEnd": "datetime, optional, application time end"
}
```

**Response:**

```json
{
  "code": 200,
  "msg": "Operation successful",
  "data": {
    "records": [
      {
        "id": "string, enterprise ID",
        "enterpriseName": "string, enterprise name",
        "orgType": "string, organization type",
        "orgTypeName": "string, organization type name",
        "inputTypes": "string, input types",
        "woreda": "string, woreda",
        "zone": "string, zone",
        "createdTime": "datetime, application time"
      }
    ],
    "total": "long, total records",
    "size": "long, page size",
    "current": "long, current page",
    "pages": "long, total pages"
  }
}
```

**Business Rules:**
1. Determines audit permission scope based on user role
2. Zone users can only view Union applications in their jurisdiction
3. Woreda users can only view Cooperative applications in their jurisdiction
4. Filters applications with "pending" status

---

### 2.2 Approve Application

**Endpoint:** `POST /api/seed/registration/audit/approve`

**Description:** Approve registration application

**Request Body:**

```json
{
  "id": "string, required, enterprise ID",
  "version": "integer, required, version number",
  "remark": "string, optional, audit remark (max 500 chars)"
}
```

**Response:**

```json
{
  "code": 200,
  "msg": "Approved successfully",
  "data": null
}
```

**Business Rules:**
1. Validates enterprise ID exists and status is "pending"
2. Validates current user has audit permission
3. Updates status to "approved"
4. Creates audit record
5. Opens system access for the organization (calls permission service)
6. Uses optimistic locking

---

### 2.3 Reject Application

**Endpoint:** `POST /api/seed/registration/audit/reject`

**Description:** Reject registration application

**Request Body:**

```json
{
  "id": "string, required, enterprise ID",
  "version": "integer, required, version number",
  "auditOpinion": "string, required, audit opinion (max 1000 chars)",
  "remark": "string, optional, audit remark (max 500 chars)"
}
```

**Response:**

```json
{
  "code": 200,
  "msg": "Rejected successfully",
  "data": null
}
```

**Business Rules:**
1. Validates enterprise ID exists and status is "pending"
2. Validates current user has audit permission
3. Validates audit opinion is required
4. Updates status to "rejected"
5. Creates audit record
6. Uses optimistic locking

---

### 2.4 Query Audit Records

**Endpoint:** `GET /api/seed/registration/audit/list?enterpriseId={enterpriseId}`

**Description:** Query audit record list for an enterprise

**Parameters:**
- `enterpriseId` (required): Enterprise ID

**Response:**

```json
{
  "code": 200,
  "msg": "Operation successful",
  "data": [
    {
      "id": "string, audit record ID",
      "auditUserName": "string, auditor name",
      "auditTime": "datetime, audit time",
      "auditResult": "string, audit result",
      "auditResultName": "string, audit result name",
      "auditOpinion": "string, audit opinion",
      "remark": "string, remark"
    }
  ]
}
```

**Business Rules:**
1. Queries all audit records by enterprise ID
2. Orders by audit time descending

---

## 3. Enumeration Values

### 3.1 Organization Type (orgType)
- `union` - Union
- `cooperative` - Cooperative

### 3.2 Application Status (applicationStatus)
- `draft` - Draft
- `pending` - Pending Audit
- `approved` - Approved
- `rejected` - Rejected

### 3.3 Input Category (inputTypes)
- `seed` - Seed
- `fertilizer` - Fertilizer
- `pesticide` - Pesticide

### 3.4 License Type (licenseType)
- `business_license` - Business License
- `seed_license` - Seed License
- `tax_certificate` - Tax Certificate
- `factory_permit` - Factory Permit

### 3.5 Audit Result (auditResult)
- `approved` - Approved
- `rejected` - Rejected

---

## 4. Error Codes

| Code | Message | Description |
|------|---------|-------------|
| 200 | Operation successful | Success |
| 500 | {error message} | Business error |

Common error messages:
- "Enterprise name is required"
- "License validity start date cannot be later than end date"
- "License validity end date cannot be earlier than current date"
- "Invalid input type"
- "Invalid organization type"
- "Enterprise not found"
- "Only draft or rejected applications can be updated"
- "Only draft applications can be submitted"
- "Only draft applications can be deleted"
- "Only pending applications can be approved"
- "Only pending applications can be rejected"
- "Audit opinion is required when rejecting"
- "Data has been modified, please refresh and try again"

---

## 5. Notes

1. **Data Permissions**: All query interfaces filter data based on current user's administrative region
2. **Optimistic Locking**: Update operations use version field for optimistic locking
3. **Logical Deletion**: Delete operations use is_deleted field (0: not deleted, 1: deleted)
4. **Audit Trail**: All operations record creator/updater ID and timestamps
5. **Status Workflow**: draft → pending → approved/rejected
6. **Message Language**: All response messages are in English as per requirements
