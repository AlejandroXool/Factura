package com.tuempresa.facturacion.acciones;

import org.openxava.actions.*;

public class GrabarFactura extends SaveAction{

	public void execute() throws Exception {
        super.execute();
        closeDialog();
    }
}
