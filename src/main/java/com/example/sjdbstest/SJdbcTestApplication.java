package com.example.sjdbstest;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@SpringBootApplication
public class SJdbcTestApplication {

  final JdbcTemplate jdbcTemplate;
  final JdbcPersonRepository jdbcPersonRepository;

  public SJdbcTestApplication(JdbcTemplate jdbcTemplate,
      JdbcPersonRepository jdbcPersonRepository) {
    this.jdbcTemplate = jdbcTemplate;
    this.jdbcPersonRepository = jdbcPersonRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(SJdbcTestApplication.class, args);
  }

  @PostConstruct
  public void init() {
    SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
        .withTableName("Person")
        .usingGeneratedKeyColumns("id");
    jdbcTemplate.execute(
        "CREATE TABLE IF NOT EXISTS Person(id INT NOT NULL PRIMARY KEY , name VARCHAR NOT NULL, address VARCHAR NOT NULL )");
    Map<String, String> person1 = Map.of("name", "Evge123", "address", "treqw");
    Map<String, String> person2 = Map.of("name", "Evge11223", "address", "1treqw");
    simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(person1));
    simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(person2));
    System.out.println(jdbcPersonRepository.findAll());
    System.out.println(jdbcPersonRepository.findOne(1));
  }

  @PreDestroy
  public void destroy() {
    jdbcTemplate.execute("DROP TABLE Person");
  }

}
