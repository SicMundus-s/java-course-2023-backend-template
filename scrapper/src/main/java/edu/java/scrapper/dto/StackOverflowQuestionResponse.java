package edu.java.scrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowQuestionResponse(
    @JsonProperty("last_activity_date") Long lastActivityDate,
    @JsonProperty("last_edit_date") Long lastEditDateQuestion
) {

}
