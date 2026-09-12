# X Synapse 后端开发总结（复习精简版）

**技术栈：** Spring Boot 4 + MyBatis + MySQL + Apifox

---

## 1. 问题：Apifox 浏览器版请求失败（Mixed Content）

- **现象**：HTTPS 页面请求 HTTP localhost 被浏览器拦截（Mixed Content），请求根本没发出
- **关键认知**：请求失败不一定是后端代码错，要**逐层定位**：客户端 → HTTP 请求是否发出 → 网络 → Spring Boot → Controller → Service → Mapper → MySQL
- **解决**：改用 Apifox 桌面端

## 2. 问题：用户名校验正则写错

```java
// 错误：只允许 5~16 个空白字符
"^\s{5,16}$"

// 正确：5~16 位字母或数字
@Pattern(regexp = "^[a-zA-Z0-9]{5,16}$")
```

- **复习点**：`^` 开始、`$` 结束、`{5,16}` 长度限制

## 3. 问题：Apifox 传 "can" 被拦截

- `@Pattern` 等校验在 **Controller 层、Service 执行之前**生效，校验失败直接返回错误，后续代码不执行
- **流程**：Apifox → Controller → 参数校验 →（失败→返回）→ Service → Mapper → DB

## 4. 问题：表名 `user` 在 IDEA 标红

- `USER` 是数据库语境下的保留/特殊词
- **解决**：SQL 中用反引号转义：`SELECT * FROM big_event.\`user\`;`
- **实践建议**：尽量避开保留关键字做表名

## 5. 问题：驼峰 vs 下划线字段名

- Java 实体：`createTime` / 数据库字段：`create_time`
- `map-underscore-to-camel-case: true` **只负责查询结果映射**，不会修改你手写 SQL
- **解决**：SQL 中必须写真实字段名 `create_time`，实体里写 `createTime`
- **核心认知**：Java 字段名和数据库字段名是两套命名体系，MyBatis 只做映射，不改 SQL

## 6. 密码处理

- 注册时存 MD5 摘要，登录时 `MD5(输入密码)` 与数据库摘要比较
- 明文密码永不落库
- **注意**：MD5 仅适合学习阶段，生产环境应改用 BCrypt / Argon2

## 7. 核心 Bug：登录时 `password == null`（最典型）

- 现象：MD5(输入密码) 与数据库密码明明一致，但查出的 `user.getPassword()` 是 null
- 定位：Mapper SQL 只 `select username`，没查 `password`，所以对象里其他字段全是 null
- **修复**：`select id, username, password, avatar, bio, create_time, update_time from ...`（开发期可用 `select *`）
- **万能排查清单**（遇到「DB 有数据但 Java 是 null」）：

1. SQL 有没有查这个字段
2. SQL 字段名是否正确
3. MyBatis 映射是否正确
4. Java 实体有没有这个属性
5. Getter / Setter 是否正常

## 8. 问题：MySQL 用户名大小写不敏感

- 数据库 `CanLu`，输入 `canlu` 也能查到
- **原因**：Collation（排序规则），如 `utf8mb4_0900_ai_ci`，`_ci` = case-insensitive（不区分大小写）
- **解决方向**（如需大小写敏感）：username 字段 Collation 改为 `utf8mb4_bin`
- **认知**：数据库设计（字符集/Collation）会影响业务行为

## 9. 完整登录流程

Apifox → POST /login → Controller → 参数校验 → Service → Mapper → MySQL → MyBatis 映射 → User 对象 → MD5(输入密码) 与 user.password 比较 → 相同=登录成功 / 不同=密码错误

## 10. 阶段性收获（4 点）

1. **分层意识**：Controller → Service → Mapper → MySQL，各司其职
2. **MyBatis 本质**：Java 对象与 SQL/数据库之间的**映射框架**，不是自动操作数据库
3. **调试思维**：通过现象建立假设 → 用日志/数据验证假设，而非盲目改代码
4. **全局观**：Java、MySQL、ORM、HTTP、业务规则是连在一起的

#  今天 X-Synapse 问题总结

@RequestHeader 写错
@RequestHeader User user
→ Header 里找 User，错误。
改：
@RequestBody User user
→ 从 JSON Body 获取 User。
URL 参数 ≠ Body
/update?username=canlu
→ @RequestParam
{"username":"canlu"}
→ @RequestBody
没传 username 也返回成功
username = null
WHERE username = null 匹配不到数据
但代码无论如何都 Result.success()
所以：接口成功 ≠ 数据库真的更新成功
JDBC 连接异常
CannotGetJdbcConnectionException
→ 数据库连接问题，不是 Controller/SQL 逻辑问题。
JWT
登录后 JWT 可以保存 userId/username
后续请求通过 JWT → 拦截器 → ThreadLocal 获取当前用户
更新用户时就不必让前端传 username
一句话记忆
URL 找资源，Header 放请求信息，Body 放业务数据，JWT 负责身份。

## 12. 下一步路线图

注册 ✅ → 登录 ✅ → **JWT 身份认证**（下一阶段关键） ✅→ 登录状态维护 ✅ → 用户信息接口 ✅ → 帖子 → 评论 → 点赞