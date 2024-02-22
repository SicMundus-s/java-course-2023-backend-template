package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowQuestionResponse(@JsonProperty("last_activity_date") Long lastActivityDate) {

}
