package com.example.demo.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import com.example.demo.utility.ConfigStatus;
import com.example.demo.utility.Constants;
import com.example.demo.utility.Utils;

/**
 * @author Nisha.Vaswani
 *
 */
public class ConfigBuilder {

	Logger LOG = Logger.getLogger(ConfigBuilder.class.getName());

	File file;
	List<String> overrides;

	public ConfigBuilder(File file, String[] overrides) {
		this.file = file;
		this.overrides = Arrays.asList(overrides);
	}

	
	public void parseFile() {
		Config config = Config.getConfigInstance();
		config.addOverrideProperties(overrides);

		// Setting the status of config parsing as IN_PROGRESS
		config.updateConfigStatus(ConfigStatus.IN_PROGRESS);
 
		LinkedHashMap<String, Object> map = null;
		try {
			Scanner scanner = new Scanner(file);
			String groupName = "";
			while (scanner.hasNextLine()) { 
				String line = scanner.nextLine().trim();

				// Check if the line is not null or empty
				if (line == null || line.isEmpty()) {
					continue;
				}

				// Check if config line starts with a comment
				if (line.startsWith(Constants.SEMI_COLON, 0)) {
					continue;
				}

				// If line contains a comment, we will trim that line till
				// comment portion
				if (line.contains(Constants.SEMI_COLON)) {
					line = line.split(Constants.SEMI_COLON)[0];
				}

				// Check if the line contains a group name
				if (line.startsWith(Constants.OPENING_BRACKET, 0)
						&& line.endsWith(Constants.CLOSING_BRACKET)) {

					/*
					 * if map is not null, then it means that this map was
					 * initialized and populated during a previous iteration. so
					 * we will put this map in the configHashMap.
					 */
					if (map != null) {
						config.addKeyValueInConfigHashMap(groupName, map);
					}

					groupName = line.replace(Constants.OPENING_BRACKET,
							Constants.EMPTY_STRING).replace(Constants.CLOSING_BRACKET,
							Constants.EMPTY_STRING);
					
					// reinitializing the configHashMap
					map = new LinkedHashMap<String, Object>();
					continue;
				}

				// if the line is not empty and group, then it should contain
				// "="
				String[] keyValuePair = line.split(Constants.EQUALS_TO);
				if (keyValuePair.length != 2) {
					config.updateConfigStatus(ConfigStatus.FAILED);
					throw new IllegalArgumentException("key value pair not found in config file for group: "	+ groupName);
				}

				String key = keyValuePair[0].trim();

				String overrideName = null;

				/*
				 * checking if the setting contains an override.
				 */
				if (key.indexOf(Constants.LESS_THAN) > -1) {
					key = key.replace(Constants.GREATER_THAN,Constants.EMPTY_STRING);
					String[] keyOverridePair = key.split(Constants.LESS_THAN);
					key = keyOverridePair[0].trim();
					overrideName = keyOverridePair[1].trim();
				}
				String hashMapKey = key;

				String value = keyValuePair[1].trim();
				Object hashMapVal = value;

				// Checking if the values are basically boolean values.
				if (value.equals("yes") || value.equals("1")
						|| value.equals("true")) {
					hashMapVal = true;
				} else if (value.equals("no") || value.equals("0")
						|| value.equals("false")) {
					hashMapVal = false;
				}

				// if the string doesnt start and end with double quote and it
				// contains
				// comma's , then we need to tokenize that string into an array.
				if (Utils.stringTokenize(value)) {
					String[] stringVals = value.split(",");
					hashMapVal = Arrays.asList(stringVals);
				}

				// if the override is null, then we will put this setting value
				// under default setting.
				if (overrideName == null) {
					map.put(hashMapKey, hashMapVal);

				} else if (config.getOverrideProperties()
						.contains(overrideName)) {
					
					/*
					 * If the override value is not null, then we will set the 
					 * setting with "override". this will denote that there is 
					 * an override set for this setting. Since the we are using
					 * a linked hashmap , values will get inserted in the order
					 * in which they were entered. so if the override is present
					 * in the list of overrides specified in the input, then we will
					 * replace that override with the new override(because the override
					 * mentioned in the last will get more priority).
					 */

					map.put(hashMapKey + Constants.HYPHEN
							+ Constants.OVERRIDE_ATTRIBUTE, hashMapVal);

				}
			}
			if (map != null) {
				config.addKeyValueInConfigHashMap(groupName, map);
			}
			
			config.updateConfigStatus(ConfigStatus.COMPLETE);
			scanner.close();
		} catch (FileNotFoundException e) {
			LOG.severe("Exception while loading confile file from : "
					+ file.getAbsolutePath() + " " + e);
			config.updateConfigStatus(ConfigStatus.FAILED);
		}

	}
}
