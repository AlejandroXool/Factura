package com.tuempresa.facturacion.acciones;

import org.openxava.actions.*;

public class IrAnyadirPedidosAFactura extends GoAddElementsToCollectionAction{

	public void execute() throws Exception {
        super.execute();
        int numeroCliente =
            getPreviousView() // getPreviousView()
                .getValueInt("cliente.numero");
                                                
        getTab().setBaseCondition(
            "${cliente.numero} = " + numeroCliente +
            " and ${entregado} = true and ${factura} is null"
        );
    }
	public String getNextController() {
        return "AnyadirPedidosAFactura";
    }
}
