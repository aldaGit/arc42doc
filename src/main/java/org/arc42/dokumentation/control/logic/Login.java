package org.arc42.dokumentation.control.logic;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.arc42.dokumentation.model.dao.general.UserDAO;
import org.arc42.dokumentation.model.dto.general.DbUserDTO;
import org.arc42.dokumentation.model.dto.general.FeLoginDTO;
import org.arc42.dokumentation.view.util.data.Roles;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Login {

  public static void login(FeLoginDTO logindto) {
    String useremail = logindto.getUsername();
    String userpassword = logindto.getPassword();
    DbUserDTO userdto = UserDAO.getInstance().getUser(useremail, userpassword);
    String username = userdto.getUsername();
    VaadinSession.getCurrent().setAttribute("username", username);
    UI.getCurrent().getSession().setAttribute(Roles.CURRENTUSER, username);
    System.out.println("User " + username + " logged in");
    UI.getCurrent().navigate("arc42View");
  }

  public static void logout() {
    UI.getCurrent().getSession().setAttribute(Roles.CURRENTUSER, null);
    UI.getCurrent().navigate("login");
  }

  public static boolean userExist(FeLoginDTO logindto) {
    return UserDAO.getInstance().existUser(logindto.getUsername());
  }

  public static void register(FeLoginDTO logindto) {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String hashedPassword = encoder.encode(logindto.getPassword());
    UserDAO.getInstance().setUser(logindto.getUsername(), hashedPassword);
  }
}