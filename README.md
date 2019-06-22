# android-app
名称：MyCost

功能：记录日常经济开销记录展示、时间开销记录展示、生活开销记录展示

目的：为了让自己变得更好，记录和分析可以发现自己的不足。PS：生命的意义在于记忆。

详细介绍请看[android-app:MyCost](/android-app)


# 同步设计
客户端：
sync_type:0代表已同步，1,2,3代表增删改

服务端：
modify_time：保存最后修改的时间戳

## 功能：
1. 拉取服务器记录：先同步，再拉取最近的记录（根据时间戳，不过排除sync_type为删除的）覆盖本地
	
2. 上传本地数据到服务器：上传本地sunc_type>0的数据到服务器；服务器只插入记录；根据回应更新本地sync_type=0,删除可以执行

## 情景：
|  |   增加   |   删除 | 修改 |
| :--- | :-----: | -----: | -----: |
| 客户端 |   type=1  |    type=2 | type=3
| 服务器 | 更新时间插入 |    插入 | 更新时间插入

## 缺点：
1. 每次拉取操作需要覆盖数据库，一般很少操作，比如在换了手机后
1. 服务器的数据库只作插入操作，数据量会很大，所以可以定期删除非最新的数据

# family-tree
这是一个家族树管理应用.

