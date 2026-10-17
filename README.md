<div align="center">

<img width="100%" src="https://capsule-render.vercel.app/api?type=waving&height=220&color=0:110c0c,55:b91c1c,100:facc15&text=GLOBALTRADE&fontSize=55&fontColor=ffffff&desc=Supply%20Chain%20Management%20System&descColor=fff3b0&descSize=21&descAlignY=64" alt="GlobalTrade"/>

**Enterprise-style supply chain management • Jakarta EE • Payara • MySQL**

![Java](https://img.shields.io/badge/Java-17-b91c1c?style=for-the-badge)
![Jakarta EE](https://img.shields.io/badge/Jakarta-EE%2010-facc15?style=for-the-badge&labelColor=18181b)
![Payara](https://img.shields.io/badge/Payara-6-b91c1c?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-Database-facc15?style=for-the-badge&labelColor=18181b)

</div>

## Overview

**GlobalTrade** is a multi-module Jakarta EE project for managing supply chain operations, including vendors, stock, shipments, customs documentation, audit logs and operational alerts.

## Core Modules

| Module | Purpose |
|---|---|
| Inventory | Stock monitoring and reorder information |
| Vendors | Supplier records and performance |
| Shipments | Dispatches, tracking and delivery statuses |
| Customs | Customs documentation and deadline tracking |
| Audit trail | Record operational actions |
| Dashboard & alerts | Operational overviews and notifications |
| Access management | Multiple user roles and authentication |

## Technology

Java 17 • Jakarta EE • EJB • JPA • Servlet/JSP • Maven multi-module • Payara 6 • MySQL • PBKDF2-HMAC-SHA256

## Architecture

```mermaid
flowchart LR
  U[Web user] --> W[Servlet / JSP web module]
  W --> B[EJB business services]
  B --> P[JPA persistence]
  P --> DB[(MySQL globaltrade)]
  B --> T[Timers / Audit / Alerts]
```

## Project Structure

```text
GlobalTrade/
├── globaltrade-web/        # Web UI and servlets
├── globaltrade-ejb/        # Business logic and entities
├── globaltrade-ear/        # Payara EAR packaging / JDBC resources
├── database/               # SQL schema and demo domain data
├── testing/                # Performance test assets
├── tools/                  # Local-only user-password setup
└── pom.xml                 # Maven parent project
```

## Local Setup

1. Install **JDK 17**, **Payara Server 6**, **Maven**, and **MySQL**.
2. Import `database/globaltrade_db.sql` into MySQL. It creates the **`globaltrade`** database and domain demo records but deliberately **does not create login accounts**.
3. Set the **local** MySQL username/password in `globaltrade-ear/src/main/application/META-INF/payara-resources.xml`. The committed password is intentionally blank. **Do not commit real credentials**; restore the blank placeholder before pushing future changes.
4. Generate your own local login passwords:

   ```powershell
   python tools/create_local_users.py
   ```

   Enter new passwords for whichever accounts you need: `admin`, `logistics`, `warehouse`, `customs`, `vendor`, `customer` (press Enter to skip an account). This writes **`local_users.sql`**, which is ignored by Git.
5. Import `local_users.sql` into your local MySQL database. **The script generates hashes, not a plaintext password list.** Keep your chosen passwords safely stored.
6. Build with `mvn clean package` from the project root; deploy the generated EAR to Payara and verify the JDBC pool/resource configuration.

### Database login accounts

**There is no recoverable original password in a PBKDF2 hash.** The usernames above are included as optional local development accounts, but this public repository does not ship active login passwords. Generate your own using the included script.

### Security

- Real database credentials were removed from the committed Payara configuration.
- Precomputed login credential hashes were removed from the public SQL seed.
- Generated `local_users.sql` must remain local; it is excluded from Git.
- If a real database password was previously committed or reused, **rotate it** before publication; removing it from a new copy does not make old copies safe.
- The code has not received a full security audit. Review authentication, authorization and SQL handling before production use.

## Project Status

Academic project. The archive was cleaned and inspected for GitHub publishing; **full Maven build, deployment and runtime testing were not performed** in this preparation step.

---

<div align="center">

**Developed by [Akash Amarasinghe](https://github.com/AkashAmarasinghe)**

<img width="100%" src="https://capsule-render.vercel.app/api?type=waving&section=footer&height=120&color=0:110c0c,55:b91c1c,100:facc15" alt="Footer"/>

</div>
