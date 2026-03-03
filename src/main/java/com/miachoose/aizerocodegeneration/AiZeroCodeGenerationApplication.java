package com.miachoose.aizerocodegeneration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.miachoose.aizerocodegeneration.mapper")
public class AiZeroCodeGenerationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiZeroCodeGenerationApplication.class, args);
    }

}
