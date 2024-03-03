package edu.java.scrapper.service;

import edu.java.core.exception.BadRequestException;
import edu.java.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final JdbcTemplate jdbcTemplate;
    public void registreation(Long id) {
        if (isNotRegistered(id)) {
            throw new BadRequestException("Chat is already registered");
        }

        jdbcTemplate.update("INSERT INTO chat(id) VALUES (?)", id);
    }

    public void delete(Long id) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM chat WHERE id = ?", id);
        if (rowsAffected == 0) {
            throw new NotFoundException("Chat with ID: " + id + " does not exist");
        }
    }

    public boolean isNotRegistered(Long id) {
        Long saveId = jdbcTemplate.queryForObject("SELECT id FROM chat WHERE id = ?", Long.class, id);
        return saveId == null || saveId <= 0;
    }
}
