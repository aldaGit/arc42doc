package org.arc42.dokumentation.control.logic;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.arc42.dokumentation.control.service.exception.GoogleBigQueryException;
import org.arc42.dokumentation.control.service.exception.InvalidLink;
import org.arc42.dokumentation.control.util.QueryBuilderFactory;
import org.arc42.dokumentation.model.dto.documentation.SoAnswersDTO;
import org.arc42.dokumentation.view.util.data.Credentials;

public class GoogleBigQueryController {

  private static GoogleBigQueryController instance;

  private final BigQuery bigQuery;
  private final Logger logger;

  private GoogleBigQueryController() throws IOException {
    String key = "key.json";
    Credentials.readEnvironment();
    this.bigQuery =
        BigQueryOptions.newBuilder()
            .setProjectId(Credentials.PROJECT_ID)
            .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(key)))
            .build()
            .getService();
    this.logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  }

  public static GoogleBigQueryController getInstance() throws IOException {
    if (instance == null) {
      instance = new GoogleBigQueryController();
    }
    return instance;
  }

  public List<SoAnswersDTO> getAnswersWithUserNameForRefLink(String refLink)
      throws InvalidLink, GoogleBigQueryException, InterruptedException {

    String query = QueryBuilderFactory.createGetAllAnswersAndUsersForQuestionQuery(refLink);
    TableResult result = runQueryJob(query);

    ArrayList<SoAnswersDTO> possibleAnswers = new ArrayList<>();
    for (FieldValueList row : result.iterateAll()) {
      SoAnswersDTO answer = new SoAnswersDTO();
      answer.setQuestionId(row.get("QuestionId").getStringValue());
      answer.setAnswerId(row.get("AnswerId").getStringValue());
      answer.setAnswerBody(row.get("AnswerBody").getStringValue());
      answer.setQuestionTitle(row.get("QuestionTitle").getStringValue());
      answer.setQuestionBody(row.get("QuestionBody").getStringValue());
      answer.setUserId(row.get("UserId").getStringValue());

      if (row.get("AcceptedAnswerId").getValue() == null) {
        answer.setAcceptedAnswerId(null);
      } else {
        answer.setAcceptedAnswerId(row.get("AcceptedAnswerId").getStringValue());
      }
      answer.setUserReputation(row.get("UserReputation").getStringValue());
      answer.setVoting(row.get("Voting").getStringValue());
      possibleAnswers.add(answer);
    }
    return possibleAnswers;
  }

  public String getDecisionByFileAndRepoName(String path, String repo_name)
      throws GoogleBigQueryException, InterruptedException {
    String query =
        QueryBuilderFactory.createQueryToGetGithubDecisionBodyByPathAndRepoName(path, repo_name);
    TableResult result = runQueryJob(query);
    for (FieldValueList row : result.iterateAll()) {
      return "Id: "
          + row.get("id").getStringValue()
          + " Content: "
          + row.get("content").getStringValue();
    }
    return null;
  }

  private TableResult runQueryJob(String query)
      throws GoogleBigQueryException, InterruptedException {
    QueryJobConfiguration queryConfig =
        QueryJobConfiguration.newBuilder(query).setUseLegacySql(false).build();

    JobId jobId = JobId.of(UUID.randomUUID().toString());
    Job queryJob = bigQuery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());
    queryJob = queryJob.waitFor();

    if (queryJob == null) {
      throw new GoogleBigQueryException("QueryJob: " + jobId + " is null");
    }
    if (queryJob.getStatus().getError() != null) {
      throw new GoogleBigQueryException("QueryJob: " + jobId + " has Errors");
    }

    TableResult result = queryJob.getQueryResults();
    this.logger.info("Result of QueryJob: " + jobId + " received");
    if (result == null) {
      this.logger.warning("Response of QueryJob is empty");
      return null;
    }
    return result;
  }
}
