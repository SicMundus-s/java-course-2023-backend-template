package edu.java.scrapper.entity;

import edu.java.core.entity.enums.ResourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "links")
@Entity
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "last_check", nullable = false)
    private OffsetDateTime lastCheck;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false)
    private ResourceType resourceType;

    @Column(name = "github_updated_at")
    private OffsetDateTime githubUpdatedAt;

    @Column(name = "stackoverflow_last_edit_date_question")
    private OffsetDateTime stackoverflowLastEditDateQuestion;

    @ManyToMany(mappedBy = "links")
    private Set<Chat> chats;
}
