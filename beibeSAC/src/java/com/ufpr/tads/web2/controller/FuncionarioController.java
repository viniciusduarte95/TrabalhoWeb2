package com.ufpr.tads.web2.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.ufpr.tads.web2.beans.AtendimentoBean;
import com.ufpr.tads.web2.beans.FuncionarioBean;
import com.ufpr.tads.web2.beans.LoginBean;
import com.ufpr.tads.web2.beans.SituacaoBean;
import com.ufpr.tads.web2.facade.AtendimentoException;
import com.ufpr.tads.web2.facade.AtendimentoFacade;
import com.ufpr.tads.web2.facade.FuncionarioException;
import com.ufpr.tads.web2.facade.FuncionarioFacade;
import com.ufpr.tads.web2.facade.SituacaoException;
import com.ufpr.tads.web2.facade.SituacaoFacade;

@WebServlet(name = "FuncionarioController", urlPatterns = { "/FuncionarioController" })
public class FuncionarioController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            HttpSession session = request.getSession();
            request.setCharacterEncoding("UTF-8");
            LoginBean logado = (LoginBean) session.getAttribute("logado");
            String action = request.getParameter("action");
            ServletContext sc = request.getServletContext();

            if (logado.getNome() != null) {
                if (action == null || action.equals("portal")) {
                    try {
                        
                        // Apresenta a pagina inicial do funcionario (só mostra chamados sem solução)
                        FuncionarioBean funcionario = FuncionarioFacade.retornaFuncionario(logado.getId());
                        SituacaoBean emAberto = SituacaoFacade.retornaSituacao(1);
                        List<AtendimentoBean> listaAtendimentosAbertos = AtendimentoFacade.getListaPorSituacao(emAberto);
                        if (listaAtendimentosAbertos.size() > 0) {
                            Collections.sort(listaAtendimentosAbertos, (AtendimentoBean a1, AtendimentoBean a2) -> a1
                                    .getDataHoraInicio().compareTo(a2.getDataHoraInicio()));
                            request.setAttribute("listaAtendimentosAbertos", listaAtendimentosAbertos);
                        }
                        request.setAttribute("funcionario", funcionario);

                        RequestDispatcher rd = sc.getRequestDispatcher("/funcionario/portalFuncionario.jsp");
                        rd.forward(request, response);
                    } catch (FuncionarioException | SituacaoException | AtendimentoException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                } else if (action.equals("todosAtendimentos")) {
                    try {
                        
                        // Apresenta tela com todos os atendimentos realizados pelo usuário (fechado ou aberto)
                        FuncionarioBean funcionario = FuncionarioFacade.retornaFuncionario(logado.getId());
                        List<AtendimentoBean> listaAtendimentos = AtendimentoFacade.getLista();
                        if (listaAtendimentos.size() > 0) {
                            Collections.sort(listaAtendimentos, (AtendimentoBean a1, AtendimentoBean a2) -> a1
                                    .getDataHoraInicio().compareTo(a2.getDataHoraInicio()));
                            request.setAttribute("listaAtendimentos", listaAtendimentos);
                        }
                        request.setAttribute("funcionario", funcionario);

                        RequestDispatcher rd = sc.getRequestDispatcher("/funcionario/todosAtendimentos.jsp");
                        rd.forward(request, response);
                    } catch (FuncionarioException | AtendimentoException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                } else if (action.equals("formResolverAtendimento")) {
                    try {
                        // Apresenta do formulário para resolver o ticket
                        String sId = request.getParameter("idAtendimento");
                        int idAtendimento = Integer.parseInt(sId);
                        AtendimentoBean atendimento = AtendimentoFacade.retornaAtendimento(idAtendimento);

                        RequestDispatcher rd = sc.getRequestDispatcher("/funcionario/resolucaoAtendimento.jsp");
                        request.setAttribute("atendimento", atendimento);
                        rd.forward(request, response);
                    } catch (AtendimentoException | NumberFormatException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                } else if (action.equals("resolverAtendimento")) {
                    try {
                        // muda o status do ticket para resolvido (altera atendimento)
                        AtendimentoBean atendimento = AtendimentoFacade
                                .retornaAtendimento(Integer.parseInt(request.getParameter("idAtendimento")));
                        FuncionarioBean funcionario = FuncionarioFacade.retornaFuncionario(logado.getId());

                        atendimento.setFuncionario(funcionario);
                        atendimento.setSolucao(request.getParameter("solucao"));
                        atendimento.setDataHoraFim(Calendar.getInstance());

                        boolean modificou = AtendimentoFacade.modificaAtendimento(atendimento);
                        if (modificou) {
                            response.sendRedirect(request.getContextPath() + "/FuncionarioController?action=portal");
                        } else {
                            request.setAttribute("msg",
                                    "Erro ao modificar atendimento de id: " + atendimento.getIdAtendimento());
                            RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                            rd.forward(request, response);
                        }
                    } catch (AtendimentoException | NumberFormatException | FuncionarioException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                }
            } else {
                RequestDispatcher rd = sc.getRequestDispatcher("/index.jsp");
                request.setAttribute("msg", "Usuário deve se autentificar para acessar o sistema");
                rd.forward(request, response);
            }
        }
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

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
