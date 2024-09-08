package org.arc42.dokumentation.model.user;

import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.general.UserDAO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.exceptions.NoSuchRecordException;

public class ProfilDAO extends ARC42DAOAbstract<ProfilDTO, String> {

  public static ProfilDAO instance;

  private ProfilDAO() {
    super();
  }

  public static ProfilDAO getInstance() {
    if (instance == null) {
      instance = new ProfilDAO();
    }
    return instance;
  }

  @Override
  public ProfilDTO save(ProfilDTO profile) {
    if (profile == null) {
      throw new IllegalArgumentException("RisikoDTO is not valid");
    }
    // check validity of fields
    if (profile.getUsername() == null || profile.getUsername().equals("")) {
      throw new IllegalArgumentException("Username is not valid");
    }
    if (profile.getEmail() == null || profile.getEmail().equals("")) {
      throw new IllegalArgumentException("Email is not valid");
    }
    if (profile.getFirstName() == null || profile.getFirstName().equals("")) {
      throw new IllegalArgumentException("First Name is not valid");
    }
    if (profile.getLastName() == null || profile.getLastName().equals("")) {
      throw new IllegalArgumentException("Last Name is not valid");
    }
    if (profile.getPhone() == null) {
      profile.setPhone("");
    }

    // Make sure the dto does not contain null as values and replace all null-values with empty
    // strings.

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          // CREATE(d:Developer {devname:$username, password:$password}) return d.name",
          transaction -> {
            transaction.run(
                "MERGE (p:Profil{username: $username, "
                    + "email: $email, firstName: $firstname, notiz: $notiz, lastName: $lastname})",
                parameters(
                    "username",
                    profile.getUsername(),
                    "email",
                    profile.getEmail(),
                    "notiz",
                    "DEBUG L52",
                    "firstname",
                    profile.getFirstName(),
                    "lastname",
                    profile.getLastName()));
            Result result =
                transaction.run(
                    "match (a:Profil {username: $username}), (b:Developer {devname:$username})"
                        + System.lineSeparator()
                        + "create (a)-[:hasDevloper]->(b)"
                        + " return b.devname, Id(b)",
                    parameters("username", profile.getUsername()));
            ProfilDTO uDti = null;
            if (result != null) {
              uDti = new ProfilDTO(profile.getUsername());
            }
            return uDti;
          });
    }
  }

  @Override
  public Boolean delete(ProfilDTO userDTO) {
    if (userDTO != null) {
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        session.writeTransaction(
            transaction -> {
              transaction.run(
                  "match(a:User)where Id(a)=$id" + System.lineSeparator() + "detach delete(a)",
                  parameters("id", userDTO.getId()));
              // org.neo4j.driver.exceptions.ClientException: Cannot delete node<234>, because it
              // still has relationships. To delete this node, you must first delete its
              // relationships.
              return null;
            });
      }
    }
    return true;
  }

  @Override
  public void update(ProfilDTO userDTO) {
    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "MATCH (a:Profil) where a.username = $username "
                        + System.lineSeparator()
                        + " SET a = {firstName: $firstname,"
                        + " username: $username,"
                        + " lastName: $lastname,"
                        + " phone: $phone,"
                        + " email: $email} return"
                        + " Id(a), a.username, a.email",
                    parameters(
                        "username",
                        userDTO.getUsername(),
                        "firstname",
                        userDTO.getFirstName(),
                        "lastname",
                        userDTO.getLastName(),
                        "phone",
                        userDTO.getPhone(),
                        "email",
                        userDTO.getEmail()));
            ProfilDTO dto = null;
            if (result != null) {
              try {
                Record r = result.single();
                dto = new ProfilDTO(userDTO.getUsername());
                dto.setUsername(r.get("a.username").asString());
                dto.setEmail(r.get("a.email").asString()); // Maybe return more?
                dto.setId(String.valueOf(r.get("Id(a)")));
              } catch (NoSuchRecordException e) {
                System.out.println("Error in update: " + e.getMessage());
              }
            }
            return dto;
          });
    }
  }

  @Override
  public List<ProfilDTO> findAll(String username) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findAll'");
  }

  @Override
  public ProfilDTO findById(String id) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {

      if (id != null && !id.equals("")) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Profil) where Id(a)=$id return a.email, a.username,"
                          + " a.firstName, a.lastName, a.phone, Id(a)",
                      parameters("id", Integer.parseInt(id)));
              if (result == null || !result.hasNext()) {
                System.out.println("No User found");
                return null;
              }
              Record r = result.next();
              ProfilDTO dto = new ProfilDTO(r.get("a.username").asString());
              dto.setEmail(r.get("a.email").asString());
              dto.setFirstName(r.get("a.firstName").asString());
              dto.setLastName(r.get("a.lastName").asString());
              dto.setPhone(r.get("a.phone").asString());
              dto.setId(String.valueOf(r.get("Id(a)")));
              return dto;
            });
      }
    }
    return null;
  }

  public ProfilDTO findByUsername(String username) {
    Credentials.readEnvironment();
    System.out.println(username);
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      if (username != null && !username.equals("")) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:Profil) where a.username=$username"
                          + System.lineSeparator()
                          + "return a.email, a.firstName, a.lastName, a.phone, Id(a)",
                      parameters("username", username));
              if (result == null) {
                System.out.println("No User found");
              }
              if (!result.hasNext()) {
                System.out.println("Empty Result when Querying for user.");

                return null;
              }
              Record r = result.peek();
              System.out.println(r);
              ProfilDTO dto = new ProfilDTO(username);
              dto.setEmail(r.get("a.email") == null ? "" : r.get("a.email").asString());
              dto.setFirstName(r.get("a.firstName") == null ? "" : r.get("a.firstName").asString());
              dto.setLastName(r.get("a.lastName") == null ? "" : r.get("a.lastName").asString());
              dto.setPhone(r.get("a.phone") == null ? "" : r.get("a.phone").asString());
              dto.setId(String.valueOf(r.get("Id(a)")));
              System.out.println("Returning From dao: " + dto);
              return dto;
            });
      }
    }
    return null;
  }

  public ProfilDTO createProfilForUserIfExists(String username) {
    System.out.println("Creating Profil for user" + username);
    UserDAO userDAO = UserDAO.getInstance();
    if (userDAO.existUser(username)) {
      ProfilDTO dto = new ProfilDTO(username);
      dto.setUsername(username);
      dto.setEmail("nichtgesetzt@problem.de");
      dto.setFirstName("Vorname");
      dto.setLastName("Nachname");
      dto.setPhone("Bitte eintragen");
      return save(dto);
    }
    return null;
  }

  public ProfilDTO findByUsernameNoErrorCorrection(String username) {
    // this method is for internal use only
    Credentials.readEnvironment();
    System.out.println("AHHHH explizite getter");
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      if (username != null && !username.equals("")) {
        session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:Profil) where a.username = $username"
                          + System.lineSeparator()
                          + "return a.email, a.firstName, a.lastName, a.phone, Id(a)",
                      parameters("username", username));
              if (result == null) {
                System.out.println("No User found");
              }
              if (!result.hasNext()) {
                System.out.println("ups");
              }
              Record r = result.peek();
              ProfilDTO dto = new ProfilDTO(username);
              dto.setEmail(r.get("a.email").asString());
              dto.setFirstName(r.get("a.firstName").asString());
              dto.setLastName(r.get("a.lastName").asString());
              dto.setPhone(r.get("a.phone").asString());
              dto.setId(String.valueOf(r.get("Id(a)")));
              return dto;
            });
      }
    }
    return null;
  }
}
