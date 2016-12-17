# mis-api
---
用springboot做后端以REST风格做一个请假管理系统

### 配置
---
本系统默认将运行在localhost:8080，需要 MySQL 数据库运行，并存在一个名为 ask_for_leave 数据库，数据库可为空; MySQL 数据库的 root 用户密码为 123456。在 src/main/resources/application.property 中可自定义相关设置。 

### 运行方式
---
依次运行

	gradle clean
	gradle build
	gradle bootRun

即可成功运行。

### 接口简要介绍
---
[note.youdao.com/share/?id=72f8b97dc89a99b266744ac7abd7791c&type=note#/](note.youdao.com/share/?id=72f8b97dc89a99b266744ac7abd7791c&type=note#/)