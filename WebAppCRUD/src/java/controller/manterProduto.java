/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.ProdutoDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Produto;

/**
 *
 * @author guifg
 */
public class manterProduto extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        String mensagem = "";
        String result = null;
        List<Produto> listaProdutos = new ArrayList<>();
        try {
            String opcao = request.getParameter("btnoperacao");
            switch (opcao) {
                
                case "Cadastrar":
                    {
                        int codigoDeBarras = Integer.parseInt(request.getParameter("txtcodigodebarras"));
                        String descricao = request.getParameter("txtdescricao");
                        double preco = Double.parseDouble(request.getParameter("txtpreco"));
                        String marca = request.getParameter("txtmarca");
                        String fornecedor = request.getParameter("txtfornecedor");
                        
                        Produto prod = new Produto();
                        prod.setCodigoDeBarras(codigoDeBarras);
                        prod.setDescricao(descricao);
                        prod.setPreco(preco);
                        prod.setMarca(marca);
                        prod.setFornecedor(fornecedor);
                        ProdutoDAO pdao = new ProdutoDAO();
                        pdao.cadastrar(prod);
                        mensagem = "Cadastrar";
                        result = "Produto cadastrado com sucesso!";
                        
                        break;
                    }
                case "Deletar":
                    {   
                        int codigoDeBarras = Integer.parseInt(request.getParameter("txtcodigodebarras"));
                        Produto prod = new Produto();
                        prod.setCodigoDeBarras(codigoDeBarras);
                        ProdutoDAO pdao = new ProdutoDAO();
                        Produto resposta = pdao.ConsultarById(prod);

                        if(resposta.getDescricao() == null){
                            result = "Não foi encontrado, impossivel deletar!";
                        }else{
                            prod.setCodigoDeBarras(codigoDeBarras);
                            pdao.Deletar(prod);
                            mensagem = "Deletar";
                            result = "Deletado com sucesso!";
                        }
                        break;
                    }
                case "Alterar":
                    {
                        mensagem = "Alterar";

                        int codigoDeBarras = Integer.parseInt(request.getParameter("txtcodigodebarras"));
                        Produto prod = new Produto();
                        prod.setCodigoDeBarras(codigoDeBarras);
                        ProdutoDAO pdao = new ProdutoDAO();
                        Produto resposta = pdao.ConsultarById(prod);

                        if(resposta.getDescricao() == null){
                            result = "Não foi encontrado, impossivel alterar!";
                        }else{
                            String descricao = request.getParameter("txtdescricao");
                            double preco = Double.parseDouble(request.getParameter("txtpreco"));
                            String marca = request.getParameter("txtmarca");
                            String fornecedor = request.getParameter("txtfornecedor");
                            prod.setDescricao(descricao);
                            prod.setPreco(preco);
                            prod.setMarca(marca);
                            prod.setFornecedor(fornecedor);
                            pdao.alterar(prod);
                            result = "Alterado com sucesso!";
                        }
                        
                        break;
                    }
                case "Consultar":
                    {
                        int codigoDeBarras = Integer.parseInt(request.getParameter("txtcodigodebarras"));
                        Produto prod = new Produto();
                        prod.setCodigoDeBarras(codigoDeBarras);
                        ProdutoDAO pdao = new ProdutoDAO();
                        Produto resposta = pdao.ConsultarById(prod);
                        mensagem = "Pesquisar";
                        if(resposta.getDescricao() == null){
                            result = "Não foi encontrado!";
                        }else{
                            listaProdutos.add(resposta);
                        }
                        break;
                    }
                default:
                    break;
            }
            if(!opcao.equals("Consultar")){
                ProdutoDAO pdao = new ProdutoDAO();
                listaProdutos = pdao.ConsultarTodos();
                mensagem = "Listar todos!";        
            }
            
        } catch (ClassNotFoundException | NumberFormatException | SQLException ex) {
            mensagem = "Erro " + ex.getMessage();
        }
        request.setAttribute("mensagem", mensagem);
        request.setAttribute("result", result);
        request.setAttribute("lista", listaProdutos);
        request.getRequestDispatcher("sucessoProduto.jsp").forward(request, response);

        try (PrintWriter out = response.getWriter()) {
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
