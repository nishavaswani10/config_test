package com.example.demo.config;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.demo.config.Config;
import com.example.demo.utility.ConfigStatus;

public class ConfigTest {

	@BeforeClass
	public static void  setUp() throws FileNotFoundException, InterruptedException {
		Config.load("test-data/config", new String[] { "ubuntu", "production" });
		while (!Config.getConfigStatus().equals(ConfigStatus.COMPLETE)) {
			if (Config.getConfigStatus().equals(ConfigStatus.FAILED)) {
				throw new RuntimeException("Exception while loading the config file.");
			} 
		}
	}

	@Test
	public void when_ftp_group_name_key_should_return_correct_value() {
		Assert.assertEquals(Config.get("ftp.name"),"“hello there, ftp uploading”");
	}

	@Test
	public void when_common_group_path_key_should_return_correct_value() {
		Assert.assertEquals(Config.get("common.path"),"/srv/var/tmp/");
	}
	
	@Test
	public void when_ftp_group_path_key_should_return_correct_value() {
		Assert.assertEquals(Config.get("ftp.path"),"/etc/var/uploads");
	}

	@Test
	public void when_http_group_params_key_should_return_correct_value() {
		Assert.assertEquals(Config.get("http.params"), "[array, of, values]");
	}

	@Test
	public void when_ftp_group_lastname_key_should_return_None() {
		Assert.assertEquals(Config.get("ftp.lastname"),"None");
	}

	@Test
	public void when_ftp_group_enabled_key_should_return_false() {
		Assert.assertEquals(Config.get("ftp.enabled"), "false");
	}

	@Test
	public void when_ftp_group_should_return_all_values_in_the_group() {
		LinkedHashMap<String, Object> temp = new LinkedHashMap<String, Object>();
		temp.put("name", "“hello there, ftp uploading”");
		temp.put("path", "/etc/var/uploads");
		temp.put("enabled", false);
		Assert.assertEquals(Config.get("ftp"), temp.toString());
	} 
	
	@Test(expected=FileNotFoundException.class)
	public void when_file_does_not_exists_should_return_exception() throws FileNotFoundException
	{
		Config.load("test-data/config1", new String[] { "ubuntu", "production" });	
	}

} 
