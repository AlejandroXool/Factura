package com.tuempresa.factura.modelo;

import java.time.*;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity

@View(extendsView="super.DEFAULT", 
members = "diasEntregaEstimados," + 
"factura { factura } ")

@View( name="SinClienteNiFactura", 
members=                      
    "anyo, numero, fecha;" +   
    "detalles;" +
    "observaciones"
)
public class Pedido extends DocumentoComercial {
	
	@ManyToOne 
	@ReferenceView("SinClienteNiPedidos")
    DocumentoComercial factura;
	
	 @Column(columnDefinition="INTEGER DEFAULT 1")
	    int diasEntrega;
	 
	    @Depends("fecha")
	    public int getDiasEntregaEstimados() {
	        if (getFecha().getDayOfYear() < 15) {
	            return 20 - getFecha().getDayOfYear(); 
	        }
	        if (getFecha().getDayOfWeek() == DayOfWeek.SUNDAY) return 2;
	        if (getFecha().getDayOfWeek() == DayOfWeek.SATURDAY) return 3;
	        return 1;
	    }
	    
	    @PrePersist @PreUpdate 
	    private void recalcularDiasEntrega() {
	        setDiasEntrega(getDiasEntregaEstimados());
	    }

		private void setDiasEntrega(int diasEntregaEstimados) {
			
			
		}


}
