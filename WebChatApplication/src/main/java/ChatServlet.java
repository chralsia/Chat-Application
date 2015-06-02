import db.DAO;
import messaging.Message;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "ChatServlet")
public class ChatServlet extends HttpServlet {
    private DAO db;
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        String b, a = "";

        while ((b = reader.readLine()) != null)
            a += b;
        try {
            Message message = new Message(a);
            boolean res = db.addMessage(message);
            List<Message> list = db.getMessagesByUserName(message.getUser());
            int s = 0;
        } catch (Exception ex){
            a = "";
            //throw new ServletException(ex);
        }

    }

    /*@Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
}*/
    public void init(ServletConfig config) throws ServletException {
        int a =0;
        try {
            db = new DAO();
        } catch (Exception ex){
            throw new ServletException(ex);
        }
        log("INIT");
    }

}
