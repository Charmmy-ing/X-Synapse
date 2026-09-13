# X Synapse

> **Agent-First Social Experience**

X Synapse 是一个以 **AI-Native** 为核心理念设计的开源社交内容平台。它不是一个"加了 AI 功能"的社交应用，而是从架构层面让 AI Agent 成为平台的一等公民——你可以在社交流中直接 `@Agent` 调用 AI 能力，同时享受基于 Signal Protocol 的端到端加密私信。

**X** 代表 eXchange（信息交换）+ Agent eXtension（智能扩展），**Synapse** 代表连接神经元与 Agent 的突触节点。

---

## ✨ 它能做什么

### 🤖 Agent Store：像 @ 好友一样 @ Agent

在社交内容中直接调用 AI 能力：

| Agent | 功能 | 触发方式 |
|-------|------|----------|
| **SummaryAgent** | 长推文 / 线程智能摘要 | `@SummaryAgent 总结这条` |
| **RewriteAgent** | 按指定风格改写内容（正式 / 幽默 / 简洁） | `@RewriteAgent 改得正式一点` |
| **ReplyAgent** | 根据上下文生成回复建议 | `@ReplyAgent 帮我回复` |
| **ScheduleAgent** | 定时发布推文 | `@ScheduleAgent 明天早上9点发` |

调用后 AI 在后台异步处理，结果通过 WebSocket 实时推送，不打断你的浏览。

### 🔐 端到端加密私信

- 基于 **X3DH** 密钥交换，消息使用 **AES-256-GCM** 加密，服务端零知识——平台方也无法读取你的任何消息
- 群聊采用 **Sender Key** 机制，支持大容量加密群聊
- 可选的 LLM 回复草稿：客户端本地解密 → AI 生成 → 本地加密发送，隐私不妥协

### 💬 高并发群聊

- 万级 WebSocket 长连接管理
- Sequence Number 保证群聊消息全局有序
- 断线重连自动补发错过的消息
- 保证AI交流时的信息交互速率

### 🧠 懂你的 AI（社交场景 RAG）

利用你自己发布过的推文构建个性化知识库，让 Agent 的回复基于你的历史内容和风格，而不是泛泛而谈。

---

## 🏗️ 它是怎么实现的

### Java-Python 双栈分层架构

这是本项目的核心设计：Java 承载高并发业务核心，Python 承载 AI 推理与 Agent 编排，各司其职。

| 维度 | Java 层 | Python 层 |
|------|---------|-----------|
| **职责** | 业务逻辑、并发控制、数据持久化、加密网关 | LLM 调用、Agent 编排、文本处理 |
| **优势** | 类型安全、生态成熟、高并发性能优秀 | AI 库丰富、开发效率高、异步原生 |
| **通信** | gRPC / OpenFeign 同步调用（简单查询） | RabbitMQ 异步队列（复杂 AI 任务） |

### 异步 Agent 任务流

LLM 推理耗时数秒，不能阻塞业务线程，因此采用全异步设计：

```
用户 @Agent → Java 立即返回"处理中"
           → 任务入 RabbitMQ 队列
           → Python Celery Worker 消费，LangGraph 编排执行
           → 结果回调 Java，经 WebSocket 推送给用户
```

### Agent 编排（LangGraph）

通过状态机实现意图识别与多 Agent 路由，支持上下文共享与任务切换；配合 Token 预算硬限制，防止异常调用导致的成本失控。

### 端到端加密（Signal Protocol 简化版）

- **X3DH**：首次会话交换公钥，建立共享密钥
- **AES-256-GCM**：消息加密算法
- **服务端零知识**：仅存储密文与公钥
- **前向安全**：定期轮换密钥，历史消息即使长期密钥泄露也无法解密

### 技术栈

**Java 后端**：Spring Boot 3 · Spring Cloud Gateway · Netty WebSocket · MyBatis-Plus · PostgreSQL · Redis · RabbitMQ · JWT

**Python AI 服务**：FastAPI · Celery · LangGraph · OpenAI / Claude API · Ollama（本地模型 / 降级方案）· ChromaDB · sentence-transformers

**DevOps**：Docker Compose · Nginx · GitHub Actions · Swagger / OpenAPI

---

## 🎯 与现有平台的对比

| 特性 | X (Twitter) | 传统 IM | **X Synapse** |
|------|-------------|---------|-----------------|
| Agent 内嵌 | ❌ | ❌ | ✅ 原生 `@Agent` 调用 |
| 端到端加密 | ❌ | ⚠️ 部分支持 | ✅ Signal Protocol |
| 大群聊加密 | N/A | ❌ | ✅ Sender Key |
| LLM 辅助回复 | ❌ | ❌ | ✅ 客户端侧可选开启 |
| 开源可扩展 | ❌ | ❌ | ✅ |

---

## 📄 相关文档

- [架构设计文档](./docs/architecture.md)
- [基于 LangGraph 的社交平台多 Agent 编排实践](./docs/langgraph-agents.md)
- [Signal Protocol 在社交私信中的工程化落地](./docs/e2ee.md)

---

## 📬 联系

- GitHub：https://github.com/Charmmy-ing
- 邮箱：lc5207242.gmail.com
- WeChat：LCORN0106
---

*License: MIT*
