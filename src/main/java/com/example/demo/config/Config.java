package com.example.demo.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;

import com.example.demo.utility.ConfigStatus;
import com.example.demo.utility.Constants;

/**
 * @author Nisha.Vaswani
 *
 */

public class Config {
	@Value("${value.overrides}")
	private   String OVERRIDE;
	
	Logger LOG = Logger.getLogger(Config.class.getName());

	private static Config config;
	
	private static Set<String> overrideProperties;
	
	private static HashMap<String, LinkedHashMap<String, Object>> configHashMap;
	
	private static ConfigStatus status;
	
	public Config() {

	}

	public static void load(String filePath, String[] overrides) throws FileNotFoundException {
		config = getConfigInstance();
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException("config file does not exist" + filePath);
		}
		ConfigBuilder builder = new ConfigBuilder(file, overrides);
		builder.parseFile();
	}
 
	
	static Config getConfigInstance() {
		if (config == null) { 
			config = new Config();
			overrideProperties = new HashSet<String>();
			configHashMap = new HashMap<String, LinkedHashMap<String, Object>>();
			status = ConfigStatus.NOT_STARTED;
		}

		return config;

	}

	
	public static String get(String key) {

		/*
		 * if key does not contains "." then it means that the request is for
		 * the values of a group. so we will simply query configHashMap for that
		 * key and will return the hashmap in the form of string.
		 */
		if (!key.contains(".")) {

			if (configHashMap.containsKey(key)) {
				LinkedHashMap<String, Object> groupHashMap = new LinkedHashMap<String, Object>();

				for (Entry<String, Object> keyValPair : configHashMap.get(key).entrySet()) {
					String currentKey = keyValPair.getKey();
					if (currentKey.contains(Constants.HYPHEN
							+ Constants.OVERRIDE_ATTRIBUTE)) {
						groupHashMap.put(currentKey.split(Constants.HYPHEN)[0],
								keyValPair.getValue());
					} else if(!groupHashMap.containsKey(currentKey)){
						groupHashMap.put(currentKey, keyValPair.getValue());

					}

				}
				return groupHashMap.toString();
			}

		} else {
			/*
			 * if key contains a "." it means that request is for a speicific
			 * setting of a group. so we will parse the group and the setting
			 * and will then query the hashmap inside the hash map and will
			 * retrieve the requested key(if it exists).
			 */
			String[] tokens = key.split("\\.");
			String groupName = tokens[0];
			String settingName = tokens[1];

			if (configHashMap.containsKey(groupName)) {

				if (configHashMap.get(groupName).containsKey(
						settingName + Constants.HYPHEN
								+ Constants.OVERRIDE_ATTRIBUTE)) {

					return configHashMap
							.get(groupName)
							.get(settingName + Constants.HYPHEN
									+ Constants.OVERRIDE_ATTRIBUTE).toString();
				} else if (configHashMap.get(groupName)
						.containsKey(settingName)) {

					return configHashMap.get(groupName).get(settingName)
							.toString();
				}

			}

		}
		return "None";

	}

	void updateConfigStatus(ConfigStatus configStatus) {
		status = configStatus;
	}

	public static ConfigStatus getConfigStatus() {
		return status;
	}

	void addOverrideProperties(List<String> overrideProperties) {
		this.overrideProperties.addAll(overrideProperties);
	}

	Set<String> getOverrideProperties() {
		return overrideProperties;
	}

	HashMap<String, LinkedHashMap<String, Object>> getConfigHashMap() {
		return configHashMap;
	}

	void addKeyValueInConfigHashMap(String hashMapKey,
			LinkedHashMap<String, Object> value) {
		configHashMap.put(hashMapKey, value);

	}

}
