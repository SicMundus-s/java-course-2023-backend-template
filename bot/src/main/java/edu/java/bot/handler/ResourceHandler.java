package edu.java.bot.handler;

import edu.java.core.entity.enums.ResourceType;

public interface ResourceHandler {

    boolean canHandle(String resourceURL);

    default ResourceType getResourceType() {
        return ResourceType.UNKNOWN;
    }

}
