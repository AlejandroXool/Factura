package com.tuempresa.facturacion.validadores;

import org.openxava.util.*;
import org.openxava.validators.*;

import com.tuempresa.facturacion.modelo.*;

import lombok.*;

@Getter @Setter 
public class ValidadorEntregadoParaEstarEnFactura 
implements IValidator {

	 private int anyo;
     private int numero;
	 private boolean entregado;
	 private Factura factura;
	 
	    public void validate(Messages errors)
	        throws Exception { 
	        if (factura == null) return;
	        if (!entregado) {
	            errors.add(
	                "pedido_debe_estar_entregado",
	                anyo, numero);
	        }
	    }
}
