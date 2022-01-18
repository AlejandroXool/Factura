package com.tuempresa.facturacion.acciones;

import org.openxava.actions.*;

public class MostrarOcultarCrearFactura extends OnChangePropertyBaseAction{

	 public void execute() throws Exception {
	        if (estaPedidoCreado() && estaEntregado() && !tieneFactura()) {
	            addActions("Pedido.crearFactura");
	        }
	        else {
	            removeActions("Pedido.crearFactura");
	        }
	    }
		
	    private boolean estaPedidoCreado() {
	        return getView().getValue("oid") != null;
	    }
		
	    private boolean estaEntregado() {
	        Boolean entregado = (Boolean)
	            getView().getValue("entregado"); 
	        return entregado == null?false:entregado;
	    }

	    private boolean tieneFactura() {
	        return getView().getValue("factura.oid") != null;
	    } 	
}
