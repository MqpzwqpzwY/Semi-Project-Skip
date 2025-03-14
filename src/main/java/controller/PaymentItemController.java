package controller;

import dao.PaymentItemDao;
import dto.PaymentItemDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/controller/items")
public class PaymentItemController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int category_id = Integer.parseInt(req.getParameter("category_id"));
        PaymentItemDao dao = new PaymentItemDao();
        List<PaymentItemDTO> items = dao.selectItem(category_id);

        if(items != null && !items.isEmpty()) {

            req.setAttribute("items", items);
            req.getRequestDispatcher("/index.jsp").forward(req, resp);  // getContextPath() 제거
        } else {
            System.out.println("실패");
        }
    }
}
