package org.arc42.analyse.control.service;

import org.arc42.analyse.control.GithubSecurityAdvisoryManager;

public class ReadSecurityAdvisoryService {

  public void execute(int arcId) {
    GithubSecurityAdvisoryManager manager =
        new GithubSecurityAdvisoryManager("https://github.com/security-advisories");
    manager.readGitHubSecurityAdvisory();
    System.out.println("############### CheckSecurityAdvisoryDelegae  ################## " + arcId);
  }
}
