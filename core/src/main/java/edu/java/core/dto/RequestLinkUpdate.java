package edu.java.core.dto;

import java.util.List;

public record RequestLinkUpdate(Long id,
                                String url,
                                String description,
                                List<Long> tgChatIds) {
}
