package org.arc42.analyse.model.util;

import org.neo4j.driver.Result;

public class ResultChecker {
  public static boolean resultNotEmpty(Result result) {
    return result != null && result.hasNext();
  }
}
