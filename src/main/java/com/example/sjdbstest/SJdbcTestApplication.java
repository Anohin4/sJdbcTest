package com.example.sjdbstest;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.jdbi.v3.core.Jdbi;
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
  public void springJdbc() {
    SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
        .withTableName("Person")
        .usingGeneratedKeyColumns("ID");
    jdbcTemplate.execute(
        "CREATE TABLE IF NOT EXISTS Person(id IDENTITY PRIMARY KEY , name VARCHAR NOT NULL, address VARCHAR NOT NULL )");
    MapSqlParameterSource person1 = new MapSqlParameterSource()
        .addValue("name", "Evge123")
        .addValue("address", "treqw");
    MapSqlParameterSource person2 = new MapSqlParameterSource()
        .addValue("name", "12Evge123")
        .addValue("address", "12treqw");

    simpleJdbcInsert.executeAndReturnKey(person1);
    simpleJdbcInsert.executeAndReturnKey(person2);
    System.out.println(jdbcPersonRepository.findAll());
    System.out.println(jdbcPersonRepository.findOne(1));
  }

  @PostConstruct
  public void jdbi() {
    System.out.println("start jdbi test");
    Jdbi jdbi = Jdbi.create("jdbc:h2:mem:sampleDb", "sa", "abc123");
    jdbi.registerRowMapper(Person.class, (rs, ctx) -> new Person(rs.getInt("id"),rs.getString("name"), rs.getString("address")));
    jdbi.useHandle(handle -> {
      handle.execute(
          "CREATE TABLE IF NOT EXISTS Person(id IDENTITY PRIMARY KEY , name VARCHAR NOT NULL, address VARCHAR NOT NULL)");
      handle.execute("INSERT INTO PERSON(id, name, address) VALUES (?,?,?)", 112, "asqewqeqeq",
          "sdcqwerqs");
    });
    List<Person> people = jdbi.withHandle(handle -> handle.createQuery("select * from Person")
        .mapTo(Person.class)
        .list());
    System.out.println(people);
  }

}
