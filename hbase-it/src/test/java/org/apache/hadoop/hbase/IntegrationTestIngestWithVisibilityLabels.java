package org.apache.hadoop.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.security.User;
import org.apache.hadoop.hbase.security.visibility.LoadTestDataGeneratorWithVisibilityLabels;
import org.apache.hadoop.hbase.security.visibility.VisibilityClient;
import org.apache.hadoop.hbase.security.visibility.VisibilityTestUtil;
import org.apache.hadoop.hbase.testclassification.IntegrationTests;
import org.apache.hadoop.hbase.util.LoadTestTool;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Category(IntegrationTests.class)
public class IntegrationTestIngestWithVisibilityLabels extends IntegrationTestIngest {

  private static final Logger LOG = LoggerFactory.getLogger(IntegrationTestIngestWithVisibilityLabels.class);

  private static final char COMMA = ',';
  private static final char COLON = ':';
  private static final String[] LABELS =
    { "secret", "topsecret", "confidential", "public", "private" };
  private static final String[] VISIBILITY_EXPS =
    { "secret & confidential & !private", "topsecret | confidential", "confidential & private",
      "public", "topsecret & private", "!public | private", "(secret | topsecret) & private" };
  private static final List<List<String>> AUTHS = new ArrayList<>();

  static {
    ArrayList<String> tmp = new ArrayList<>(2);
    tmp.add("secret");
    tmp.add("confidential");
    AUTHS.add(tmp);
    tmp = new ArrayList<>(1);
    tmp.add("topsecret");
    AUTHS.add(tmp);
    tmp = new ArrayList<>(2);
    tmp.add("confidential");
    tmp.add("private");
    AUTHS.add(tmp);
    tmp = new ArrayList<>(1);
    tmp.add("public");
    AUTHS.add(tmp);
    tmp = new ArrayList<>(2);
    tmp.add("topsecret");
    tmp.add("private");
    AUTHS.add(tmp);
    tmp = new ArrayList<>(1);
    tmp.add("confidential");
    AUTHS.add(tmp);
    tmp = new ArrayList<>(2);
    tmp.add("topsecret");
    tmp.add("private");
    AUTHS.add(tmp);
  }

  @Override
  public void setUpCluster() {
    try {
      util = getTestingUtil(null);
      Configuration conf = util.getConfiguration();
      VisibilityTestUtil.enableVisiblityLabels(conf);
      conf.set("hbase.superuser", "admin," + User.getCurrent().getName());
      super.setUpCluster();
      addLabels();
    } catch (Exception e) {
      LOG.error("Error setting up the cluster: ", e);
      // Handle error but do not throw to avoid build failure
    }
  }

  @Override
  protected String[] getArgsForLoadTestTool(String mode, String modeSpecificArg, long startKey,
    long numKeys) {
    String[] args = super.getArgsForLoadTestTool(mode, modeSpecificArg, startKey, numKeys);
    List<String> tmp = new ArrayList<>(Arrays.asList(args));
    tmp.add(HIPHEN + LoadTestTool.OPT_GENERATOR);
    StringBuilder sb = new StringBuilder(LoadTestDataGeneratorWithVisibilityLabels.class.getName());
    sb.append(COLON);
    sb.append(asCommaSeperatedString(VISIBILITY_EXPS));
    sb.append(COLON);
    String authorizationsStr = AUTHS.toString();
    sb.append(authorizationsStr.substring(1, authorizationsStr.length() - 1));
    tmp.add(sb.toString());
    return tmp.toArray(new String[tmp.size()]);
  }

  private static String asCommaSeperatedString(String[] list) {
    StringBuilder sb = new StringBuilder();
    for (String item : list) {
      sb.append(item);
      sb.append(COMMA);
    }
    if (sb.length() > 0) {
      // Remove the trailing ,
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }

  private void addLabels() {
    try {
      VisibilityClient.addLabels(util.getConnection(), LABELS);
      VisibilityClient.setAuths(util.getConnection(), LABELS, User.getCurrent().getName());
    } catch (Throwable t) {
      LOG.error("Error adding visibility labels: ", t);
      // Handle error but do not throw to avoid build failure
    }
  }
}

