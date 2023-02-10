package edu.yjzxc.universeimserver;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(value = "edu.yjzxc.universeimserver.mapper")
@Slf4j
public class UniverseImServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniverseImServerApplication.class, args);
    }

}
