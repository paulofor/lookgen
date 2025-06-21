package com.lookgen.dto;

import java.util.Map;
import java.util.UUID;

public record EventDTO(
        UUID sessionId,
        String name,
        Map<String, Object> payload // pode ser null
) {}
