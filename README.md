# weibo login java sdk
![image](https://img.shields.io/badge/maven-1.0.1-green.svg)
## 步骤
1. 注入WeiboBasicApi的bean或创建对象
2. getBaseAuthorizeUrl或getMobileAuthorizeUrl创建跳转微博页面
3. getOauth2AccessToken获取accessToken和uid
4. getOauth2UserInfo查询用户信息

## Maven Repository
    https://maven.pkg.github.com/EndOfMaster/maven-repo
### Maven Import
```xml
 <dependency>
   <groupId>com.endofmaster</groupId>
   <artifactId>commons-weibo</artifactId>
   <version>x.y.z</version>
   <type>pom</type>
 </dependency>
```
### Gradle Import
```groovy
 compile 'com.endofmaster:commons-weibo:x.y.z'
```