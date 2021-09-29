package com.example.sjdbstest;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPersonRepository implements  PersonRepository{

  final
  JdbcTemplate jdbcTemplate;

  public JdbcPersonRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Iterable<Person> findAll() {
    return jdbcTemplate.query("SELECT * FROM Person", this::mapRowToPerson);
  }

  @Override
  public Person findOne(int id) {
    return jdbcTemplate.queryForObject("SELECT * FROM Person WHERE id=?", this::mapRowToPerson, id);
  }

  @Override
  public Person save(Person person) {
    jdbcTemplate.update("INSERT INTO Person VALUES (?, ?, ?)", person.getId(), person.getName(), person.getAddress());
    return person;
  }

  private Person mapRowToPerson(ResultSet resultSet, int rowNum) throws SQLException {
    return new Person(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("address"));
  }
}
