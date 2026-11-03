# 苍穹外卖 sky-take-out

## 本地配置

将 `sky-server/src/main/resources/application-dev.example.yml` 复制为同目录下的 `application-dev.yml`，然后在 IDE 的运行配置或启动环境中设置：

- `DB_PASSWORD`：本地 MySQL 密码。
- `ALIOSS_ENDPOINT`、`ALIOSS_ACCESS_KEY_ID`、`ALIOSS_ACCESS_KEY_SECRET`、`ALIOSS_BUCKET_NAME`：阿里云 OSS 配置。
- 可选：`DB_HOST`、`DB_PORT`、`DB_NAME`、`DB_USERNAME`。

也可以只在被忽略的 `application-dev.yml` 中填写本机配置。该文件及 `.env` 不应提交；示例配置中仅保留环境变量占位符。已有本地配置可继续使用。

使用 JDK 8 和 Maven，在项目根目录执行 `mvn clean package -DskipTests` 构建，然后运行 `sky-server` 模块。
