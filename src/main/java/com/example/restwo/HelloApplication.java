package com.example.restwo;

import com.example.restwo.controller.Controller;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.io.File;
import java.io.IOException;

@ApplicationPath("/api")
public class HelloApplication extends Application {


    public static File file = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "ASD123");
    static {
        try {

            String line = "\n\n---------------------------------------\n\n";
            System.out.println(line + "Fetching..." + line);

            if(! file.exists()) {
                Controller.getJson();
            }

            System.out.println(line + "Done..." + line);

        } catch (IOException e) {
            System.out.print("");
        }
    }

}

