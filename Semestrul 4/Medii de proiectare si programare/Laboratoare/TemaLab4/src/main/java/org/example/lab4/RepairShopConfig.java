package org.example.lab4;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.example.lab4.repository.ComputerRepairRequestRepository;
import org.example.lab4.repository.ComputerRepairedFormRepository;
import org.example.lab4.repository.jdbc.ComputerRepairRequestJdbcRepository;
import org.example.lab4.repository.jdbc.ComputerRepairedFormJdbcRepository;
import org.example.lab4.services.ComputerRepairServices;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class RepairShopConfig {
    @Bean
    Properties getProps() {
        Properties props = new Properties();
        try {
            System.out.println("Current dir: " + new File(".").getAbsolutePath());
            props.load(new FileReader("D:\\Facultate\\Semestrul 4\\Medii de proiectare si programare\\Laboratoare\\Lab4\\src\\main\\resources\\bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }
        return props;
    }

    @Bean
    ComputerRepairRequestRepository requestsRepo(){
        return new ComputerRepairRequestJdbcRepository(getProps());
    }

    @Bean
    ComputerRepairedFormRepository formsRepo(){
        return new ComputerRepairedFormJdbcRepository(getProps());
    }

    @Bean
    ComputerRepairServices services(){
        return new ComputerRepairServices(requestsRepo(),formsRepo());
    }

}
