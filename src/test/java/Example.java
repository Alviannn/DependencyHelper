import dev.luckynetwork.lib.dependencyhelper.DependencyHelper;
import dev.luckynetwork.lib.dependencyhelper.profile.DependencyProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Example {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<DependencyProfile> dependencies = new ArrayList<>();

        dependencies.add(new DependencyProfile("asm-7.1.jar", "https://repo1.maven.org/maven2/org/ow2/asm/asm/7.1/asm-7.1.jar"));
        dependencies.add(new DependencyProfile("asm-commons-7.1.jar", "https://repo1.maven.org/maven2/org/ow2/asm/asm-commons/7.1/asm-commons-7.1.jar"));
        dependencies.add(new DependencyProfile("jar-relocator-1.3.jar", "https://search.maven.org/remotecontent?filepath=me/lucko/jar-relocator/1.3/jar-relocator-1.3.jar"));

        dependencies.add(new DependencyProfile("mysql-connector-java-8.0.16.jar", "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.16/mysql-connector-java-8.0.16.jar"));
        dependencies.add(new DependencyProfile("slf4j-nop-1.7.26.jar", "https://repo1.maven.org/maven2/org/slf4j/slf4j-nop/1.7.26/slf4j-nop-1.7.26.jar"));
        dependencies.add(new DependencyProfile("slf4j-api-1.7.26.jar", "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.26/slf4j-api-1.7.26.jar"));
        dependencies.add(new DependencyProfile("HikariCP-3.3.1.jar", "https://repo1.maven.org/maven2/com/zaxxer/HikariCP/3.3.1/HikariCP-3.3.1.jar"));
        dependencies.add(new DependencyProfile("H2-1.4.196.jar", "https://repo1.maven.org/maven2/com/h2database/h2/1.4.196/h2-1.4.196.jar"));
        dependencies.add(new DependencyProfile("sqlite-jdbc-3.28.0.jar", "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.28.0/sqlite-jdbc-3.28.0.jar"));
        dependencies.add(new DependencyProfile("json-simple-1.1.1.jar", "https://repo1.maven.org/maven2/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar"));

        dependencies.add(new DependencyProfile("JDA-4.BETA.0_32.jar", "https://bintray.com/dv8fromtheworld/maven/download_file?file_path=net%2Fdv8tion%2FJDA%2F4.BETA.0_32%2FJDA-4.BETA.0_32.jar"));

        DependencyHelper helper = new DependencyHelper(Example.class.getClassLoader());
        File dir = new File("depends");
        Logger logger = Logger.getLogger("Library");

        for (DependencyProfile dependency : dependencies) {
            try {
                helper.download(dependency, dir.toPath());
            } catch (Exception e) {
                logger.warning(e.getMessage());
            }
        }

        for (DependencyProfile dependency : dependencies) {
            try {
                helper.load(dependency, dir.toPath());
            } catch (Exception e) {
                logger.warning(e.getMessage());
            }
        }

        logger.info("Loaded and/or downloaded all dependencies in " + (System.currentTimeMillis() - start) + " ms!");
    }

}
