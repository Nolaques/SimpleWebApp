package com.company.webapp.servlet;


import com.company.webapp.beans.Product;
import com.company.webapp.utils.DBUtils;
import com.company.webapp.utils.MyUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/createProduct"})
public class CreateProductServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;

    public CreateProductServlet() {
        super();
    }

    // show page of creating product.

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/views/createProductView.jsp");
        dispatcher.forward(req,resp);
    }


    // When user enters product's info and presses Submit.
    // this method will invoke
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection conn = MyUtils.getStoredConnection(req);

        String code= (String)req.getParameter("code");
        String name= (String)req.getParameter("name");
        String priceStr= (String)req.getParameter("price");
        float price = 0;

        try {
            price = Float.parseFloat(priceStr);
        }catch (Exception e){

        }

        Product product = new Product(code, name, price);

        String errorString = null;

        // string is product's code [a-zA-Z_0-9]
        // has min 1 symbol.
        String regex = "\\w+";

        if (code == null || !code.matches(regex)){
            errorString = "Product Code invalid";
        }

        if (errorString == null){
            try {
                DBUtils.insertProduct(conn, product);
            }catch (SQLException e){
                e.printStackTrace();
                errorString = e.getMessage();
            }
        }

        // save info's request attribute before forward to views.
        req.setAttribute("errorString", errorString);
        req.setAttribute("product", product);

        // if there is error, forward to page 'edit'.
        if (errorString != null){
            RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/views/createProductView.jsp");
            dispatcher.forward(req,resp);
        }
        // if everything is good
        // Redirect to page with the product's list
        else{
            resp.sendRedirect(req.getContextPath() + "/productList");
        }
    }
}
