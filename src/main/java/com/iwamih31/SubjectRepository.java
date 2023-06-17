package com.iwamih31;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

	/**	name 列の値が引数 name の値と同じ行のvalue列の値を文字列で返す */
	@Query("select subject.subject_value"
			+ " from Subject subject"
			+ " where subject.subject_name = :subject_name")
	public List<String> value(@Param("subject_name")String subject_name);

}
