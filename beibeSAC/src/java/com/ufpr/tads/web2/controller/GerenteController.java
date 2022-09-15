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
import com.ufpr.tads.web2.beans.CidadeBean;
import com.ufpr.tads.web2.beans.EnderecoBean;
import com.ufpr.tads.web2.beans.EstadoBean;
import com.ufpr.tads.web2.beans.FuncionarioBean;
import com.ufpr.tads.web2.beans.GerenteBean;
import com.ufpr.tads.web2.beans.LoginBean;
import com.ufpr.tads.web2.beans.SituacaoBean;
import com.ufpr.tads.web2.beans.TipoAtendimentoBean;
import com.ufpr.tads.web2.facade.AtendimentoException;
import com.ufpr.tads.web2.facade.AtendimentoFacade;
import com.ufpr.tads.web2.facade.CidadeException;
import com.ufpr.tads.web2.facade.CidadeFacade;
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

@WebServlet(name = "GerenteController", urlPatterns = { "/GerenteController" })
public class GerenteController extends HttpServlet {

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
                
                // Apresenta o dashboard principal do gerente (HOME)
                if (action == null || action.equals("portal")) {
                    try {
                        GerenteBean gerente = GerenteFacade.retornaGerente(logado.getId());
                        request.setAttribute("gerente", gerente);
                        // Somatória de todos os atendimento sja abertos no sistema
                        int qtdAtendimentos = Ferramentas.qtdAtendimentos();
                        request.setAttribute("qtdAtendimentos", qtdAtendimentos);
                        // calcula quantidade de atendimentos sem solução no sistema (tickets com status "em aberto")
                        int qtdAtendimentosAbertos = Ferramentas.qtdAtendimentosAbertos();
                        request.setAttribute("qtdAtendimentosAbertos", qtdAtendimentosAbertos);
                        
                        // Calcula o perentual de atendimentos em aberto(sem atendeimento) com base em todos atendimento ja abertos no sistema inteiro
                        float percentualAtendimentosAbertos = Ferramentas.calculaPercentual(qtdAtendimentosAbertos,
                                qtdAtendimentos);
                        request.setAttribute("percentualAtendimentosAbertos", percentualAtendimentosAbertos);
                        
                        // *** Apresenta o tipo de reclamação "PRESENCIAL" 
                        TipoAtendimentoBean reclamacao = TipoAtendimentoFacade.retornaTipoAtendimento(1);
                        request.setAttribute("reclamacao", reclamacao);
                        int qtdAtendimentosReclamacao = Ferramentas.qtdAtendimentosTipo(reclamacao);
                        request.setAttribute("qtdAtendimentosReclamacao", qtdAtendimentosReclamacao);
                        int qtdAtendimentosAbertosReclamacao = Ferramentas.qtdAtendimentosAbertosTipo(reclamacao);
                        request.setAttribute("qtdAtendimentosAbertosReclamacao", qtdAtendimentosAbertosReclamacao);

                        // *** Apresenta o tipo de reclamação "TELEFONICO"
                        TipoAtendimentoBean elogio = TipoAtendimentoFacade.retornaTipoAtendimento(2);
                        request.setAttribute("elogio", elogio);
                        int qtdAtendimentosElogio = Ferramentas.qtdAtendimentosTipo(elogio);
                        request.setAttribute("qtdAtendimentosElogio", qtdAtendimentosElogio);
                        int qtdAtendimentosAbertosElogio = Ferramentas.qtdAtendimentosAbertosTipo(elogio);
                        request.setAttribute("qtdAtendimentosAbertosElogio", qtdAtendimentosAbertosElogio);

                        // *** Apresenta o tipo de reclamação "E-MAIL"
                        TipoAtendimentoBean sugestao = TipoAtendimentoFacade.retornaTipoAtendimento(3);
                        request.setAttribute("sugestao", sugestao);
                        int qtdAtendimentosSugestao = Ferramentas.qtdAtendimentosTipo(sugestao);
                        request.setAttribute("qtdAtendimentosSugestao", qtdAtendimentosSugestao);
                        int qtdAtendimentosAbertosSugestao = Ferramentas.qtdAtendimentosAbertosTipo(sugestao);
                        request.setAttribute("qtdAtendimentosAbertosSugestao", qtdAtendimentosAbertosSugestao);

                        RequestDispatcher rd = sc.getRequestDispatcher("/gerente/portalGerente.jsp");
                        rd.forward(request, response);
                    } catch (GerenteException | FerramentasException | TipoAtendimentoException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                    
                    // Apresenta a lista dos funcionarios cadastrados
                } else if (action.equals("todosCadastrados")) {
                    try {
                        GerenteBean gerente = GerenteFacade.retornaGerente(logado.getId());
                        request.setAttribute("gerente", gerente);
                        List<FuncionarioBean> listaFuncionarios = FuncionarioFacade.getLista();
                        request.setAttribute("listaFuncionarios", listaFuncionarios);
                        List<GerenteBean> listaGerentes = GerenteFacade.getLista();

                        for (int i = 0; i < listaGerentes.size(); i++) {
                            GerenteBean g = listaGerentes.get(i);
                            if (g.getIdGerente() == gerente.getIdGerente())
                                listaGerentes.remove(g);
                        }
                        request.setAttribute("listaGerentes", listaGerentes);

                        RequestDispatcher rd = sc.getRequestDispatcher("/gerente/todosCadastrados.jsp");
                        rd.forward(request, response);
                    } catch (GerenteException | FuncionarioException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                    
                    // Apresenta Formulario de alteração de cadastro de funcionario 
                } else if (action.equals("formAlterarFuncionario")) {
                    try {
                        FuncionarioBean funcionario = FuncionarioFacade
                                .retornaFuncionario(Integer.parseInt(request.getParameter("idCadastrado")));
                        request.setAttribute("cadastrado", funcionario);
                        List<EstadoBean> listaEstados = EstadoFacade.getLista();
                        request.setAttribute("listaEstados", listaEstados);
                        String tipo = "funcionario";
                        request.setAttribute("tipo", tipo);
                        RequestDispatcher rd = sc.getRequestDispatcher("/gerente/formCadastro.jsp");
                        rd.forward(request, response);
                    } catch (FuncionarioException | EstadoException | NumberFormatException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                    
                    // Apresenta formulario para alteração do gerente 
                } else if (action.equals("formAlterarGerente")) {
                    try {
                        GerenteBean gerente = GerenteFacade
                                .retornaGerente(Integer.parseInt(request.getParameter("idCadastrado")));
                        request.setAttribute("cadastrado", gerente);
                        List<EstadoBean> listaEstados = EstadoFacade.getLista();
                        request.setAttribute("listaEstados", listaEstados);
                        String tipo = "gerente";
                        request.setAttribute("tipo", tipo);
                        RequestDispatcher rd = sc.getRequestDispatcher("/gerente/formCadastro.jsp");
                        rd.forward(request, response);
                    } catch (GerenteException | EstadoException | NumberFormatException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                    
                    // Apresente formulario para novos cadastros
                } else if (action.equals("formNovo")) {
                    try {
                        List<EstadoBean> listaEstados = EstadoFacade.getLista();
                        request.setAttribute("listaEstados", listaEstados);

                        RequestDispatcher rd = sc.getRequestDispatcher("/gerente/formCadastro.jsp");
                        rd.forward(request, response);
                    } catch (EstadoException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                    
                    // Apresenta informaçoes de um cadastro ja existente
                } else if (action.equals("visualizarCadastro")) {
                    try {
                        String tipo = request.getParameter("tipo");
                        if (tipo.equals("gerente")) {
                            GerenteBean gerente = GerenteFacade
                                    .retornaGerente(Integer.parseInt(request.getParameter("idCadastrado")));
                            request.setAttribute("cadastrado", gerente);
                        } else if (tipo.equals("funcionario")) {
                            FuncionarioBean funcionario = FuncionarioFacade
                                    .retornaFuncionario(Integer.parseInt(request.getParameter("idCadastrado")));
                            request.setAttribute("cadastrado", funcionario);
                        }
                        RequestDispatcher rd = sc.getRequestDispatcher("/gerente/cadastroVisualizar.jsp");
                        rd.forward(request, response);
                    } catch (GerenteException | FuncionarioException | NumberFormatException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                    
                    //Realiza a alteração do cadastro com as informações preenchidas no formulario
                } else if (action.equals("alterarCadastro")) {
                    try {
                        EstadoBean estado = EstadoFacade.retornaEstado(Integer.parseInt(request.getParameter("idEstado")));
                        CidadeBean cidade = CidadeFacade.retornaCidade(Integer.parseInt(request.getParameter("idCidade")));
                        cidade.setEstado(estado);

                        String tipo = request.getParameter("tipo");
                        if (tipo.equals("gerente")) {
                            GerenteBean gerente = GerenteFacade
                                    .retornaGerente(Integer.parseInt(request.getParameter("idCadastrado")));
                            EnderecoBean endereco = gerente.getEndereco();
                            endereco.setCidade(cidade);
                            endereco.setRua(request.getParameter("rua"));
                            endereco.setNumero(Integer.parseInt(request.getParameter("numero")));
                            endereco.setBairro(request.getParameter("bairro"));
                            endereco.setCep(Integer.parseInt(request.getParameter("cep")));
                            endereco.setComplemento(request.getParameter("complemento"));

                            gerente.setPrimeiroNome(request.getParameter("primeiroNome"));
                            gerente.setSobreNome(request.getParameter("sobreNome"));
                            gerente.setTelefone(request.getParameter("telefone"));

                            String senha = request.getParameter("senha");
                            if (senha.length() > 0) {
                                gerente.setSenha(senha);
                            }

                            boolean modificou = GerenteFacade.modificaGerente(gerente);
                            if (modificou) {
                                response.sendRedirect(
                                        request.getContextPath() + "/GerenteController?action=todosCadastrados");
                            } else {
                                request.setAttribute("msg",
                                        "Erro ao modificar gerente de id: " + gerente.getIdGerente());
                                RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                                rd.forward(request, response);
                            }//
                        } else if (tipo.equals("funcionario")) {
                            FuncionarioBean funcionario = FuncionarioFacade
                                    .retornaFuncionario(Integer.parseInt(request.getParameter("idCadastrado")));
                            EnderecoBean endereco = funcionario.getEndereco();
                            endereco.setCidade(cidade);
                            endereco.setRua(request.getParameter("rua"));
                            endereco.setNumero(Integer.parseInt(request.getParameter("numero")));
                            endereco.setBairro(request.getParameter("bairro"));
                            endereco.setCep(Integer.parseInt(request.getParameter("cep")));
                            endereco.setComplemento(request.getParameter("complemento"));

                            funcionario.setPrimeiroNome(request.getParameter("primeiroNome"));
                            funcionario.setSobreNome(request.getParameter("sobreNome"));
                            funcionario.setTelefone(request.getParameter("telefone"));

                            String senha = request.getParameter("senha");
                            if (senha.length() > 0) {
                                funcionario.setSenha(senha);
                            }

                            boolean modificou = FuncionarioFacade.modificaFuncionario(funcionario);
                            if (modificou) {
                                response.sendRedirect(
                                        request.getContextPath() + "/GerenteController?action=todosCadastrados");
                            } else {
                                request.setAttribute("msg",
                                        "Erro ao modificar funcionario de id: " + funcionario.getIdFuncionario());
                                RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                                rd.forward(request, response);
                            }
                        }
                    } catch (EstadoException | CidadeException | GerenteException | FuncionarioException
                            | NumberFormatException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                    
                    // Inclui um novo usuário com base nos dados incluidos no fomulario de cadastro (Gerente ou Funcionario)//
                } else if (action.equals("novoCadastro")) {
                    try {
                        EnderecoBean endereco = new EnderecoBean();

                        EstadoBean estado = EstadoFacade.retornaEstado(Integer.parseInt(request.getParameter("idEstado")));
                        CidadeBean cidade = CidadeFacade.retornaCidade(Integer.parseInt(request.getParameter("idCidade")));
                        cidade.setEstado(estado);
                        endereco.setCidade(cidade);
                        endereco.setRua(request.getParameter("rua"));
                        endereco.setNumero(Integer.parseInt(request.getParameter("numero")));
                        endereco.setBairro(request.getParameter("bairro"));
                        endereco.setCep(Integer.parseInt(request.getParameter("cep")));
                        endereco.setComplemento(request.getParameter("complemento"));

                        String tipo = request.getParameter("tipo");
                        if (tipo.equals("gerente")) {
                            GerenteBean gerente = new GerenteBean();
                            gerente.setEndereco(endereco);
                            gerente.setPrimeiroNome(request.getParameter("primeiroNome"));
                            gerente.setSobreNome(request.getParameter("sobreNome"));
                            gerente.setTelefone(request.getParameter("telefone"));
                            gerente.setSenha(request.getParameter("senha"));
                            // Colocar metodo para conferir validade de cpf
                            gerente.setCpf(Long.parseLong(request.getParameter("cpf")));
                            boolean confereEmail = Ferramentas.confereEmail(request.getParameter("email"));
                            if (confereEmail) {
                                List<EstadoBean> listaEstados = EstadoFacade.getLista();
                                request.setAttribute("listaEstados", listaEstados);
                                request.setAttribute("msg", "Email ja cadastrado na base de dados");
                                RequestDispatcher rd = request.getRequestDispatcher("/gerente/formCadastro.jsp");
                                rd.forward(request, response);
                            } else {
                                gerente.setEmail(request.getParameter("email"));
                                GerenteBean novoGerente = GerenteFacade.adicionaGerente(gerente);

                                if (novoGerente != null) {
                                    response.sendRedirect(
                                            request.getContextPath() + "/GerenteController?action=todosCadastrados");
                                } else {
                                    request.setAttribute("msg",
                                            "Erro ao modificar gerente de id: " + gerente.getIdGerente());
                                    RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                                    rd.forward(request, response);
                                }
                            }
                        } else if (tipo.equals("funcionario")) {
                            FuncionarioBean funcionario = new FuncionarioBean();
                            funcionario.setEndereco(endereco);
                            funcionario.setPrimeiroNome(request.getParameter("primeiroNome"));
                            funcionario.setSobreNome(request.getParameter("sobreNome"));
                            funcionario.setTelefone(request.getParameter("telefone"));
                            funcionario.setSenha(request.getParameter("senha"));
                            // Colocar metodo para conferir validade de cpf
                            funcionario.setCpf(Long.parseLong(request.getParameter("cpf")));
                            boolean confereEmail = Ferramentas.confereEmail(request.getParameter("email"));
                            if (confereEmail) {
                                List<EstadoBean> listaEstados = EstadoFacade.getLista();
                                request.setAttribute("listaEstados", listaEstados);
                                request.setAttribute("msg", "Email ja cadastrado na base de dados");
                                RequestDispatcher rd = request.getRequestDispatcher("/gerente/formCadastro.jsp");
                                rd.forward(request, response);
                            } else {
                                funcionario.setEmail(request.getParameter("email"));
                                FuncionarioBean novoFuncionario = FuncionarioFacade.adicionaFuncionario(funcionario);

                                if (novoFuncionario != null) {
                                    response.sendRedirect(
                                            request.getContextPath() + "/GerenteController?action=todosCadastrados");
                                } else {
                                    request.setAttribute("msg",
                                            "Erro ao modificar gerente de id: " + funcionario.getIdFuncionario());
                                    RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                                    rd.forward(request, response);
                                }
                            }
                        }
                    } catch (EstadoException | CidadeException | GerenteException | FuncionarioException
                            | NumberFormatException | FerramentasException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                }else if (action.equals("removerFuncionario")) {
                    try {
                        // Remove o funcionario chamando o facede
                        FuncionarioBean funcionario = FuncionarioFacade
                                .retornaFuncionario(Integer.parseInt(request.getParameter("idFuncionario")));

                        boolean confirmaRemocao = FuncionarioFacade.removerFuncionario(funcionario);

                        if (confirmaRemocao) {
                            response.sendRedirect(request.getContextPath() + "/GerenteController?action=todosCadastrados");
                        } else {
                            GerenteBean gerente = GerenteFacade.retornaGerente(logado.getId());
                            request.setAttribute("gerente", gerente);
                            List<FuncionarioBean> listaFuncionarios = FuncionarioFacade.getLista();
                            request.setAttribute("listaFuncionarios", listaFuncionarios);
                            List<GerenteBean> listaGerentes = GerenteFacade.getLista();

                            for (int i = 0; i < listaGerentes.size(); i++) {
                                GerenteBean g = listaGerentes.get(i);
                                if (g.getIdGerente() == gerente.getIdGerente())
                                    listaGerentes.remove(g);
                            }
                            request.setAttribute("listaGerentes", listaGerentes);
                            request.setAttribute("msg", "Funcionário id " + funcionario.getIdFuncionario()
                                    + " possui Atendimentos e não pode ser removido.");
                            RequestDispatcher rd = request.getRequestDispatcher("/gerente/todosCadastrados.jsp");
                            rd.forward(request, response);
                        }
                    } catch (FuncionarioException | GerenteException | NumberFormatException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                } else if (action.equals("removerGerente")) {
                    try {
                        
                        // Remove o gerente chamando o facede
                        GerenteBean gerente = GerenteFacade
                                .retornaGerente(Integer.parseInt(request.getParameter("idGerente")));

                        boolean confirmaRemocao = GerenteFacade.removeGerente(gerente);

                        if (confirmaRemocao) {
                            response.sendRedirect(request.getContextPath() + "/GerenteController?action=todosCadastrados");
                        } else {
                            request.setAttribute("msg", "Erro ao remover gerente de id: " + gerente.getIdGerente());
                            RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                            rd.forward(request, response);
                        }
                    } catch (GerenteException | NumberFormatException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                    /**********/
                } else if (action.equals("atendimentosAbertos")) {
                    try {
                        
                        GerenteBean gerente = GerenteFacade.retornaGerente(logado.getId());
                        SituacaoBean emAberto = SituacaoFacade.retornaSituacao(1);
                        List<AtendimentoBean> listaAtendimentosAbertos = AtendimentoFacade.getListaPorSituacao(emAberto);
                        if (listaAtendimentosAbertos.size() > 0) {
                            Collections.sort(listaAtendimentosAbertos, (AtendimentoBean a1, AtendimentoBean a2) -> a1
                                    .getDataHoraInicio().compareTo(a2.getDataHoraFim()));
                            request.setAttribute("listaAtendimentosAbertos", listaAtendimentosAbertos);
                        }
                        request.setAttribute("gerente", gerente);

                        RequestDispatcher rd = sc.getRequestDispatcher("/gerente/listarAtendimentos.jsp");
                        rd.forward(request, response);
                    } catch (GerenteException | SituacaoException | AtendimentoException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                } else if (action.equals("todosAtendimentos")) {
                    try {
                        GerenteBean gerente = GerenteFacade.retornaGerente(logado.getId());
                        List<AtendimentoBean> listaAtendimentos = AtendimentoFacade.getLista();
                        if (listaAtendimentos.size() > 0) {
                            Collections.sort(listaAtendimentos, (AtendimentoBean a1, AtendimentoBean a2) -> a1
                                    .getDataHoraInicio().compareTo(a2.getDataHoraInicio()));
                            request.setAttribute("listaAtendimentos", listaAtendimentos);
                        }
                        request.setAttribute("gerente", gerente);

                        RequestDispatcher rd = sc.getRequestDispatcher("/gerente/listarAtendimentos.jsp");
                        rd.forward(request, response);
                    } catch (GerenteException | AtendimentoException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }/**********/
                } else if (action.equals("telaRelatorios")) {
                    try {
                        RequestDispatcher rd = sc.getRequestDispatcher("/gerente/relatorios.jsp");
                        rd.forward(request, response);
                    } catch (Exception e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
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
