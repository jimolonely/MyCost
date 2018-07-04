# sql



```sql
create table user(
	id BIGINT not null auto_increment,
	user_name VARCHAR(256) not null,
	password VARCHAR(512) not null,
	PRIMARY key(id)
)

create table person(
	id BIGINT not null auto_increment,
	name VARCHAR(100) not null,
	birthday VARCHAR(100),
	zodiac VARCHAR(30),
	constellation VARCHAR(50),
	age int DEFAULT 0,
	sex VARCHAR(10) not null,
	blood VARCHAR(10),
	education VARCHAR(20),
	job VARCHAR(200),
	remark VARCHAR(1000),
	PRIMARY KEY(id)
) engine=InnoDB DEFAULT charset=utf8;
```
