package com.tuempresa.facturacion.acciones;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

import com.tuempresa.facturacion.modelo.*;

public class AnyadirPedidosAFactura extends AddElementsToCollectionAction{

	public void execute() throws Exception {
        super.execute();
        getView().refresh();
    }

    protected void associateEntity(Map clave)
        throws ValidationException,
            XavaException, ObjectNotFoundException,
            FinderException, RemoteException
    {
        super.associateEntity(clave);
        Pedido pedido = (Pedido) MapFacade.findEntity("Pedido", clave);
        pedido.copiarDetallesAFactura();
    }
}
