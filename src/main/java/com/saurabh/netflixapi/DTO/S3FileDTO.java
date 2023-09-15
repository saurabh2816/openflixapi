package com.saurabh.netflixapi.DTO;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Builder
@Data
@Getter
@Setter
public class S3FileDTO {
    public S3FileDTO(S3ObjectSummary s3ObjectSummary, URL encodedUrl) {
        this.s3ObjectSummary = s3ObjectSummary;
        this.encodedUrl = encodedUrl;
    }

    private S3ObjectSummary s3ObjectSummary;
    private URL encodedUrl;
}
