package com.epam.aws.training.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Table(name = "image_metadata")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageMetadata implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Instant lastUpdateDate;

  @Column(unique = true)
  private String name;

  @Column
  private Long sizeInBytes;

  @Column
  private String fileExtension;
}
