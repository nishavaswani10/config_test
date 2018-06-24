package com.example.demo.utility;

/**
 * @author Nisha.Vaswani
 *
 */

/**
 * Utils class to hold utility method used while loading the config file.
 *
 */
public class Utils {

	/**
	 * static method to find out if we need to tokenize the string while
	 * loading the config file.
	 * if the file starts and end with double quote then we dont need to
	 * tokenize the string
	 * otherwise if the file contains comma, then we need to tokenize the 
	 * string.
	 * @param content
	 * @return
	 */
	public static boolean stringTokenize(String content) {
		if (content.startsWith("“") && content.endsWith("”")) {
			return false;
		} else if (content.contains(Constants.COMMA)) {
			return true;
		}
		return false;
	}

}
