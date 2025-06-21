package com.lookgen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lookgen.dto.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metric")
public class MetricController {

    private final JdbcTemplate jdbc;
    private final ObjectMapper mapper;

    /* ------ construtor explícito (sem Lombok) ------ */
    @Autowired
    public MetricController(JdbcTemplate jdbc, ObjectMapper mapper) {
        this.jdbc   = jdbc;
        this.mapper = mapper;
    }

    /* ------ endpoint "POST /api/metric" ------------- */
    @PostMapping
    public void track(@RequestBody EventDTO ev) throws Exception {

        String json = (ev.payload() == null)
                ? "{}"
                : mapper.writeValueAsString(ev.payload());

        jdbc.update("""
  INSERT INTO fx_event (id, session_id, event_name, payload)
  VALUES (
    UNHEX(REPLACE(UUID(),'-','')),      -- id gerado no servidor
    UNHEX(REPLACE(?,'-','')),           -- session_id (String UUID)
    ?,                                  -- nome do evento
    CAST(? AS JSON)                     -- payload JSON
  )
  """,
                ev.sessionId().toString(),
                ev.name(),
                json);
    }
}