package com.kh.bbs.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOARD")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq")
  @SequenceGenerator(name = "board_seq", sequenceName = "board_board_id_seq", allocationSize = 1)
  private Long boardId;

  @Column(nullable = false, length = 200)
  private String title;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false, length = 50)
  private String writer;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  //
  @PrePersist
  public void prePersis() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }



}
