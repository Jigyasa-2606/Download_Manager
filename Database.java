package me.groot.downloadmanager.database;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.h2.H2ConnectionOption;
import io.r2dbc.spi.ConnectionFactory;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultConfiguration;

public class Database {

    private final DatabaseSettings settings;
    private ConnectionFactory factory;
    private DSLContext context;

    public Database(DatabaseSettings settings) {
        this.settings = settings;
    }

    public void create() {
        factory = new H2ConnectionFactory(
                H2ConnectionConfiguration.builder()
                        .file(settings.file())
                        .username(settings.user())
                        .password(settings.password())
                        .property(H2ConnectionOption.DB_CLOSE_DELAY, "-1")
                        .property(H2ConnectionOption.DB_CLOSE_ON_EXIT, "true")
                        .build()
        );
    }

    public void migrate() {
        Flyway.configure(this.getClass().getClassLoader())
                .dataSource("jdbc:h2:file:" + settings.file(), settings.user(), settings.password())
                .driver("org.h2.Driver")
                .locations("classpath:db/migration")
                .validateMigrationNaming(true)
                .baselineOnMigrate(true)
                .baselineVersion("0.0")
                .load()
                .migrate();
    }

    private DSLContext createContext(ConnectionFactory factory) {
        return new DefaultConfiguration()
                .set(factory)
                .set(SQLDialect.H2)
                .set(new Settings().withRenderSchema(false))
                .dsl();
    }

    public DSLContext getContext() {
        return getContext(false);
    }

    public DSLContext getContext(boolean createNew) {
        if (context == null || createNew) {
            context = createContext(factory);
        }
        return context;
    }

}
