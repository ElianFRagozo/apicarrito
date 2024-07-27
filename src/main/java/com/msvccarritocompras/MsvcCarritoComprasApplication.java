package com.msvccarritocompras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcCarritoComprasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcCarritoComprasApplication.class, args);
    }

}
