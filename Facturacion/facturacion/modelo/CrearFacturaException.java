package com.tuempresa.facturacion.modelo;

import org.openxava.util.*;

public class CrearFacturaException extends Exception{

	public CrearFacturaException(String mensaje) {
        
        super(XavaResources.getString(mensaje)); 
    }
}
