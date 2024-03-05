package com.nyakako.simplesave.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nyakako.simplesave.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
        boolean existsByCategoryId(Long categoryId);

        List<Transaction> findByUserId(Long userId, Sort sort);

        @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.category.type = :type AND t.date BETWEEN :startDate AND :endDate")
        BigDecimal sumAmountByTypeAndDateRange(@Param("userId") Long userId, @Param("type") String type,
                        @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query("SELECT t.category.id AS categoryId, t.category.name AS categoryName, SUM(t.amount) AS totalAmount " +
                        "FROM Transaction t " +
                        "WHERE t.user.id = :userId " +
                        "AND t.category.type = :type " +
                        "AND t.date BETWEEN :startDate AND :endDate " +
                        "GROUP BY t.category.id, t.category.name " +
                        "ORDER BY totalAmount DESC")
        List<CategorySum> findTotalAmountByCategoryForCurrentUser(@Param("userId") Long userId,
                        @Param("type") String type,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        public interface CategorySum {
                Long getCategoryId();

                String getCategoryName();

                BigDecimal getTotalAmount();
        }

        @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.date BETWEEN :startDate AND :endDate ORDER BY t.date DESC, t.id DESC")
        List<Transaction> findTransactionsForCurrentUserInCurrentMonth(
                        @Param("userId") Long userId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);
}
