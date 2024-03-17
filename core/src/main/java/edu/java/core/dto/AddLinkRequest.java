package edu.java.core.dto;

import edu.java.core.entity.enums.ResourceType;
import lombok.Data;

@Data
public class AddLinkRequest {
    private String link;
    private ResourceType resourceType;
}
