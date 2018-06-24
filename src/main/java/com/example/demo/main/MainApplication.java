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
		String overrides;

		InputStream inputFile = MainApplication.class.getClassLoader().getResourceAsStream("application.properties");
		prop.load(inputFile);
		Scanner scanner=new Scanner(System.in);
		overrides=prop.getProperty("value.overrides");
		Config.load(prop.getProperty("file.path"),overrides.split(","));
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the property you want to read ");

		while(sc.hasNext()) {
			String input = sc.next();

			if(input.equals("exit")) {
				break;
			}
			String output=config.get(input);
			System.out.println(output);
			System.out.println("Do you want to read more property,Please enter the property else type exit");

		}
	}



}
