package in.ac.daiict.deep.config;

import in.ac.daiict.deep.constant.database.DBConstants;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Service
public class DBConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManagerFactoryBuilder builder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationContext context;

    private static LocalContainerEntityManagerFactoryBean emfBean;

    @PostConstruct
    public void initDefaultSchema() {
        runFlyway(DBConstants.WORKING_SCHEMA_NAME);
        createEntityManagerFactory(DBConstants.WORKING_SCHEMA_NAME);
    }
    public void runFlyway(String newSchemaName) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(newSchemaName)
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
    }

    public void createSchemaAndSwitch(String schemaName) {
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
//        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS " + DBConstants.FLYWAY_TABLE);
        runFlyway(schemaName);
        createEntityManagerFactory(schemaName);
    }

    private void createEntityManagerFactory(String schemaName) {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.default_schema", schemaName);
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        emfBean = builder
                .dataSource(dataSource)
                .packages("in.ac.daiict.deep.entity")
                .properties(props)
                .persistenceUnit("dynamic")
                .build();
        emfBean.afterPropertiesSet();
    }

    @Primary
    public EntityManagerFactory entityManagerFactory() {
        return emfBean.getObject();
    }

    @Primary
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }
}