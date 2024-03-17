package edu.java.scrapper.mapper.rowmapper;

import edu.java.core.entity.enums.ResourceType;
import edu.java.scrapper.entity.Link;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkRowMapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        Link link = new Link();
        link.setId(rs.getLong("id"));
        link.setUrl(rs.getString("url"));
        link.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime().atOffset(ZoneOffset.UTC));
        link.setLastCheck(rs.getTimestamp("last_check").toLocalDateTime().atOffset(ZoneOffset.UTC));
        link.setResourceType(ResourceType.valueOf(rs.getString("resource_type")));
        link.setGithubUpdatedAt(rs.getTimestamp("github_updated_at").toLocalDateTime().atOffset(ZoneOffset.UTC));
        link.setStackoverflowLastEditDateQuestion(
            rs.getTimestamp("stackoverflow_last_edit_date_question").toLocalDateTime().atOffset(ZoneOffset.UTC));
        return link;
    }
}
