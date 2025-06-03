package in.ac.daiict.deep.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
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
public class InstanceCreationService {

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
        createEntityManagerFactory("public");
    }

    public void createSchemaAndSwitch(String schemaName) {
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
        createEntityManagerFactory(schemaName);
    }

    private void createEntityManagerFactory(String schemaName) {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.default_schema", schemaName);
        props.put("hibernate.hbm2ddl.auto", "update");
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