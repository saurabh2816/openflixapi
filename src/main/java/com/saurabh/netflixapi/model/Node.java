package com.saurabh.netflixapi.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Node {

    private String movieName;
    private String rawMovieName;
    private String text;
    private String link;
    private String strLink;

    private String year;
    private String type;
    private String resolution;

}
