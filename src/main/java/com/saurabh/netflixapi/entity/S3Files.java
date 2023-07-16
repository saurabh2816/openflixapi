package com.saurabh.netflixapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "S3Files")
public class S3Files {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="filename")
    private String fileName;

    @Column(name="lastmodified")
    private Date lastModified;

    @Column(name="size")
    private Long size;

    @Column(name="etag")
    private String etag;

    @Column(name="bucket")
    private String bucket;

    @Column(name="visited")
    private boolean visited;

}
