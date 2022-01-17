package com.tuempresa.facturacion.validadores;

import org.openxava.util.*;
import org.openxava.validators.*;

import com.tuempresa.facturacion.modelo.*;

public class ValidadorBorrarPedido implements IRemoveValidator {

	 private Pedido pedido;
	 
	    public void setEntity(Object entity)
	        throws Exception
	    {
	        this.pedido = (Pedido) entity;
	    }
	 
	    public void validate(Messages errors) 
	        throws Exception
	    {
	        if (pedido.getFactura() != null) {
	            errors.add("no_puede_borrar_pedido_con_factura");
	        }
	    }
}
