
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/jsp/erro.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>BEIBE estética</title>

         <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/index.css" />
         <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/page-login.css" />
    </head>
            <body class="login">
                <div class="wrapper">
                    <div id="formContent">
                        <!-- Icon -->
                         <div>
                             <img src="./img/user.png" id="icon" alt="User Icon" />
                         </div>
                        <form action="${pageContext.request.contextPath}/LoginServlet?action=logar" method="POST">
                            <div class="flex-row">
                                <input id="login" name="login" placeholder="Digite o email do usuário" type="text">
                            </div>
                            <div class="flex-row">
                                <input id="senha" placeholder="Digite a senha" name="senha" type="password">
                            </div>
                            <input type="submit" value="Login">
                            <c:if test="${msg != null}">
                                <div class="alert alert-danger" role="alert">
                                    <c:out value="${msg}" />
                                </div>
                            </c:if>
                            <div id="formFooter">
                                 Não possui conta?
                                <a href="${pageContext.request.contextPath}/LoginServlet?action=autoCadastro" >Criar Conta</a>
                            </div>
                        </form>
                        <div class="footer">
                            <span>Rua do Embuste, 1313 <br></span>
                            <span>Curitiba, PR, 13131-313 <br> </span>
                            <span>(41) 9 1313-1313 <br> </span>
                        </div>
                    </div> 
                </div>  
            </div>          
        </div>
    </body>
</html>