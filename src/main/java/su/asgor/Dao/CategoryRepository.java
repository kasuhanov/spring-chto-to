package su.asgor.Dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import su.asgor.model.Category;

import javax.transaction.Transactional;

@Transactional
@Repository
public abstract class CategoryRepository implements PagingAndSortingRepository<Category, Long> {

}