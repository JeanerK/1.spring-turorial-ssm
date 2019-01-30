# SSM框架搭建过程

需求：一个图书管理系统，为了简单处理，简单做一个单表操作。
建表语句：
```sql
create table IF NOT EXISTS T_BOOKSSS(BOOK_ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT, 
BOOK_NAME VARCHAR(50), AUTHOR VARCHAR(50) , PRICE FLOAT(10.2));

```

## 1. 记住maven工程的目录结构
现在很多ide工具都支持一键生成一个maven工程，给开发带来了很大的便利，但是造成的结果是很多基础较差的开发人员连`页面资源`，`js`静态文件应该放在哪个目录都一脸懵逼，`WEB-INF`这个目录的结构也完全不清楚。

所以建议在家可以手动以一个目录一个目录的方式手动建立一个maven工程，加深印象。

一个普通的web项目的maven目录结构：
<pre>
src
+-main
+---java
+---resources
+---webapp
+-----WEB-INF
+-------web.xml
+-test
+---java
+---resouces
pom.xml
</pre>
其中`java`目录下存放的是java源代码；而`resources`目录用来存放资源文件、配置文件等；`webapp`目录用来存放页面资源文件及`WEB-INF`目录;`WEB-INF`目录下存放`web.xml`文件，这个文件是`JAVAEE`应用的核心配置文件。
`test`目录存放单元代码及资源文件。

当然，`pom.xml`文件就不需要我们去手动一个字符一个符去敲了，去maven官网都能找到一个最简单的maven pom文件。

网址如下：https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html

一个最基本的pom文件如下：
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
 
  <groupId>com.mycompany.app</groupId>
  <artifactId>my-app</artifactId>
  <version>1.0-SNAPSHOT</version>
 
  <properties>
    <maven.compiler.source>1.7</maven.compiler.source>
    <maven.compiler.target>1.7</maven.compiler.target>
  </properties>
 
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```
## 2. 引入maven依赖

引入的依赖主要有spring-core,spring-webmvc, spring-tx, spring-test, mybatis, druid等。
```xml
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>3.1.0</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>jsp-api</artifactId>
      <version>2.0</version>
      <scope>provided</scope>
    </dependency>
    <!--================spring================-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-core</artifactId>
      <version>5.0.9.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>5.0.9.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.0.9.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-tx</artifactId>
      <version>5.0.9.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-test</artifactId>
      <version>5.0.9.RELEASE</version>
    </dependency>

    <!--================mybatis&druid================-->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>1.3.2</version>
    </dependency>
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.4.6</version>
    </dependency>
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.13</version>
    </dependency>
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>1.1.10</version>
    </dependency>

    <!--================log================-->
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-api</artifactId>
      <version>1.7.25</version>
    </dependency>
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-core</artifactId>
      <version>1.2.3</version>
    </dependency>
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <version>1.2.3</version>
    </dependency>

  </dependencies>
```

## 3. 整合spring与mybatis.

准备db的配置文件`src/main/resources/config.propertis`,这个文件存放db的连接信息:

> DB_URL=jdbc:mysql://localhost:3306/jeanerk?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
DB_USERNAME=jeanerk
DB_PASSWORD=admin
DB_DRIVER=com.mysql.cj.jdbc.Driver

配置spring-ioc基本配置文件`src/main/resources/spring/applicationContext-db.xml`,同样地，这个文件的基础模板可以在spring官网找到。

网址如下：


https://docs.spring.io/spring/docs/5.1.4.RELEASE/spring-framework-reference/core.html#spring-core

基础模板如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="..." class="...">   
        <!-- collaborators and configuration for this bean go here -->
    </bean>

    <bean id="..." class="...">
        <!-- collaborators and configuration for this bean go here -->
    </bean>

    <!-- more bean definitions go here -->

</beans>
```

在这里，`applicationContext-db.xml`就是spring的IOC容器(为了便于管理，只将数据库相关的存放在这个文件)，在这个文件中定义的bean都将由spring统一管理，我们可以通过依赖注入的方式来使用通过spring管理的对象。

1. 在包`com.jeanerk.book.dto`建立`book`的实体类;
2. 在包`com.jeanerk.book.mapper`中建立操作数据库的接口`BookMapper.java`；
3. 在`src/main/resources/com/jeanerk/book/mapper`中建立接口的xml映射文件`BookMapper.xml`。
4. 配置数据源
5. 配置`sqlSesstionFactory`.

`applicationContext-db.xml`配置文件如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:tx="http://www.springframework.org/schema/tx" xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <context:property-placeholder location="classpath:config.properties"/>

    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
        <property name="driverClassName" value="${DB_DRIVER}"/>
        <property name="url" value="${DB_URL}"/>
        <property name="username" value="${DB_USERNAME}"/>
        <property name="password" value="${DB_PASSWORD}"/>
        <property name="maxActive" value="50"/>
        <property name="minIdle" value="10"/>
        <property name="maxWait" value="3000"/>
    </bean>

    <!--配置mybatis会话工厂-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="mapperLocations" value="classpath*:/**/*Mapper.xml"/>
        <property name="configLocation" value="classpath:mybatis-configuration.xml"/>
    </bean>

    <!--配置mapper接口扫描器-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.jeanerk.**.mapper"/>
    </bean>

    <!--配置事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!--启用基于注解驱动事务-->
    <tx:annotation-driven/>

</beans>
```

`BookMapper.xml`映射文件如下：
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.jeanerk.book.mapper.BookMapper">

    <parameterMap id="book" type="com.jeanerk.book.dto.Book"/>

    <resultMap id="bookMap" type="com.jeanerk.book.dto.Book"/>

    <update id="updateBook" parameterMap="book">
        update T_BOOK SET BOOK_NAME = #{bookName} where BOOK_ID = #{bookId}
    </update>

    <insert id="insertBook" parameterMap="book">
        insert into T_BOOK(BOOK_NAME, AUTHOR, PRICE)
        VALUES (#{bookName}, #{author}, #{price})
    </insert>

    <select id="selectBooks" parameterMap="book" resultMap="bookMap">
        select
        BOOK_ID,
        BOOK_NAME,
        AUTHOR,
        PRICE
        from T_BOOK
        <where>
            1 = 1
            <if test="bookName != null and bookName != ''">
                and BOOK_NAME = #{bookName}
            </if>
            <if test="bookId != null">
                and BOOK_ID = #{bookId}
            </if>
        </where>

    </select>

    <select id="selectBookById" resultMap="bookMap" parameterType="java.lang.Long">
        select
        BOOK_ID,
        BOOK_NAME,
        AUTHOR,
        PRICE
        from T_BOOK
        where BOOK_ID = #{bookId}
    </select>

    <delete id="deleteProductById" parameterType="java.lang.Long">
        delete from T_BOOK where BOOK_ID = #{bookId}
    </delete>
</mapper>
```

`BookMapper.java`:
```java
package com.jeanerk.book.mapper;

import com.jeanerk.book.dto.Book;

import java.util.List;

public interface BookMapper {

    int updateBook(Book book);

    int insertBook(Book book);

    List<Book> selectBooks(Book book);

    Book selectBookById(Long bookId);

    int deleteProductById(Long bookId);
}

```

## 4. 单元测试
单元测试相关的代码及配置存放在test目录下，test下的目录结构与main目录下的目录结构一致。

通过单元测试，测试spring和mybatis是否整合成功。

`BookMapperTest.java`：
```java
package com.jeanerk.book.mapper;

import com.jeanerk.book.dto.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)//spring提供的测试支持类
@ContextConfiguration(locations = {"classpath*:spring/applicationContext-test.xml"})//单元测试使用单独的配置文件，只是将spring的配置文件复制一份到test目录下相同的位置改了个名字。
@Rollback//这个注解是为了不让单元测试产生垃圾数据或删除了数据库中的真实数据，强制回滚事务。
@Transactional//事务注解
public class BookMapperTest {

    @Autowired
    private BookMapper bookMapper;

    @Test
    public void updateBook() {
        Book book = new Book("TypeScript入门", "廖雪峰", 33.7F);
        book.setBookId(1L);
        int result = bookMapper.updateBook(book);
        assertEquals(1, result);//Assert的方法，断言修改的结果为1条
    }

    @Test
    public void insertBook() {
        Book book = new Book("ES6标准入门", "阮一峰", 37.0F);
        int result = bookMapper.insertBook(book);
        assertEquals(1, result);
    }

    @Test
    public void selectBooks() {
        Book book = new Book(null, "阮一峰", null);
        List<Book> books = bookMapper.selectBooks(book);
        assertTrue("no books found.", !CollectionUtils.isEmpty(books));
    }

    @Test
    public void selectBookById() {
        Book book = bookMapper.selectBookById(2L);
        assertNotNull(book);
    }

    @Test
    public void deleteProductById() {
        int result = bookMapper.deleteProductById(1L);
        assertEquals(1, result);
    }
}
```
spring-mybatis的整合就完成了。
