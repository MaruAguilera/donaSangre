package model;

public class Pedido {
	int id;
	 String tipo;
	 String factor;
	 String hospital;
	 String receptor;
	 
	 //constructor
	 public Pedido(int id,String tipo,String factor,String hospital, String receptor) {
		 this.id=id;
		 this.tipo= tipo;
		 this.factor=factor;
		 this.hospital=hospital;
		 this.receptor=receptor;
		
		 //no hace falta aclarar el retorno en los contructores, pq lo q devuelven siempre es el objeto creado
	 }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFactor() {
		return factor;
	}

	public void setFactor(String factor) {
		this.factor = factor;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getReceptor() {
		return receptor;
	}

	public void setReceptor(String receptor) {
		this.receptor = receptor;
	}
	 

}	
