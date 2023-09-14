package test.containers;

import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class AppTest {
    @Test
    public void test() {
        // https://java.testcontainers.org/modules/databases/jdbc/
        // に書いてあるように jdbc:tc:postgis:9.6-2.5:///databasename みたいなURLで接続するか、
        // BaeldungPostgresqlContainer みたいに独自のクラスを作って手動で起動するか。
        //
        // 設定ファイルは https://java.testcontainers.org/features/files/ で上書きできそう。
        //
        System.out.println("START");
        BaeldungPostgresqlContainer
            .getInstance()
            .withEnv("POSTGRES_DB", "test")
            .withEnv("POSTGRES_PASSWORD", "blah")
            .withEnv("POSTGRES_INITDB_ARGS", "-E UTF8")
            .withUsername("tanaka")
            .withPassword("hello")
            .start();
    }

}

class BaeldungPostgresqlContainer extends PostgreSQLContainer<BaeldungPostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:11.1";
    private static BaeldungPostgresqlContainer container;

    private BaeldungPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static BaeldungPostgresqlContainer getInstance() {
        if (container == null) {
            container = new BaeldungPostgresqlContainer();
        }
        return container;
    }

    @Override
    public void start() {

        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.out.println("DB_URL: " + container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.out.println("DB_USERNAME: " + container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
        System.out.println("DB_PASSWORD: " + container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
