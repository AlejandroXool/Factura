package com.tuempresa.facturacion.modelo;
 
import java.time.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.apache.commons.beanutils.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import com.tuempresa.facturacion.acciones.*;

import lombok.*;
 
@Entity @Getter @Setter
@View(extendsView="super.DEFAULT", 
	members=
		"diasEntregaEstimados, entregado, " + 
		"factura { factura } " 
)
@View( name="SinClienteNiFactura", 
	members=                       
	    "anyo, numero, fecha;" +   
	    "detalles;" +
	    "observaciones"
)
@Tab(baseCondition = "eliminado = false")
@Tab(name="Eliminado", baseCondition = "eliminado = true")
public class Pedido extends DocumentoComercial {
 
    @ManyToOne
    @ReferenceView("SinClienteNiPedidos") 
    @OnChange(MostrarOcultarCrearFactura.class)
    @OnChangeSearch(BuscarAlCambiarFactura.class)
    @SearchAction("Pedido.buscarFactura")
    Factura factura; 
    
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
    
    @Column(columnDefinition="BOOLEAN DEFAULT FALSE")
    @OnChange(MostrarOcultarCrearFactura.class)
    boolean entregado;
    
    public void setEliminado(boolean eliminado) {
        if (eliminado) validarPreBorrar(); 
        super.setEliminado(eliminado);
    }
    
    @PrePersist @PreUpdate 
    private void recalcularDiasEntrega() {
        setDiasEntrega(getDiasEntregaEstimados());
    }
    
    @AssertTrue(message="pedido_debe_estar_entregado")
	private boolean isEntregadoParaEstarEnFactura() { 
	    return factura == null || isEntregado(); 
	}
    
    @PreRemove
    private void validarPreBorrar() {
        if (factura != null) { 
            throw new javax.validation.ValidationException( 
                XavaResources.getString( 
                    "no_puede_borrar_pedido_con_factura"));
        }
    }
        
        public void crearFactura()
            throws CrearFacturaException 
        	 {
        	     if (this.factura != null) { 
        	         throw new CrearFacturaException( 
        	             "pedido_ya_tiene_factura"); 
        	     }
        	     if (!isEntregado()) { 
        	         throw new CrearFacturaException("pedido_no_entregado");
        	     }
        	     try {
        	         Factura factura = new Factura(); 
        	         BeanUtils.copyProperties(factura, this); 
        	         factura.setOid(null); 
        	         factura.setFecha(LocalDate.now()); 
        	         factura.setDetalles(new ArrayList<>(getDetalles())); 
        	         XPersistence.getManager().persist(factura);
        	         this.factura = factura; 
        	     }
        	     catch (Exception ex) { 
        	         throw new SystemException( 
        	             "imposible_crear_factura", ex);
        	     }
        	 }
        
        public void copiarDetallesAFactura() { 
            factura.getDetalles().addAll(getDetalles());
            factura.setIva(factura.getIva().add(getIva()));
            factura.setImporteTotal( 
    		    factura.getImporteTotal().add(getImporteTotal()));
        }
        
        @AssertTrue(message="cliente_pedido_factura_coincidir") 
        private boolean isClienteFacturaCoincide() {
        	return factura == null ||
        		factura.getCliente().getNumero() == getCliente().getNumero();
        }
}

