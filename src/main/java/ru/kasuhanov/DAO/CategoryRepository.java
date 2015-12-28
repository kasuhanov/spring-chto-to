package ru.kasuhanov.Dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kasuhanov.model.Category;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

}