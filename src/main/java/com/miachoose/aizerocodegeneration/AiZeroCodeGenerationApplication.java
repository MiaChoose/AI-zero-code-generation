package com.miachoose.aizerocodegeneration;

import dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {RedisEmbeddingStoreAutoConfiguration.class})
@MapperScan("com.miachoose.aizerocodegeneration.mapper")
public class AiZeroCodeGenerationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiZeroCodeGenerationApplication.class, args);
    }

}
