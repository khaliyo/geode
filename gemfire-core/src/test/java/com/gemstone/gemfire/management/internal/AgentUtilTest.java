package com.gemstone.gemfire.management.internal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.util.Assert;

import com.gemstone.junit.UnitTest;

@Category(UnitTest.class)
public class AgentUtilTest  extends TestCase {
	
	private AgentUtil agentUtil;
	
	public void setUp() {
		String version = getGemfireVersion();
		agentUtil = new AgentUtil(version);
	}
	
	@Test
	public void testRESTApiExists() {		
		String gemFireWarLocation = agentUtil.getGemFireWebApiWarLocation();
		Assert.notNull(gemFireWarLocation, "GemFire REST API WAR File was not found");
	}
		
	@Test
	public void testPulseWarExists() {
		String gemFireWarLocation = agentUtil.getPulseWarLocation();
		Assert.notNull(gemFireWarLocation, "Pulse WAR File was not found");
	}

	private String getGemfireVersion() {
		String version = null;
		String GemFirePath = new AgentUtil(null).getGemFireHome();
		
		Properties prop = new Properties();         
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(GemFirePath + "/../../../../" + "gradle.properties");
		} catch (FileNotFoundException e1) {
			try {
				inputStream = new FileInputStream("../" + "gradle.properties");
			} catch (FileNotFoundException e) {
			}
		}
		
		if (inputStream != null) {
			try {
				prop.load(inputStream);
				version = prop.getProperty("version");
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}
		return version;
	}
}
