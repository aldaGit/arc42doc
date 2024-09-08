package org.arc42.analyse.model.util;

import org.arc42.dokumentation.view.util.methods.StaticUtils;

public class GithubDecisionParser {

  public static String getAcceptedAlternative(String githubReferenz) {
    String mdContent = StaticUtils.getContentFromUrl(githubReferenz);
    String[] contents = mdContent.split("(\\n\\s*)(#){1,2}(?!#)");
    for (String content : contents) {
      if (content.toLowerCase().contains("Decision Outcome".toLowerCase())) {
        String[] split = content.split("\\*");
        return split[split.length - 1];
      }
    }
    return null;
  }
}
