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
import com.ufpr.tads.web2.beans.CidadeBean;
import com.ufpr.tads.web2.beans.ClienteBean;
import com.ufpr.tads.web2.beans.EnderecoBean;
import com.ufpr.tads.web2.beans.EstadoBean;
import com.ufpr.tads.web2.beans.LoginBean;
import com.ufpr.tads.web2.beans.ProdutoBean;
import com.ufpr.tads.web2.beans.SituacaoBean;
import com.ufpr.tads.web2.beans.TipoAtendimentoBean;
import com.ufpr.tads.web2.facade.AtendimentoException;
import com.ufpr.tads.web2.facade.AtendimentoFacade;
import com.ufpr.tads.web2.facade.CidadeException;
import com.ufpr.tads.web2.facade.CidadeFacade;
import com.ufpr.tads.web2.facade.ClienteException;
import com.ufpr.tads.web2.facade.ClienteFacade;
import com.ufpr.tads.web2.facade.EstadoException;
import com.ufpr.tads.web2.facade.EstadoFacade;
import com.ufpr.tads.web2.facade.ProdutoException;
import com.ufpr.tads.web2.facade.ProdutoFacade;
import com.ufpr.tads.web2.facade.SituacaoException;
import com.ufpr.tads.web2.facade.SituacaoFacade;
import com.ufpr.tads.web2.facade.TipoAtendimentoException;
import com.ufpr.tads.web2.facade.TipoAtendimentoFacade;

@WebServlet(name = "ClienteController", urlPatterns = { "/ClienteController" })
public class ClienteController extends HttpServlet {

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
                        // Apresenta a tela inicial do cliente com seus atendimentos tanto abertos quanto fechados
                        ClienteBean cliente = ClienteFacade.retornaCliente(logado.getId());
                        List<AtendimentoBean> listaAtendimentos = AtendimentoFacade.getListaPorCliente(cliente);
                        if (listaAtendimentos.size() > 0) {
                            Collections.sort(listaAtendimentos, (AtendimentoBean a1, AtendimentoBean a2) -> a2
                                    .getDataHoraInicio().compareTo(a1.getDataHoraInicio()));
                            request.setAttribute("listaAtendimentos", listaAtendimentos);
                        }
                        request.setAttribute("cliente", cliente);

                        RequestDispatcher rd = sc.getRequestDispatcher("/cliente/portalCliente.jsp");
                        rd.forward(request, response);
                    } catch (AtendimentoException | ClienteException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                } else if (action.equals("mostraAtendimento")) {
                    try {
                        // Apresenta os detalhes do atendimento do cliente 
                        AtendimentoBean atendimento = AtendimentoFacade
                                .retornaAtendimento(Integer.parseInt(request.getParameter("idAtendimento")));
                        ClienteBean cliente = ClienteFacade.retornaCliente(logado.getId());
                        request.setAttribute("cliente", cliente);
                        request.setAttribute("atendimento", atendimento);

                        RequestDispatcher rd = sc.getRequestDispatcher("/cliente/visualizarAtendimento.jsp");
                        rd.forward(request, response);
                    } catch (AtendimentoException | NumberFormatException | ClienteException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                } else if (action.equals("removeAtendimento")) {
                    try {
                        
                        // Cliente excluir o ticket que ainda não foi solucionado
                        AtendimentoBean atendimento = AtendimentoFacade
                                .retornaAtendimento(Integer.parseInt(request.getParameter("idAtendimento")));

                        SituacaoBean emAberto = SituacaoFacade.retornaSituacao(1);
                        SituacaoBean situacaoAtendimento = atendimento.getSituacao();

                        String sitEmAberto = emAberto.getEstado();
                        String sitAtendimento = situacaoAtendimento.getEstado();

                        if (sitAtendimento.equals(sitEmAberto)) {
                            boolean confirmaRemocao = AtendimentoFacade.removerAtendimento(atendimento);

                            if (confirmaRemocao) {
                                response.sendRedirect(request.getContextPath() + "/ClienteController?action=portal");
                            } else {
                                request.setAttribute("msg",
                                        "Erro ao remover atendimento de id: " + atendimento.getIdAtendimento());
                                RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                                rd.forward(request, response);
                            }
                        } else {
                            request.setAttribute("msg", "Não é possível remover atendimentos fechados");
                            // Adicionar caminho para tela de onde veio o comando de remoção
                            RequestDispatcher rd = request.getRequestDispatcher("");
                            rd.forward(request, response);
                        }
                    } catch (AtendimentoException | NumberFormatException | SituacaoException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                } else if (action.equals("formNovoAtendimento")) {
                    try {
                        //apresenta o formulario para incluir um novo atendimento
                        List<ProdutoBean> listaProdutos = ProdutoFacade.getLista();
                        request.setAttribute("listaProdutos", listaProdutos);
                        List<TipoAtendimentoBean> listaTipoAtendimento = TipoAtendimentoFacade.getLista();
                        request.setAttribute("listaTipoAtendimento", listaTipoAtendimento);

                        RequestDispatcher rd = sc.getRequestDispatcher("/cliente/novoAtendimento.jsp");
                        rd.forward(request, response);
                    } catch (ProdutoException | TipoAtendimentoException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                } else if (action.equals("novoAtendimento")) {
                    try {
                        // Insere o novo atendimento do formulario acima 
                        AtendimentoBean atendimento = new AtendimentoBean();

                        ClienteBean cliente = ClienteFacade.retornaCliente(logado.getId());
                        atendimento.setCliente(cliente);
                        ProdutoBean produto = ProdutoFacade
                                .retornaProduto(Integer.parseInt(request.getParameter("idProduto")));
                        atendimento.setProduto(produto);
                        TipoAtendimentoBean tipoAtendimento = TipoAtendimentoFacade
                                .retornaTipoAtendimento(Integer.parseInt(request.getParameter("idTipoAtendimento")));
                        atendimento.setTipoAtendimento(tipoAtendimento);

                        atendimento.setDataHoraInicio(Calendar.getInstance());
                        atendimento.setReclamacao(request.getParameter("reclamacao"));

                        AtendimentoBean novoAtendimento = AtendimentoFacade.adicionaAtendimento(atendimento);

                        if (novoAtendimento != null) {
                            if (novoAtendimento.getTipoAtendimento().getIdTipo() == 2) {
                                int qtdReclamacoes = produto.getQtdReclamacoes();
                                qtdReclamacoes++;
                                produto.setQtdReclamacoes(qtdReclamacoes);
                                boolean modificou = ProdutoFacade.modificaProduto(produto);
                                if (modificou) {
                                    response.sendRedirect(request.getContextPath() + "/ClienteController?action=portal");
                                } else {
                                    request.setAttribute("msg",
                                            "Erro ao atualizar quantidade de reclamacoes de produto");
                                    RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                                    rd.forward(request, response);
                                }
                            } else {
                                response.sendRedirect(request.getContextPath() + "/ClienteController?action=portal");
                            }
                        } else {
                            request.setAttribute("msg", "Erro ao adicionar novo atendimento");
                            RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                            rd.forward(request, response);
                        }
                    } catch (ClienteException | ProdutoException | TipoAtendimentoException | NumberFormatException
                            | AtendimentoException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                } else if (action.equals("formVisualizaCliente")) {
                    try {
                        // Tras as informaçoes para cliente vizualizar o seu cadastro "CAMPOS FICAM CINZA" ou seja, não consegue editar nessa tela
                        
                        ClienteBean cliente = ClienteFacade.retornaCliente(logado.getId());
                        request.setAttribute("cliente", cliente);
                        CidadeBean cidade = cliente.getEndereco().getCidade();
                        EstadoBean estado = cidade.getEstado();
                        request.setAttribute("cidade", cidade);
                        request.setAttribute("estado", estado);

                        RequestDispatcher rd = sc.getRequestDispatcher("/cliente/clienteVisualizar.jsp");
                        rd.forward(request, response);
                    } catch (ClienteException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                } else if (action.equals("formModificaCliente")) {
                    try {
                        
                        //Apresenta formulario do cadastro do cliente, pode modificar informações
                        ClienteBean cliente = ClienteFacade.retornaCliente(logado.getId());
                        CidadeBean cidade = cliente.getEndereco().getCidade();
                        EstadoBean estado = cidade.getEstado();
                        request.setAttribute("cidadeCliente", cidade);
                        request.setAttribute("estadoCliente", estado);
                        request.setAttribute("cliente", cliente);
                        List<EstadoBean> listaEstados = EstadoFacade.getLista();
                        List<CidadeBean> listaCidades = CidadeFacade.getLista(estado);
                        request.setAttribute("listaEstados", listaEstados);
                        request.setAttribute("listaCidades", listaCidades);

                        RequestDispatcher rd = sc.getRequestDispatcher("/cliente/clienteAlterar.jsp");
                        rd.forward(request, response);
                    } catch (ClienteException | EstadoException | CidadeException e) {
                        request.setAttribute("msg", "ERRO: " + e.getMessage());
                        RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                        rd.forward(request, response);
                    }
                } else if (action.equals("modificaCliente")) {
                    try {
                        
                        /* Modifica o cliente com base nos dados preenchidos no formulário*/
                        
                        EnderecoBean endereco = new EnderecoBean();
                        EstadoBean estado = EstadoFacade.retornaEstado(Integer.parseInt(request.getParameter("estado")));
                        CidadeBean cidade = CidadeFacade.retornaCidade(Integer.parseInt(request.getParameter("cidade")));
                        cidade.setEstado(estado);
                        endereco.setCidade(cidade);
                        endereco.setRua(request.getParameter("rua"));
                        endereco.setNumero(Integer.parseInt(request.getParameter("numero")));
                        endereco.setBairro(request.getParameter("bairro"));
                        endereco.setCep(
                                Integer.parseInt(request.getParameter("cep").replace(".", "").replace("-", "")));
                        endereco.setComplemento(request.getParameter("complemento"));

                        ClienteBean cliente = ClienteFacade.retornaCliente(logado.getId());
                        endereco.setId(cliente.getEndereco().getId());
                        cliente.setEndereco(endereco);
                        cliente.setPrimeiroNome(request.getParameter("primeiroNome"));
                        cliente.setSobreNome(request.getParameter("sobreNome"));
                        cliente.setTelefone(request.getParameter("telefone"));

                        String senha = request.getParameter("senha");
                        if (senha != null || !senha.equals("")) {
                            cliente.setSenha(request.getParameter("senha"));
                        }

                        boolean confirmaModificacao = ClienteFacade.modificaCliente(cliente);

                        if (confirmaModificacao) {
                            LoginBean loginBean = new LoginBean(cliente.getIdCliente(), cliente.getPrimeiroNome());
                            session.setAttribute("logado", loginBean);
                            response.sendRedirect(request.getContextPath() + "/ClienteController?action=portal");
                        } else {
                            request.setAttribute("msg", "Erro ao modificar cliente de id: " + logado.getId());
                            RequestDispatcher rd = sc.getRequestDispatcher("/erro.jsp");
                            rd.forward(request, response);
                        }
                    } catch (EstadoException | CidadeException | ClienteException e) {
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
