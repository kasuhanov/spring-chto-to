package su.asgor.Dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import su.asgor.model.Category;
import su.asgor.model.Purchase;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface PurchaseRepository extends PagingAndSortingRepository<Purchase, Long> {

    public abstract Page<Purchase> findByCategories(Category category, Pageable pageable);
}