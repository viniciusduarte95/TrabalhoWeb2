package com.ufpr.tads.web2.controller;
import java.io.IOException;
import java.io.PrintWriter;
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
import com.ufpr.tads.web2.beans.ClienteBean;
import com.ufpr.tads.web2.beans.EstadoBean;
import com.ufpr.tads.web2.beans.FuncionarioBean;
import com.ufpr.tads.web2.beans.GerenteBean;
import com.ufpr.tads.web2.beans.LoginBean;
import com.ufpr.tads.web2.beans.SituacaoBean;
import com.ufpr.tads.web2.beans.TipoAtendimentoBean;
import com.ufpr.tads.web2.facade.AtendimentoException;
import com.ufpr.tads.web2.facade.AtendimentoFacade;
import com.ufpr.tads.web2.facade.ClienteException;
import com.ufpr.tads.web2.facade.ClienteFacade;
import com.ufpr.tads.web2.facade.EstadoException;
import com.ufpr.tads.web2.facade.EstadoFacade;
import com.ufpr.tads.web2.facade.Ferramentas;
import com.ufpr.tads.web2.facade.FerramentasException;
import com.ufpr.tads.web2.facade.FuncionarioException;
import com.ufpr.tads.web2.facade.FuncionarioFacade;
import com.ufpr.tads.web2.facade.GerenteException;
import com.ufpr.tads.web2.facade.GerenteFacade;
import com.ufpr.tads.web2.facade.SituacaoException;
import com.ufpr.tads.web2.facade.SituacaoFacade;
import com.ufpr.tads.web2.facade.TipoAtendimentoException;
import com.ufpr.tads.web2.facade.TipoAtendimentoFacade;

@WebServlet(name = "LoginController", urlPatterns = { "/LoginController" })
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            request.setCharacterEncoding("UTF-8");
            
            // Peg os login e senha passados
            String login = request.getParameter("login");
            String senha = request.getParameter("senha");
            String action = request.getParameter("action");

            String nomeCliente = null;
            String nomeFuncionario = null;
            String nomeGerente = null;

            ServletContext sc = request.getServletContext();
            
            if (action.equals("logar") && (login == null || senha == null)) {
                request.setAttribute("msg", "Campos Login e Senha são obrigatórios");
                RequestDispatcher rd = sc.getRequestDispatcher("/index.jsp");
                rd.forward(request, response);
            } else if (action.equals("autoCadastro")) {
                try {
                    List<EstadoBean> listaEstados = EstadoFacade.getLista();
                    request.setAttribute("listaEstados", listaEstados);
                    RequestDispatcher rd = sc.getRequestDispatcher("/cliente/autoCadastro.jsp");

                    rd.forward(request, response);
                } catch (EstadoException e) {
                    request.setAttribute("msg", "ERRO: " + e.getMessage());
                    RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                    rd.forward(request, response);
                }
            } else if (action.equals("logar")) {
                try {
                    ClienteBean cliente = ClienteFacade.logaCliente(login, senha);
                    nomeCliente = cliente.getPrimeiroNome();

                    if (nomeCliente != null) {
                        List<AtendimentoBean> listaAtendimentos = AtendimentoFacade.getListaPorCliente(cliente);
                        if (listaAtendimentos.size() > 0) {
                            Collections.sort(listaAtendimentos, (AtendimentoBean a1, AtendimentoBean a2) -> a2
                                    .getDataHoraInicio().compareTo(a1.getDataHoraInicio()));
                            request.setAttribute("listaAtendimentos", listaAtendimentos);
                        }
                        request.setAttribute("cliente", cliente);
                        LoginBean loginBean = new LoginBean(cliente.getIdCliente(), cliente.getPrimeiroNome());
                        HttpSession session = request.getSession();
                        session.setAttribute("logado", loginBean);

                        // Adicionar caminho para portal de ClienteBean
                        RequestDispatcher rd = sc.getRequestDispatcher("/cliente/portalCliente.jsp");
                        rd.forward(request, response);
                    }
                } catch (ClienteException | AtendimentoException e) {
                    request.setAttribute("msg", "ERRO: " + e.getMessage());
                    RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                    rd.forward(request, response);
                }

                try {
                    FuncionarioBean funcionario = FuncionarioFacade.logaFuncionario(login, senha);
                    nomeFuncionario = funcionario.getPrimeiroNome();

                    if (nomeFuncionario != null) {
                        SituacaoBean emAberto = SituacaoFacade.retornaSituacao(1);
                        List<AtendimentoBean> listaAtendimentosAbertos = AtendimentoFacade.getListaPorSituacao(emAberto);
                        if (listaAtendimentosAbertos.size() > 0) {
                            Collections.sort(listaAtendimentosAbertos, (AtendimentoBean a1, AtendimentoBean a2) -> a1
                                    .getDataHoraInicio().compareTo(a2.getDataHoraInicio()));
                            request.setAttribute("listaAtendimentosAbertos", listaAtendimentosAbertos);
                        }
                        request.setAttribute("funcionario", funcionario);
                        LoginBean loginBean = new LoginBean(funcionario.getIdFuncionario(),
                                funcionario.getPrimeiroNome());
                        HttpSession session = request.getSession();
                        session.setAttribute("logado", loginBean);

                        // Adicionar caminho para portal de FuncionarioBean
                        RequestDispatcher rd = sc.getRequestDispatcher("/funcionario/portalFuncionario.jsp");
                        rd.forward(request, response);
                    }
                } catch (FuncionarioException | SituacaoException | AtendimentoException e) {
                    request.setAttribute("msg", "ERRO: " + e.getMessage());
                    RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                    rd.forward(request, response);
                }

                try {
                    GerenteBean gerente = GerenteFacade.logaGerente(login, senha);
                    nomeGerente = gerente.getPrimeiroNome();

                    if (nomeGerente != null) {
                        int qtdAtendimentos = Ferramentas.qtdAtendimentos();
                        request.setAttribute("qtdAtendimentos", qtdAtendimentos);
                        int qtdAtendimentosAbertos = Ferramentas.qtdAtendimentosAbertos();
                        request.setAttribute("qtdAtendimentosAbertos", qtdAtendimentosAbertos);
                        float percentualAtendimentosAbertos = Ferramentas.calculaPercentual(qtdAtendimentosAbertos,
                                qtdAtendimentos);
                        request.setAttribute("percentualAtendimentosAbertos", percentualAtendimentosAbertos);

                        TipoAtendimentoBean reclamacao = TipoAtendimentoFacade.retornaTipoAtendimento(1);
                        request.setAttribute("reclamacao", reclamacao);
                        int qtdAtendimentosReclamacao = Ferramentas.qtdAtendimentosTipo(reclamacao);
                        request.setAttribute("qtdAtendimentosReclamacao", qtdAtendimentosReclamacao);
                        int qtdAtendimentosAbertosReclamacao = Ferramentas.qtdAtendimentosAbertosTipo(reclamacao);
                        request.setAttribute("qtdAtendimentosAbertosReclamacao", qtdAtendimentosAbertosReclamacao);

                        TipoAtendimentoBean elogio = TipoAtendimentoFacade.retornaTipoAtendimento(2);
                        request.setAttribute("elogio", elogio);
                        int qtdAtendimentosElogio = Ferramentas.qtdAtendimentosTipo(elogio);
                        request.setAttribute("qtdAtendimentosElogio", qtdAtendimentosElogio);
                        int qtdAtendimentosAbertosElogio = Ferramentas.qtdAtendimentosAbertosTipo(elogio);
                        request.setAttribute("qtdAtendimentosAbertosElogio", qtdAtendimentosAbertosElogio);

                        TipoAtendimentoBean sugestao = TipoAtendimentoFacade.retornaTipoAtendimento(3);
                        request.setAttribute("sugestao", sugestao);
                        int qtdAtendimentosSugestao = Ferramentas.qtdAtendimentosTipo(sugestao);
                        request.setAttribute("qtdAtendimentosSugestao", qtdAtendimentosSugestao);
                        int qtdAtendimentosAbertosSugestao = Ferramentas.qtdAtendimentosAbertosTipo(sugestao);
                        request.setAttribute("qtdAtendimentosAbertosSugestao", qtdAtendimentosAbertosSugestao);

                        request.setAttribute("gerente", gerente);
                        LoginBean loginBean = new LoginBean(gerente.getIdGerente(), gerente.getPrimeiroNome());
                        HttpSession session = request.getSession();
                        session.setAttribute("logado", loginBean);

                        // Adicionar caminho para portal de GerenteBean
                        RequestDispatcher rd = sc.getRequestDispatcher("/gerente/portalGerente.jsp");
                        rd.forward(request, response);
                    }
                } catch (GerenteException | FerramentasException | TipoAtendimentoException e) {
                    request.setAttribute("msg", "ERRO: " + e.getMessage());
                    RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                    rd.forward(request, response);
                }

                if (nomeCliente == null && nomeFuncionario == null && nomeGerente == null) {
                    RequestDispatcher rd = sc.getRequestDispatcher("/index.jsp");
                    request.setAttribute("msg", "Usuário/Senha inválidos");
                    rd.forward(request, response);
                }
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
