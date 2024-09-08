package org.arc42.dokumentation.view.util.methods;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.arc42.dokumentation.control.service.exception.InvalidLink;

public class StaticUtils {

  public static String getBadgeCategoryForClassNumber(String badgeClass) {
    return badgeClass.equals("1")
        ? "GoldBadge"
        : badgeClass.equals("2") ? "SilverBadge" : badgeClass.equals("3") ? "BronzeBadge" : null;
  }

  public static String getQuestionIdInRefLink(String refLink) throws InvalidLink {
    final String PRE_SO_LINK = "https://stackoverflow.com/questions/";
    final String ONLY_NUMBERS = "[0-9]+";

    String refLinkWithoutPre = refLink.replaceAll(PRE_SO_LINK, "");
    int indexOfNextSlash = refLinkWithoutPre.lastIndexOf("/");
    String soId = refLinkWithoutPre.substring(0, indexOfNextSlash);
    if (soId.matches(ONLY_NUMBERS)) {
      return soId;
    } else {
      throw new InvalidLink("No valid StackOverflow-RefLink!");
    }
  }

  public static String[] getRepoNameAndFileFromGithubLink(String githubLink) {
    String urlPrefix = "https://github.com/";
    int index = githubLink.lastIndexOf("/");
    String fileName = githubLink.substring(index + 1);
    String newUrl = githubLink.replaceFirst(urlPrefix, "");
    String[] split = newUrl.split("/");
    String repo_name = split[0] + "/" + split[1];

    return new String[] {repo_name, fileName};
  }

  public static String getContentFromUrl(String url) {

    StringBuilder textBuilder = new StringBuilder();
    URLConnection connection = null;
    try {
      connection = new URL(url).openConnection();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (connection == null) {
      return "";
    }
    connection.setRequestProperty("Accept", "text/plain");
    try (InputStream inputStream = connection.getInputStream();
        Reader reader =
            new BufferedReader(
                new InputStreamReader(
                    inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {

      int c;
      while ((c = reader.read()) != -1) {
        textBuilder.append((char) c);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return textBuilder.toString();
  }
}
