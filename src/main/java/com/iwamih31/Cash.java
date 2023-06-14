package com.iwamih31;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cash")
public class Cash {

  // ID
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;

  // 日付
  @DateTimeFormat(pattern = "yyyy-MM-dd")   // 入力時の期待フォーマット
  @JsonFormat(pattern = "yyyy/MM/dd")   // 出力時の期待フォーマット
  @Column(name = "date", nullable = true)
  private LocalDate date;

  // 10,000
  @Column(name = "man1", nullable = false)
  private Integer man1;

  // 5,000
  @Column(name = "sen5", nullable = false)
  private Integer sen5;

  // 1,000
  @Column(name = "sen1", nullable = false)
  private Integer sen1;

  // 500
  @Column(name = "hyaku5", nullable = false)
  private Integer hyaku5;

  // 100
  @Column(name = "hyaku1", nullable = false)
  private Integer hyaku1;

  // 50
  @Column(name = "jyuu5", nullable = false)
  private Integer jyuu5;

  // 10
  @Column(name = "jyuu1", nullable = false)
  private Integer jyuu1;

  // 5
  @Column(name = "en5", nullable = false)
  private Integer en5;

  // 1
  @Column(name = "en1", nullable = false)
  private Integer en1;

}
