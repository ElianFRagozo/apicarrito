package com.msvccarritocompras.infrastructure.configuration.kafka.dto;

import java.util.List;

public record KafkaMessageDto(
        String emailUsuario,
        String nombre,
        String apellido,
        List<String> urlImagesList
) {
}
