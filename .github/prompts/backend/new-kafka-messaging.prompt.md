---
mode: agent
description: "创建Kafka生产者服务和消费者监听器"
---

我需要为 `{{name}}` 场景实现 Kafka 消息传递。

请生成以下代码：
1.  一个 `{{name | pascal}}Message.java` DTO 类，作为消息体。
2.  一个 `{{name | pascal}}ProducerService.java` 类，注入 `KafkaTemplate`，并包含一个 `sendMessage` 方法。
3.  一个 `{{name | pascal}}ConsumerService.java` 类，使用 `@KafkaListener` 注解来监听指定的 topic，并处理接收到的消息。