# 启动指引（DayAnchor）

## 1. 后端启动（Spring Boot）
在项目根目录执行：

```powershell
cd C:\Users\XN\Desktop\dayanchor
.\mvnw.cmd spring-boot:run
```

说明：
- 使用 Maven Wrapper，无需本机安装 Maven。
- 默认启动端口：`8080`。

## 2. 前端启动（Vite + React）
在前端目录执行：

```powershell
cd C:\Users\XN\Desktop\dayanchor\frontend
npm install
npm run dev
```

说明：
- 首次启动必须先 `npm install`。
- Vite 默认端口：`5173`。

## 3. 访问地址
- 前端：`http://localhost:5173`
- 后端：`http://localhost:8080`

## 4. 常见问题
### 4.1 mvn 无法识别
请使用 Maven Wrapper：

```powershell
.\mvnw.cmd spring-boot:run
```

### 4.2 npm 不存在
说明未安装 Node.js。请安装 LTS 版本（18 或 20）。

### 4.3 前端报找不到 MUI
通常是没有在 `frontend` 目录安装依赖：

```powershell
cd frontend
npm install
```

### 4.4 端口冲突
- 后端：修改 `application.properties` 或换端口启动
- 前端：

```powershell
npm run dev -- --port 5174
```

## 5. 备注
- 前端请求统一走 `/api`，通过 Vite proxy 转发到后端。
- 当前为“方案A”：页面底部输入 User ID，存储在 localStorage。
