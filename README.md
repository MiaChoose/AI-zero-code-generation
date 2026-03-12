# AI Zero-Code Generation

一个基于 `Spring Boot + Vue 3 + LangChain4j` 的 AI 零代码应用生成平台。  
用户通过自然语言描述需求，系统可流式生成代码、管理应用、部署预览并下载项目。

## 项目简介

本仓库为后端工程 `AI-zero-code-generation`，配套前端工程为 `AI-zero-code-generation-front`。  
项目聚焦“对话式生成前端应用”，支持从 Prompt 到代码产物再到部署链接的完整闭环。

核心能力：

- AI 流式生成：支持 `HTML`、`MULTI_FILE`、`VUE_PROJECT`
- 工具调用编排：读写文件、修改文件、目录读取等 Tool 机制
- 对话历史与状态管理：SSE 实时输出 + 持久化聊天记录
- 一键部署预览：生成代码可直接静态发布并访问
- 后台管理能力：应用、用户、会话等管理接口

## 技术栈

### 后端（本仓库）

- Java 21
- Spring Boot 3.5.11
- MyBatis-Flex
- MySQL 8+
- Redis + Spring Session + Redisson
- LangChain4j 1.1.0 / OpenAI Starter 1.1.0-beta7
- LangGraph4j
- Selenium + WebDriverManager（网页截图）
- Tencent COS SDK
- Knife4j / SpringDoc

### 前端（配套仓库）

- Vue 3 + TypeScript
- Vite
- Pinia
- Vue Router
- Axios
- Ant Design Vue

## 系统架构

### 1) 生成链路（SSE）

1. 前端请求 `GET /api/app/chat/gen/code`
2. 后端按应用类型选择生成策略与 AI 服务
3. `AiCodeGeneratorFacade` 统一处理流式消息
4. 对话流经流处理器后实时返回前端
5. 代码结果落盘并保存聊天记录

### 2) 部署链路

1. 调用 `POST /api/app/deploy`
2. 将产物复制到部署目录（Vue 项目会先构建）
3. 返回部署地址：`/api/static/{deployKey}/`
4. 异步尝试截图并上传（失败不影响主流程）

### 3) 会话与权限

- 登录态：Spring Session + Redis
- 接口权限：注解 + AOP 鉴权
- 前端请求：`withCredentials` 携带会话

## 目录结构

```text
AI-zero-code-generation/
├─ src/main/java/com/miachoose/aizerocodegeneration
│  ├─ controller/          # 业务接口层
│  ├─ service/impl/        # 业务实现层
│  ├─ ai/                  # AI 服务工厂、工具、模型配置
│  ├─ core/                # 生成门面、解析器、保存器、流处理
│  ├─ config/              # CORS、缓存、会话等配置
│  ├─ ratelimter/          # 限流
│  └─ langgraph4j/         # 工作流扩展
├─ src/main/resources
│  ├─ application.yml
│  └─ application-local.yml
└─ tmp/
   ├─ code_output/         # 生成目录
   └─ code_deploy/         # 部署目录
```

## 快速启动

### 1. 环境要求

- JDK 21
- Maven 3.9+
- MySQL 8+
- Redis 6+
- Node.js 18+（前端）
- Chrome（截图功能可选）

### 2. 启动后端

在 `AI-zero-code-generation` 目录执行：

```bash
./mvnw spring-boot:run
```

Windows：

```powershell
.\mvnw.cmd spring-boot:run
```

默认后端地址：`http://localhost:8123/api`

### 3. 启动前端（配套仓库）

在 `AI-zero-code-generation-front` 目录执行：

```bash
npm install
npm run dev
```

默认前端地址：`http://localhost:5173`

## 配置说明

后端主要配置文件：

- `src/main/resources/application.yml`
- `src/main/resources/application-local.yml`

请重点配置：

- 数据库连接：`spring.datasource.*`
- Redis 连接：`spring.data.redis.*`
- AI 模型配置：`langchain4j.open-ai.*`
- 第三方密钥：COS / DashScope / Pexels 等

前端建议配置：

- `VITE_API_BASE_URL=/api`（走 Vite 代理）
- `VITE_DEPLOY_DOMAIN=http://localhost:8123/api/static`

## 常用接口

基于 `context-path=/api`：

- `POST /api/user/login`：登录
- `GET /api/user/get/login`：获取当前用户
- `GET /api/app/chat/gen/code`：SSE 生成代码
- `POST /api/app/chat/stop`：停止当前生成
- `POST /api/app/deploy`：部署应用
- `GET /api/app/download/{appId}`：下载代码包
- `GET /api/static/{deployKey}/...`：访问部署静态资源

接口文档：`http://localhost:8123/api/doc.html`

## 已处理的稳定性设计

- SSE 业务错误统一事件输出（`business-error`）
- 生成任务支持取消（含刷新/离开页面场景）
- 工具调用链路增加取消短路，防止继续写文件
- 异步截图失败降级，不阻断主业务
- 精选应用分页结果缓存 + 管理操作缓存失效

## 常见问题（FAQ）

### 1. 刷新后登录失效

- 确认前端使用 `/api` 代理
- 确认请求携带 `withCredentials: true`
- 检查浏览器是否存在 `SESSION` Cookie

### 2. 截图报 `ERR_CONNECTION_REFUSED`

- 确认部署地址可访问
- 检查 `CODE_DEPLOY_HOST` 是否正确（含端口与路径）

### 3. DeepSeek 报 `Missing reasoning_content`

- 原因是推理模型在工具调用多轮消息下的兼容限制
- 已使用 `deepseek-chat` 处理工具调用场景以提升稳定性

## 安全建议

如配置文件中包含真实密钥，请务必：

- 使用环境变量或配置中心托管密钥
- 及时轮换已暴露密钥
- 避免将敏感信息提交到仓库

## License

当前仓库未附带开源许可证；如需开源发布，请补充 `LICENSE` 文件。

