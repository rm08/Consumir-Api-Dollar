package shx.cotacaodolar.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "moeda")
public class Moeda implements Serializable {	
		private static final long serialVersionUID = 1L;
	
	    @NotNull(message = "O preÃ§o nÃ£o pode ser nulo")
	    @Min(value = 0, message = "O preÃ§o nÃ£o pode ser negativo")
	    @Column(nullable = false, updatable = false)
	    public Double preco;
	    
	    @Id
	    @NotNull(message = "A data nÃ£o pode ser nula")
	    @Column(nullable = false, updatable = false, unique = true)
	    public String data;
	    
	    @NotNull(message = "A hora nÃ£o pode ser nula")
	    @Column(nullable = false, updatable = false)
	    public String hora;
	    
	    public Moeda() {}
	    
	    public Moeda(BigDecimal preco, String data, String hora) {
	    	this.preco = preco.doubleValue();
	    	this.data = data;
	    	this.hora = hora;
	    }

	    public String toString(){
	        return preco.toString() + data.toString() + hora;
	    }

		public Double getPreco() {
			return preco;
		}

		public void setPreco(Double preco) {
			this.preco = preco;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}

		public String getHora() {
			return hora;
		}

		public void setHora(String hora) {
			this.hora = hora;
		}	    
	    
}