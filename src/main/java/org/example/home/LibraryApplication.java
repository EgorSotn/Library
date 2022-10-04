package org.example.home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) throws SQLException {
        ApplicationContext context = SpringApplication.run(LibraryApplication.class);
//        LibraryService libraryService = context.getBean(LibraryService.class);
//        libraryService.addBookOrReturnBool();
    }
}
