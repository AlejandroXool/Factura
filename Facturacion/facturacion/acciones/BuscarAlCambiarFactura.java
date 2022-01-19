package com.tuempresa.facturacion.acciones;

import java.util.*;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.view.*;

import com.tuempresa.facturacion.modelo.*;


public class BuscarAlCambiarFactura extends OnChangeSearchAction{

	 public void execute() throws Exception {
	        super.execute();
	        Map clave = getView()
	            .getKeyValuesWithValue();
	        if (clave.isEmpty()) return;
	        Factura factura = (Factura)
	            MapFacade.findEntity(getView().getModelName(), clave);
	        View vistaCliente = getView().getRoot().getSubview("cliente");
	        int numeroCliente = vistaCliente.getValueInt("numero");
	        if (numeroCliente == 0) {
	            vistaCliente.setValue("numero", factura.getCliente().getNumero());
	            vistaCliente.refresh();
	        } 
	        else { 
	            if (numeroCliente != factura.getCliente().getNumero()) {
	                addError("cliente_factura_no_coincide", 
	                    factura.getCliente().getNumero(), factura, numeroCliente);
	                getView().clear();
	            }
	        }
	    }
}
