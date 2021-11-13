package web.commands;

import business.entities.User;
import business.services.UserFacade;
import business.exceptions.UserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCommand extends CommandUnprotectedPage {
    private UserFacade userFacade;

    public LoginCommand(String pageToShow) {
        super(pageToShow);
        userFacade = new UserFacade(database);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws UserException {
        String page = "";
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            User user = userFacade.login(email, password);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            session.setAttribute("email", email);

            if (user.getRole().equals("employee")) {
                page = "employeepage";
            } else {
                page = "index";
            }
            return REDIRECT_INDICATOR + page;
        } catch (UserException ex) {
            request.setAttribute("error", ex.getMessage());
            return "loginpage";
        }
    }

}
