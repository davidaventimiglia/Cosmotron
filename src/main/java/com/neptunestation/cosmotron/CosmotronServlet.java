package com.neptunestation.cosmotron;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CosmotronServlet extends HttpServlet {
    @Override public void doGet (HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        getServletContext().getRequestDispatcher("/index.jsp").forward(req, res);}}
