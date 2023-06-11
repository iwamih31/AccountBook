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
@Table(name = "action")
public class Action {

  // ID
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;

  // 日付
  @DateTimeFormat(pattern = "yyyy-MM-dd")   // 入力時の期待フォーマット
  @JsonFormat(pattern = "yyyy/MM/dd")   // 出力時の期待フォーマット
  @Column(name = "date", nullable = true)
  private LocalDate date;

  // 科目
  @Column(name = "subject", nullable = false)
  private String subject;

  // 適用
  @Column(name = "apply", nullable = true)
  private String apply;

  // 収入
  @Column(name = "income", nullable = false)
  private Integer income;

  // 支出
  @Column(name = "spending", nullable = false)
  private Integer spending;

}
