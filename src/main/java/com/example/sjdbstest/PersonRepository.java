package com.example.sjdbstest;

public interface PersonRepository {
  Iterable<Person> findAll();

  Person findOne(int id);

  Person save(Person person);


}
