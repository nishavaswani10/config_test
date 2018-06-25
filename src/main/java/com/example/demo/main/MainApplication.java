package com.example.demo.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

import com.example.demo.config.Config;

/**
 * @author Nisha.Vaswani
 *
 */

public class MainApplication {
	static Logger LOG = Logger.getLogger("DemoApplication.Class");

	public static String fileName;

	public static void main(String[] args) throws IOException {
		Properties prop = new Properties();
		Config config=new Config();
		InputStream inputFile = MainApplication.class.getClassLoader().getResourceAsStream("application.properties");
		prop.load(inputFile);
		Scanner scanner=new Scanner(System.in);
		String output = null;
		String overrides=prop.getProperty("value.overrides"); 
		Config.load(prop.getProperty("file.path"),overrides.split(","));
		System.out.println("Please enter the property you want to read ");
		output = displayProp(config, scanner);
	}

	static String displayProp(Config config, Scanner sc) {
		String result=null;
		while(sc.hasNext()) {
			String input =sc.nextLine(); 
			if(input.equals("exit")) {
				break;
			}
			 result= config.get(input);

			System.out.println("Output:"+result);
			System.out.println("Do you want to read more property,Please enter the property else type exit");

		}
		return result;
	}
}
