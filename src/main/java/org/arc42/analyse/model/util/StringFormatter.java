package org.arc42.analyse.model.util;

public class StringFormatter {

  public static String formatRuleName(String i) {
    String ruleName = i.substring(i.lastIndexOf(":") + 1);
    String[] ruleWordsArray = ruleName.split("(?<=.)(?=\\p{Lu})");
    String formattedRuleName = "";
    for (int j = 0; j < ruleWordsArray.length; j++) {
      if (j == ruleWordsArray.length - 1) formattedRuleName = formattedRuleName + ruleWordsArray[j];
      else formattedRuleName = formattedRuleName + ruleWordsArray[j] + " ";
    }
    return formattedRuleName;
  }
}
