package edu.yjzxc.universeimserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(value = "edu.yjzxc.universeimserver.mapper")
public class UniverseImServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniverseImServerApplication.class, args);
    }

}
