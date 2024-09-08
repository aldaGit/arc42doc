package org.arc42.dokumentation.model.dto.documentation;

public class SoAnswersDTO {

  private String questionId;
  private String answerId;
  private String answerBody;
  private String questionTitle;
  private String questionBody;
  private String acceptedAnswerId;
  private String userReputation;
  private String voting;
  private String userId;

  public String getQuestionId() {
    return questionId;
  }

  public void setQuestionId(String questionId) {
    this.questionId = questionId;
  }

  public String getAnswerId() {
    return answerId;
  }

  public void setAnswerId(String answerId) {
    this.answerId = answerId;
  }

  public String getAnswerBody() {
    return answerBody;
  }

  public void setAnswerBody(String answerBody) {

    this.answerBody = answerBody;
  }

  public String getQuestionTitle() {
    return questionTitle;
  }

  public void setQuestionTitle(String questionTitle) {
    this.questionTitle = questionTitle;
  }

  public String getQuestionBody() {
    return questionBody;
  }

  public void setQuestionBody(String questionBody) {
    this.questionBody = questionBody;
  }

  public String getAcceptedAnswerId() {
    return acceptedAnswerId;
  }

  public void setAcceptedAnswerId(String acceptedAnswerId) {
    this.acceptedAnswerId = acceptedAnswerId;
  }

  public String getUserReputation() {
    return userReputation;
  }

  public void setUserReputation(String userReputation) {
    this.userReputation = userReputation;
  }

  public String getVoting() {
    return voting;
  }

  public void setVoting(String voting) {
    this.voting = voting;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "SoAnswersDTO{"
        + "questionId='"
        + questionId
        + '\''
        + ", answerId='"
        + answerId
        + '\''
        + ", answerBody='"
        + answerBody
        + '\''
        + ", questionTitle='"
        + questionTitle
        + '\''
        + ", questionBody='"
        + questionBody
        + '\''
        + ", acceptedAnswerId='"
        + acceptedAnswerId
        + '\''
        + ", userReputation='"
        + userReputation
        + '\''
        + ", voting='"
        + voting
        + '\''
        + ", userId='"
        + userId
        + '\''
        + '}';
  }
}
