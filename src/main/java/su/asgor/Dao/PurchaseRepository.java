package su.asgor.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import su.asgor.model.Category;
import su.asgor.model.Customer;
import su.asgor.model.Purchase;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface PurchaseRepository extends PagingAndSortingRepository<Purchase, Long>, QueryDslPredicateExecutor<Purchase> {
    Page<Purchase> findByCategories(Category category, Pageable pageable);
    Page<Purchase> findByCustomer(Customer customer, Pageable pageable);
    Page<Purchase> findAllByOrderById(Pageable pageable);
    Page<Purchase> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Purchase> findByNameContainingIgnoreCase(String name);
}