package com.example.demo.main;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.config.Config;

/**
 * @author Nisha.Vaswani
 *
 */
@SpringBootApplication
public class MainApplication {
	static Logger LOG = Logger.getLogger("DemoApplication.Class");
	
	public static String overrides;
	
	public static String fileName;
	
	public static void main(String[] args) throws FileNotFoundException {
		
		SpringApplication.run(MainApplication.class, args);
		Config config=new Config();
		Scanner scanner=new Scanner(System.in);
		System.out.println("Please enter the property you want to read ");
		String input=scanner.next();
		Config.load(fileName,overrides.split(","));
		
		String output=config.get(input);
		LOG.info("Output: "+output);
	}
	
	
	@Value("${value.overrides}")
    public void setOverride(String overrideValue) {
      overrides = overrideValue;
    }
	
	@Value("${file.path}")
    public void setFilePath(String filePath) {
      fileName = filePath;
    }
}
