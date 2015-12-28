package ru.kasuhanov.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

}