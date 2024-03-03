package edu.java.scrapper.mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class StackOverflowQuestionResponseMapper {
    public OffsetDateTime mapToOffSetDateTime(Long lastActivityDate) {
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(lastActivityDate), ZoneOffset.UTC);
    }
}
