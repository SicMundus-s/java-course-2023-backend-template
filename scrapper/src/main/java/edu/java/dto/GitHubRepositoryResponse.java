package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubRepositoryResponse(@JsonProperty("pushed_at") OffsetDateTime pushedAt) {
}
