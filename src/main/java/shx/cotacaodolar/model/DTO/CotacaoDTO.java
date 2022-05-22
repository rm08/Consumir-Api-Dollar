package shx.cotacaodolar.model.DTO;

import java.math.BigDecimal;

public class CotacaoDTO {
	private BigDecimal cotacaoCompra;	
	private BigDecimal cotacaoVenda;
	private String dataHoraCotacao;    
	private String tipoBoletim;
	
	public BigDecimal getCotacaoCompra() {
		return cotacaoCompra;
	}
	public void setCotacaoCompra(BigDecimal cotacaoCompra) {
		this.cotacaoCompra = cotacaoCompra;
	}
	public BigDecimal getCotacaoVenda() {
		return cotacaoVenda;
	}
	public void setCotacaoVenda(BigDecimal cotacaoVenda) {
		this.cotacaoVenda = cotacaoVenda;
	}
	public String getDataHoraCotacao() {
		return dataHoraCotacao;
	}
	public void setDataHoraCotacao(String dataHoraCotacao) {
		this.dataHoraCotacao = dataHoraCotacao;
	}
	public String getTipoBoletim() {
		return tipoBoletim;
	}
	public void setTipoBoletim(String tipoBoletim) {
		this.tipoBoletim = tipoBoletim;
	}
	
}
