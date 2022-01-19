package com.tuempresa.facturacion.acciones;

import org.openxava.actions.*;

public class BuscarFacturaDesdePedido extends ReferenceSearchAction{

	public void execute() throws Exception {
        int numeroCliente =
            getView().getValueInt("cliente.numero"); 
        super.execute(); 
        if (numeroCliente > 0) { 
            getTab().setBaseCondition("${cliente.numero} = " + numeroCliente);
        }
    }
}
