package controller;

import dao.SaleDao;
import dao.UserDao;
import dto.SaleDTO;
import dto.UsersDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/controller/sale")
public class SaleController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("진입");
        int uuid = Integer.parseInt(req.getParameter("sale_id"));
        SaleDao dao = new SaleDao();
        SaleDTO n = dao.select(uuid);
        if(n != null) {
            req.setAttribute("sale",n);
            req.getRequestDispatcher(req.getContextPath() + "/index.jsp").forward(req,resp);
        } else {
            System.out.println("실패");
        }
    }
}
