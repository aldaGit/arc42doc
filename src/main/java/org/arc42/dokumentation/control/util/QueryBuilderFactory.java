package org.arc42.dokumentation.control.util;

import org.arc42.dokumentation.control.service.exception.InvalidLink;
import org.arc42.dokumentation.view.util.methods.StaticUtils;

public class QueryBuilderFactory {

  public static String createGetAllAnswersAndUsersForQuestionQuery(String refLink)
      throws InvalidLink {
    String QUESTION_ID = StaticUtils.getQuestionIdInRefLink(refLink);
    return "SELECT q.id as QuestionId, a.id AS AnswerId, a.body AS AnswerBody, q.accepted_answer_id"
        + " AS AcceptedAnswerId, u.reputation AS UserReputation, a.score AS Voting, u.id AS"
        + " UserId, q.body AS QuestionBody, q.title AS QuestionTitle, u.display_name as"
        + " userName\n"
        + "FROM `bigquery-public-data.stackoverflow.posts_answers` a\n"
        + "JOIN `bigquery-public-data.stackoverflow.users` u\n"
        + "ON u.id = a.owner_user_id\n"
        + "JOIN `bigquery-public-data.stackoverflow.posts_questions` q\n"
        + "ON q.id = "
        + QUESTION_ID
        + "\n"
        + "WHERE a.parent_id = "
        + QUESTION_ID;
  }

  public static String createQueryToGetGithubDecisionBodyByPathAndRepoName(
      String path, String repo_name) {
    return "select id, content from `bigquery-public-data.github_repos.contents` "
        + "where id = ( select id from `bigquery-public-data.github_repos.files` "
        + "where path LIKE '%adr/%"
        + path
        + "' and repo_name='"
        + repo_name
        + "')";
  }
}
