package com.tuempresa.facturacion.acciones;

import java.util.*;

import javax.ejb.*;

import org.openxava.actions.*;
import org.openxava.model.*;

import com.tuempresa.facturacion.modelo.*;

public class CrearFacturaDesdePedidosSeleccionados extends TabBaseAction{

	public void execute() throws Exception {
        Collection<Pedido> pedidos = getPedidosSeleccionados(); 
        Factura factura = Factura.crearDesdePedidos(pedidos); 
        addMessage("factura_creada_desde_pedidos", factura, pedidos);
        
        showNewView();
        
        getView().setModel(factura);
        getView().setKeyEditable(false);
        setControllers("EdicionFactura");
    }

    private Collection<Pedido> getPedidosSeleccionados()
        throws FinderException
    {
        Collection<Pedido> pedidos = new ArrayList<>();
        for (Map key: getTab().getSelectedKeys()) {
            Pedido pedido = (Pedido) MapFacade.findEntity("Pedido", key);
            pedidos.add(pedido);
        }
        return pedidos;
    }
}
