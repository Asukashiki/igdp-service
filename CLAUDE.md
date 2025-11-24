# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**IGDP (焱宇行业大模型平台数字底座)** - Industry Large Model Platform Digital Foundation

A multi-module Spring Boot application built on the RuoYi-Vue framework v3.8.7, customized for industry-specific AI model platform services.

## During execution:

- Do not generate documentation files (.md) for the code, test cases for the code, or execute any commands to run processes.
- For maximum efficiency, when you need to perform multiple independent operations, invoke all relevant tools simultaneously rather than sequentially.
- If you create any temporary new files, scripts, or auxiliary files for iteration, clean up by deleting these files upon task completion.


## Technology Stack

- **Java**: 1.8
- **Spring Boot**: 2.7.6
- **Maven**: 3.6.3
- **Authentication**: Sa-Token 1.37.0 (replaced traditional JWT)
- **ORM**: MyBatis Plus with logical delete support
- **Configuration**: Nacos for centralized configuration management
- **Databases**: MySQL, HighGo (瀚高), DM8 (达梦)
- **Caching**: Redis with Jackson serialization
- **File Storage**: MinIO 8.2.2
- **Messaging**: RocketMQ (optional, currently disabled)
- **Utilities**: Hutool 5.8.27, Fastjson2 2.0.43

## Module Architecture

### Core Modules

- **igdp-admin**: Main application entry point and web layer
  - Location: `igdp-admin/src/main/java/com/inspur/IgdpApplication.java`
  - Handles HTTP requests, integrates all business modules
  - Built artifact: `yy-service.jar`

- **igdp-framework**: Core framework layer
  - Aspectj: AOP functionality for logging, permissions, etc.
  - Config: Spring configuration classes (security, thread pool, etc.)
  - Datasource: Dynamic datasource switching
  - Interceptor: Request interceptors
  - Manager: Async task manager, cache management
  - Web: Web layer utilities (domain models, exception handlers)

- **igdp-common**: Shared utilities across all modules
  - Annotation: Custom annotations (data permissions, logs, rate limiting, etc.)
  - Config: Common configuration
  - Constant: System-wide constants
  - Core: Core domain models (page, text, controller base classes)
  - Domain: Common domain objects (R response wrapper, TreeEntity, BaseEntity)
  - Enums: Enumeration types
  - Exception: Custom exception handling
  - Utils: Utility classes (StringUtils, DateUtils, HttpUtils, SecurityUtils, etc.)
  - XSS/Filter: Security filters

- **igdp-system**: System management module
  - User, role, department, menu management
  - Dictionary, parameter, notice management
  - Login logs, operation logs
  - Standard domain/mapper/service structure

### Business Modules

- **igdp-aiagent-square**: AI agent and transformation services
  - Package: `com.inspur.transformation`
  - Handles AI model interaction and data transformation

- **igdp-uc-interface-forward**: User Center interface forwarding
  - Package: `com.inspur.ucif`
  - Proxies and forwards requests to external user management systems

- **igdp-doc**: Document and file storage management
  - Package: `com.inspur.doc`
  - Integrates with MinIO for file operations
  - Handles document upload, download, and management

- **igdp-generator**: Code generator (based on RuoYi)
  - Velocity templates for generating controller/service/mapper/domain/Vue code
  - Supports CRUD generation from database tables

- **igdp-quartz**: Scheduled task module (currently disabled)
  - Quartz job management
  - Commented out in pom.xml

## Build and Run Commands

### Build Project
```bash
# Clean and package all modules
mvn clean package -Dmaven.test.skip=true

# Clean only
mvn clean

# Build specific module
cd igdp-admin && mvn clean package
```

### Run Application
```bash
# Start application (Linux)
./ry.sh start

# Stop application
./ry.sh stop

# Restart application
./ry.sh restart

# Check status
./ry.sh status

# Run directly with Maven (development)
cd igdp-admin
mvn spring-boot:run
```

### Development Mode
```bash
# Run with dev tools (hot reload enabled)
cd igdp-admin
mvn spring-boot:run -Dspring-boot.run.fork=false
```

## Configuration Management

### Nacos Configuration
- **Server**: Configured in `igdp-admin/src/main/resources/bootstrap.yml`
- **Namespace**: `smart_agriculture` (current environment)
- **Shared Config**: `common.yml` contains shared properties across services
- Application-specific configs are loaded from Nacos based on `spring.application.name`

### Database Configuration
- Multi-database support via dynamic datasource
- Database type determined by Nacos configuration
- Supports MySQL, HighGo, and DM8 drivers
- MyBatis Plus logical delete configured: `del_flag` (deleted: "2", active: "0")

### Local Configuration Files
- `bootstrap.yml`: Bootstrap configuration with Nacos connection
- `application.yml.bak`, `application-dev.yml.bak`, `application-prod.yml.bak`: Backup config files (when not using Nacos)
- `logback.xml`: Logging configuration

## Important Conventions

### Package Structure
- Controllers: `com.inspur.{module}.controller`
- Services: `com.inspur.{module}.service` (interfaces) and `service.impl` (implementations)
- Mappers: `com.inspur.{module}.mapper`
- Domain: `com.inspur.{module}.domain`
- Mapper XML: `resources/mapper/{module}/*Mapper.xml`

### Authentication
- Uses **Sa-Token** instead of traditional JWT
- Session management handled by Sa-Token with Redis
- StpUtil for authentication operations (login, logout, token validation)
- Permission annotations: `@SaCheckPermission`, `@SaCheckRole`

### Data Permissions
- Custom `@DataScope` annotation for row-level data filtering
- Implemented via MyBatis interceptor in framework layer
- Filters data based on user's department and role

### API Response Format
- Standardized response wrapper: `AjaxResult` or `R` class in igdp-common
- Success: `AjaxResult.success(data)` or `R.ok(data)`
- Error: `AjaxResult.error(message)` or `R.fail(message)`

### Logical Delete
- Uses MyBatis Plus logical delete feature
- Field: `del_flag` (String type)
- Deleted value: "2"
- Active value: "0"
- Automatically handled in queries and updates

## Git Workflow

- **Main Branch**: `dev_1.0` (use for pull requests)
- **Current Development Branch**: `dev-1.3.0`
- Follow the existing branch naming convention: `dev-X.Y.Z`

## Code Generation

### Using the Generator Module
1. Access generator through admin interface (typically `/tool/gen`)
2. Import database tables
3. Configure generation options (module name, package name, etc.)
4. Generate code (creates controller, service, mapper, domain, and Vue files)
5. Generated code follows the standard RuoYi structure

### Generator Templates
- Located in: `igdp-generator/src/main/resources/vm/`
- Uses Velocity template engine
- Customizable for project-specific needs

## API Documentation
- Annotations are sufficient for the document

## Common Development Patterns

### Adding a New Business Module
1. Create module directory under project root
2. Add `<module>` entry in root `pom.xml`
3. Create module's `pom.xml` with parent reference
4. Follow standard structure: controller/service/mapper/domain
5. Add module dependency in `igdp-admin/pom.xml`
6. Create MyBatis mapper XML in `resources/mapper/`

### Adding a New API Endpoint
1. Create controller in appropriate module
2. Use `@RestController` and `@RequestMapping`
3. Add `@RequiresPermissions` or `@SaCheckPermission` for authorization
4. Return `AjaxResult` or `R` for standardized responses
6. Create corresponding service interface and implementation

### Database Queries
- Use MyBatis Plus for simple CRUD: extend `BaseMapper<T>`
- Use `@Select`, `@Insert`, `@Update`, `@Delete` for simple queries
- Create XML mapper files for complex queries
- Use `PageHelper` for pagination: `startPage(pageNum, pageSize)`

## Key Files to Review

- `igdp-admin/src/main/java/com/inspur/IgdpApplication.java`: Main application entry
- `igdp-admin/src/main/resources/bootstrap.yml`: Bootstrap configuration
- `igdp-framework/src/main/java/com/inspur/framework/config/`: Framework configurations
- `igdp-common/src/main/java/com/inspur/common/`: Shared utilities and base classes
- Root `pom.xml`: Dependency management and version control

## Database Compatibility

When writing SQL or MyBatis queries, be aware that the application supports multiple databases:
- MySQL (standard)
- HighGo (PostgreSQL-based Chinese database)
- DM8 (达梦, Chinese database)

Avoid database-specific syntax when possible. Use MyBatis Plus or standard SQL.
