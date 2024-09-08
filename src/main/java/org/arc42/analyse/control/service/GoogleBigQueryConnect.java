package org.arc42.analyse.control.service;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.*;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;
import org.arc42.dokumentation.view.util.data.Credentials;

public class GoogleBigQueryConnect {

  private static GoogleBigQueryConnect instance;
  private final BigQuery bigQuery;

  private GoogleBigQueryConnect() throws IOException {
    Credentials.readEnvironment();
    this.bigQuery =
        BigQueryOptions.newBuilder()
            .setProjectId(Credentials.PROJECT_ID)
            .setCredentials(
                ServiceAccountCredentials.fromStream(getClass().getResourceAsStream("key.json")))
            .build()
            .getService();
  }

  public static GoogleBigQueryConnect getInstance() throws IOException {
    if (instance == null) {
      instance = new GoogleBigQueryConnect();
    }
    return instance;
  }

  public String getAcceptedAnswerForQuestionId(String questionId) throws InterruptedException {

    String query =
        "SELECT q.accepted_answer_id AS AcceptedAnswerId"
            + System.lineSeparator()
            + " FROM `bigquery-public-data.stackoverflow.posts_questions` q"
            + System.lineSeparator()
            + " WHERE q.id = "
            + questionId;

    TableResult result = runQueryJob(query);
    for (FieldValueList row : result.iterateAll()) {

      if (row.get("AcceptedAnswerId").getValue() == null) {
        return null;
      } else {
        return row.get("AcceptedAnswerId").getStringValue();
      }
    }
    return null;
  }

  private TableResult runQueryJob(String query) throws InterruptedException {
    QueryJobConfiguration queryConfig =
        QueryJobConfiguration.newBuilder(query).setUseLegacySql(false).build();

    JobId jobId = JobId.of(UUID.randomUUID().toString());
    Job queryJob = bigQuery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());
    queryJob = queryJob.waitFor();

    TableResult result = queryJob.getQueryResults();
    Logger.getAnonymousLogger().info("Result of QueryJob: " + jobId + " received");
    if (result == null) {
      Logger.getAnonymousLogger().info("Response of QueryJob is empty");
      return null;
    }
    return result;
  }
}
