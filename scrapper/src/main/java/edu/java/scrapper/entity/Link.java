package edu.java.scrapper.entity;

import edu.java.core.entity.enums.ResourceType;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Link {

    private Long id;
    private String url;
    private OffsetDateTime updatedAt;
    private OffsetDateTime lastCheck;
    private ResourceType resourceType;
    private OffsetDateTime githubUpdatedAt;
    private OffsetDateTime stackoverflowLastEditDateQuestion;
}
