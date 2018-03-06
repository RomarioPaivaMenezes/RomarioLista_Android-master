package br.com.romario.cadastro.model;

/**
 * Created by romario on 26/02/18.
 */

public class Produto {



    private String uIdProduto;
    private String descricao;
    private Double quantidade;





    public String getDescricao() {
        return descricao;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public String getuIdProduto() {
        return uIdProduto;
    }

    public void setuIdProduto(String uIdProduto) {
        this.uIdProduto = uIdProduto;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
