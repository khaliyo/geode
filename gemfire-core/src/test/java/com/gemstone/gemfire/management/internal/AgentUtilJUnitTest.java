package com.gemstone.gemfire.management.internal;

import java.io.File;
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
import com.sun.tools.internal.ws.processor.util.DirectoryUtil;

@Category(UnitTest.class)
public class AgentUtilJUnitTest extends TestCase {

  private AgentUtil agentUtil;
  private String version;

  public void setUp() {
    version = getGemfireVersion();
    agentUtil = new AgentUtil(version);
  }

  @Test
  public void testRESTApiExists() {
    String gemFireWarLocation = agentUtil.getGemFireWebApiWarLocation();
    Assert.notNull(gemFireWarLocation, "GemFire REST API WAR File was not found");
  }

  /*
   * This test should be activated when pulse gets added to Geode
   */
  // @Test
  // public void testPulseWarExists() {
  // String gemFireWarLocation = agentUtil.getPulseWarLocation();
  // Assert.notNull(gemFireWarLocation, "Pulse WAR File was not found");
  // }

  private String getGemfireVersion() {
    String version = null;

    Properties prop = new Properties();
    InputStream inputStream = null;
    String pathPrefix = null;
    try {
      pathPrefix = calculatePathPrefixToProjectRoot("gemfire-assembly/");
      inputStream = new FileInputStream(pathPrefix + "gradle.properties");
    } catch (FileNotFoundException e1) {
      try {
        pathPrefix = calculatePathPrefixToProjectRoot("gemfire-core/");
        inputStream = new FileInputStream(pathPrefix + "gradle.properties");
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

  private String calculatePathPrefixToProjectRoot(String subDirectory) {
    String pathPrefix = "";

    String currentDirectoryPath = new File(".").getAbsolutePath();
    int gemfireCoreLocationIx = currentDirectoryPath.indexOf(subDirectory);
    if (gemfireCoreLocationIx < 0) {
      return pathPrefix;
    }

    String pathFromRoot = currentDirectoryPath.substring(gemfireCoreLocationIx);
    int segmentsCount = pathFromRoot.split("/").length - 1;

    for (int i = 0; i < segmentsCount; i++) {
      pathPrefix = pathPrefix + "../";
    }
    return pathPrefix;
  }
}
