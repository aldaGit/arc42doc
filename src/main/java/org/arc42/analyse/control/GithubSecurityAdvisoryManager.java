package org.arc42.analyse.control;

import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.stream.Collectors;
import org.arc42.analyse.control.service.Arc42AnalyseService;
import org.arc42.analyse.model.dto.GitHubSecurityAdvisoryDTO;

public class GithubSecurityAdvisoryManager {

  private final Arc42AnalyseService service;
  private final String securityAdvisoryUrl;

  public GithubSecurityAdvisoryManager(String securityAdvisoryUrl) {
    this.service = Arc42AnalyseService.getInstance();
    this.securityAdvisoryUrl = securityAdvisoryUrl;
  }

  public void readGitHubSecurityAdvisory() {
    this.readGitHubSecurityAdvisory(this.securityAdvisoryUrl);
  }

  private void readGitHubSecurityAdvisory(String url) {
    try {
      InputStream inputStream = getContentFromUrl(url);
      SyndFeedInput input = new SyndFeedInput();
      SyndFeed feed = input.build(new InputStreamReader(inputStream));
      List<SyndEntryImpl> entries = feed.getEntries();
      List<GitHubSecurityAdvisoryDTO> feeds =
          entries.stream()
              .filter(entry -> checkCategories((List<SyndCategoryImpl>) entry.getCategories()))
              .map(
                  entry ->
                      new GitHubSecurityAdvisoryDTO(
                          entry.getUri(),
                          entry.getPublishedDate(),
                          entry.getUpdatedDate(),
                          entry.getTitle(),
                          "MAVEN",
                          ((SyndContentImpl) entry.getContents().get(0)).getValue()))
              .collect(Collectors.toList());
      if (!feeds.isEmpty()) {
        this.service.deleteAllGitHubSecurityAdvisory();
      }
      feeds.forEach(this.service::saveGitHubSecurityAdvisory);
    } catch (FeedException e) {
      e.printStackTrace();
    }
  }

  private InputStream getContentFromUrl(String url) {
    InputStream inputStream = null;
    try {
      URLConnection connection = new URL(url).openConnection();
      connection.setRequestProperty("Accept", "application/atom+xml");
      inputStream = connection.getInputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return inputStream;
  }

  private Boolean checkCategories(List<SyndCategoryImpl> categories) {
    for (SyndCategoryImpl category : categories) {
      if (category.getName().equalsIgnoreCase("MAVEN")) {
        return true;
      }
    }
    return false;
  }
}
