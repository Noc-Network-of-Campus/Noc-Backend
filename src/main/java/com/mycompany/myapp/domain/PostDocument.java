package com.mycompany.myapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import lombok.*;

@Document(indexName = "posts")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDocument {
    @Id
    private Long id;

    private String title;
//    private String content;
//    private String category;
//    private LocalDateTime createdAt;
}
