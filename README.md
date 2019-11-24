# DependencyHelper
[![](https://jitpack.io/v/Alviannn/DependencyHelper.svg)](https://jitpack.io/#Alviannn/DependencyHelper)

This is a java external dependency loader!

Example code:

```java
public class Example {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Map<String, String> dependencyMap = new HashMap<>();

        dependencyMap.put("asm-7.2.jar", "https://repo1.maven.org/maven2/org/ow2/asm/asm/7.2/asm-7.2.jar");
        dependencyMap.put("asm-commons-7.2.jar", "https://repo1.maven.org/maven2/org/ow2/asm/asm-commons/7.2/asm-commons-7.2.jar");
        dependencyMap.put("jar-relocator-1.3.jar", "https://search.maven.org/remotecontent?filepath=me/lucko/jar-relocator/1.3/jar-relocator-1.3.jar");

        dependencyMap.put("mysql-connector-java-8.0.16.jar", "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.18/mysql-connector-java-8.0.18.jar");
        dependencyMap.put("slf4j-nop-1.7.29.jar", "https://repo1.maven.org/maven2/org/slf4j/slf4j-nop/1.7.29/slf4j-nop-1.7.29.jar");
        dependencyMap.put("slf4j-api-1.7.29.jar", "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.29/slf4j-api-1.7.29.jar");
        dependencyMap.put("HikariCP-3.4.1.jar", "https://repo1.maven.org/maven2/com/zaxxer/HikariCP/3.4.1/HikariCP-3.4.1.jar");
        dependencyMap.put("H2-1.4.200.jar", "https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar");
        dependencyMap.put("gson-2.8.6.jar", "https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar");

        DependencyHelper helper = new DependencyHelper(Example.class.getClassLoader());
        File dir = new File("depends");

        try {
            helper.download(dependencyMap, dir.toPath());
            helper.loadDir(dir.toPath());
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }

        log("Loaded and/or downloaded all dependencies in " + (System.currentTimeMillis() - start) + " ms!");
    }

    private static void log(String message) {
        System.out.println("[Logger - Library]: " + message);
    }

}
```

### How to setup

#### Maven
1. Insert this to your repository section (on the pom.xml)
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

2. Insert this to your dependency section (on the pom.xml)
```xml
<dependency>
    <groupId>com.github.Alviannn</groupId>
    <artifactId>DependencyHelper</artifactId>
    <version>1.2</version>
    <scope>compile</scope>
</dependency>
```