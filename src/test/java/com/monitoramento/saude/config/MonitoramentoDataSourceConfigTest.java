package com.monitoramento.saude.config;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import javax.sql.DataSource;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MonitoramentoDataSourceConfigTest {
    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(DataSourceAutoConfiguration.class))
            .withUserConfiguration(MonitoramentoDataSourceConfig.class)
            .withPropertyValues(
                    "monitoramento.datasource.url=jdbc:mysql://127.0.0.1:3306/monitoramento_saude",
                    "monitoramento.datasource.username=monitoramento_test",
                    "monitoramento.datasource.password=test-only",
                    "monitoramento.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
                    "monitoramento.datasource.hikari.maximum-pool-size=7");

    @Test
    void ignoresGlobalDatabaseSettingsFromAnotherProject() {
        runner.withInitializer(context -> context.getEnvironment().getPropertySources().addFirst(
                new SystemEnvironmentPropertySource("other-project", Map.of(
                        "SPRING_DATASOURCE_URL", "jdbc:mysql://db.invalid:3306/administrative_panel",
                        "SPRING_DATASOURCE_USERNAME", "other_user",
                        "SPRING_DATASOURCE_PASSWORD", "other-password"))))
                .run(context -> {
                    assertThat(context).hasNotFailed().hasSingleBean(DataSource.class);
                    HikariDataSource dataSource = context.getBean(HikariDataSource.class);
                    assertThat(dataSource.getJdbcUrl()).isEqualTo("jdbc:mysql://127.0.0.1:3306/monitoramento_saude");
                    assertThat(dataSource.getUsername()).isEqualTo("monitoramento_test");
                    assertThat(dataSource.getPassword()).isEqualTo("test-only");
                    assertThat(dataSource.getMaximumPoolSize()).isEqualTo(7);
                    assertThat(dataSource.getHikariPoolMXBean()).isNull();
                });
    }

    @Test
    void acceptsEnvironmentVariablesSpecificToMonitoramento() {
        runner.withInitializer(context -> context.getEnvironment().getPropertySources().addFirst(
                new SystemEnvironmentPropertySource("monitoramento-environment", Map.of(
                        "MONITORAMENTO_DATASOURCE_URL", "jdbc:mysql://localhost:3306/monitoramento_test",
                        "MONITORAMENTO_DATASOURCE_USERNAME", "specific_user",
                        "MONITORAMENTO_DATASOURCE_PASSWORD", "specific-test-password"))))
                .run(context -> {
                    assertThat(context).hasNotFailed().hasSingleBean(DataSource.class);
                    HikariDataSource dataSource = context.getBean(HikariDataSource.class);
                    assertThat(dataSource.getJdbcUrl()).endsWith("/monitoramento_test");
                    assertThat(dataSource.getUsername()).isEqualTo("specific_user");
                    assertThat(dataSource.getPassword()).isEqualTo("specific-test-password");
                    assertThat(dataSource.getHikariPoolMXBean()).isNull();
                });
    }
}
