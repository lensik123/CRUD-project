package ru.baysarov.springcourse.dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.baysarov.springcourse.models.Person;

@Component
public class PersonDao {

  private final JdbcTemplate jdbcTemplate;
  private final BeanPropertyRowMapper<Person> rowMapper = new BeanPropertyRowMapper<>(Person.class);

  @Autowired
  public PersonDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Person> index() {
    return jdbcTemplate.query("SELECT * FROM Person", rowMapper);
  }

  public Person show(int id) {
    return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id}, rowMapper).stream().findAny().orElse(null);
  }

  public void update(int id, Person updatedPerson) {
    jdbcTemplate.update("UPDATE Person SET name = ?, age = ?, email = ? WHERE id = ?", updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), id);
  }

  public void delete(int id) {
    jdbcTemplate.update("DELETE FROM Person WHERE id = ?", id);
  }

  public void save(Person person) {
    Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM Person", Integer.class);
    int newId = (maxId != null ? maxId : 0) + 1;
    jdbcTemplate.update("INSERT INTO Person (id, name, age, email) VALUES (?, ?, ?, ?)", newId, person.getName(), person.getAge(), person.getEmail());
  }
}
