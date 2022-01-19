package com.tuempresa.facturacion.acciones;

import org.openxava.actions.*;
import org.openxava.jpa.*;

import com.tuempresa.facturacion.modelo.*;

public class CrearFacturaDesdePedido extends ViewBaseAction{

	public void execute() throws Exception {
		 if (getView().getValue("oid") == null) { 
		        addError("imposible_crear_factura_pedido_no_existe");
		        return;
		    }
        Pedido pedido = XPersistence.getManager().find(
            Pedido.class, 
            getView().getValue("oid"));
        pedido.crearFactura();
        getView().refresh(); 
        addMessage("factura_creada_desde_pedido", pedido.getFactura());
        
     
        removeActions("Pedido.crearFactura");
    }
}
