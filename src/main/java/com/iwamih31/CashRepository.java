package com.iwamih31;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CashRepository extends JpaRepository<Cash, Integer> {

	/**	Cash取得（日付指定） */
	@Query("select cash"
			+ " from Cash cash"
			+ " where cash.date = :date")
	public List<Cash> cash(
			@Param("date") LocalDate date
			);

}
