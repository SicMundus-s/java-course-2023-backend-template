package edu.java.core.dto;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, Integer size) {
}
