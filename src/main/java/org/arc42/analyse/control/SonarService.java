package org.arc42.analyse.control;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Objects;
import okhttp3.*;
import org.arc42.analyse.model.dto.ComponentList;
import org.arc42.analyse.model.dto.sonar.*;
import org.arc42.dokumentation.model.dao.arc42documentation.SonarSettingsDAO;

public class SonarService {
  private static SonarService instance;
  SonarSettingsDTO settings;

  private static final String ENERGY_SMELLS = "hbrs:LongMethod, hbrs:LinkedList";

  private static final String AUTHORIZATION = "Authorization";
  private static final String BEARER = "Bearer ";

  private static final String QUALITY_PROFILE = "Energy Smell Analyse";

  public static SonarService getInstance() {
    if (instance == null) {
      instance = new SonarService();
    }
    return instance;
  }

  public IssueList getIssues() throws IOException {
    getSettings();
    if (settings == null) {
      return null;
    } else {
      HttpUrl.Builder urlBuilder =
          Objects.requireNonNull(HttpUrl.parse(formatUrl(settings.getUrl()) + "/api/issues/search"))
              .newBuilder();
      urlBuilder.addQueryParameter("components", settings.getComponent());
      urlBuilder.addQueryParameter("resolved", "false");
      urlBuilder.addQueryParameter("rules", ENERGY_SMELLS);
      String url = urlBuilder.build().toString();
      OkHttpClient client = new OkHttpClient().newBuilder().build();

      Request request =
          new Request.Builder()
              .url(url)
              .addHeader(AUTHORIZATION, BEARER + settings.getToken())
              .build();
      Response response = client.newCall(request).execute();
      return new Gson().fromJson(response.body().string(), IssueList.class);
    }
  }

  public boolean checkSettings(SonarSettingsDTO settings) throws IOException {
    HttpUrl.Builder urlBuilder =
        Objects.requireNonNull(
                HttpUrl.parse(formatUrl(settings.getUrl()) + "/api/components/search"))
            .newBuilder();
    urlBuilder.addQueryParameter("qualifiers", "TRK");
    urlBuilder.addQueryParameter("q", settings.getComponent());
    OkHttpClient client = new OkHttpClient().newBuilder().build();

    Request request =
        new Request.Builder()
            .url(urlBuilder.build().toString())
            .addHeader(AUTHORIZATION, BEARER + settings.getToken())
            .build();
    Response response = client.newCall(request).execute();
    Gson gson = new Gson();
    ComponentList list = gson.fromJson(response.body().string(), ComponentList.class);
    return list.getComponents().size() == 1
        && list.getComponents().get(0).getKey().equals(settings.getComponent());
  }

  public void getSettings() {
    SonarSettingsDAO sonarSettingsDAO = SonarSettingsDAO.getInstance();
    this.settings = sonarSettingsDAO.getSettings();
  }

  public String formatUrl(String url) {
    if (url.endsWith("/")) {
      return url.substring(0, url.length() - 1);
    } else return url;
  }

  public int bulkChangeIssues(String transition, String keys) throws IOException {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    RequestBody body =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("issues", keys)
            .build();
    Request request =
        new Request.Builder()
            .url(
                formatUrl(settings.getUrl())
                    + "/api/issues/bulk_change?do_transition="
                    + transition)
            .method("POST", body)
            .addHeader(AUTHORIZATION, BEARER + settings.getToken())
            .build();
    Response response = client.newCall(request).execute();
    return response.code();
  }

  public RuleList getRules() throws IOException {
    getSettings();
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    Request request =
        new Request.Builder()
            .url(
                formatUrl(settings.getUrl())
                    + "/api/rules/search?repositories=hbrs&f=repo, htmlDesc, severity")
            .addHeader(AUTHORIZATION, BEARER + settings.getToken())
            .build();
    Response response = client.newCall(request).execute();
    return new Gson().fromJson(response.body().string(), RuleList.class);
  }

  public RulesRoot getRuleDetails(String key) throws IOException {
    if (settings == null) getSettings();
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    Request request =
        new Request.Builder()
            .url(formatUrl(settings.getUrl()) + "/api/rules/show?actives=true&key=" + key)
            .addHeader(AUTHORIZATION, BEARER + settings.getToken())
            .build();
    Response response = client.newCall(request).execute();
    return new Gson().fromJson(response.body().string(), RulesRoot.class);
  }

  public int changeParameterValue(String ruleKey, String paramKey, int value) throws IOException {
    String qualityProfile = getQualityProfileKey();
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    RequestBody body = RequestBody.create("", null);

    HttpUrl.Builder urlBuilder =
        Objects.requireNonNull(
                HttpUrl.parse(formatUrl(settings.getUrl()) + "/api/qualityprofiles/activate_rule"))
            .newBuilder();
    urlBuilder.addQueryParameter("key", qualityProfile);
    urlBuilder.addQueryParameter("params", paramKey + "=" + value);
    urlBuilder.addQueryParameter("rule", ruleKey);

    Request request =
        new Request.Builder()
            .url(urlBuilder.build().toString())
            .method("POST", body)
            .addHeader(AUTHORIZATION, BEARER + settings.getToken())
            .build();
    Response response = client.newCall(request).execute();
    return response.code();
  }

  public String getQualityProfileKey() throws IOException {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    Request request =
        new Request.Builder()
            .url(
                formatUrl(settings.getUrl())
                    + "/api/qualityprofiles/search?qualityProfile="
                    + QUALITY_PROFILE)
            .addHeader(AUTHORIZATION, BEARER + settings.getToken())
            .build();
    Response response = client.newCall(request).execute();
    return new Gson()
        .fromJson(response.body().string(), Profiles.class)
        .getProfiles()
        .get(0)
        .getKey();
  }
}
