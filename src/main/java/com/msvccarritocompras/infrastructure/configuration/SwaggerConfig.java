package com.msvccarritocompras.infrastructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API CARRITO SERVICE",
                description = "Una api que controla los cupones de compra , y el carrito de compras ",
                version = "1.0.0",
                contact = @Contact(
                        name = "erik antony"
                )
        ),
        servers = {
                @Server(
                        description = "DEV SERVER",
                        url = "http://localhost:8085"
                ),
                @Server(
                        description = "PROD SERVER",
                        url = ""
                )
        }
)
public class SwaggerConfig {
}
