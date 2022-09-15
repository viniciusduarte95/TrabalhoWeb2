package com.ufpr.tads.web2.controller;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.ufpr.tads.web2.beans.CidadeBean;
import com.ufpr.tads.web2.beans.EstadoBean;
import com.ufpr.tads.web2.facade.CidadeException;
import com.ufpr.tads.web2.facade.CidadeFacade;
import com.ufpr.tads.web2.facade.EstadoException;
import com.ufpr.tads.web2.facade.EstadoFacade;

@WebServlet(name = "AJAXController", urlPatterns = { "/AJAXController" })
public class AJAXController extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Apresenta a lista de cidades com base no estado selecionado
        String idEstado = request.getParameter("idEstado");
        ServletContext sc = request.getServletContext();
        try {
            // Para retornar o estado é passado o id do estado selecionado para a facade
            EstadoBean estado = EstadoFacade.retornaEstado(Integer.parseInt(idEstado));
            // Pega as informações do estado acima e pesquisa as cidades vinculados a esse estado 
            List<CidadeBean> listaCidades = CidadeFacade.getLista(estado);
            // Resultado as cidades vinculadas ao estado
            String json = new Gson().toJson(listaCidades);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (EstadoException | CidadeException | NumberFormatException e) {
            request.setAttribute("msg", "ERRO: " + e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("erro.jsp");
            rd.forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
