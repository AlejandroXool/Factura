package com.tuempresa.facturacion.acciones;
 
import java.util.*;

import javax.ejb.*;

import org.openxava.actions.*;
 
public class BuscarExcluyendoEliminados
    // extends SearchByViewKeyAction { 
	extends SearchExecutingOnChangeAction {
 
    private boolean esEliminable() { 
        return getView().getMetaModel()
            .containsMetaProperty("eliminado");
    }
 
    protected Map getValuesFromView() 
        throws Exception 
    {
        if (!esEliminable()) { 
            return super.getValuesFromView();
        }
        Map<String, Object> valores = super.getValuesFromView();
        valores.put("eliminado", false) ; 
        return valores;
    }
 
    protected Map getMemberNames() 
        throws Exception
    {
        if (!esEliminable()) { 
            return super.getMemberNames();
        }
        Map<String, Object> miembros = super.getMemberNames();
        miembros.put("eliminado", null); 
        return miembros; 
    }
 
    protected void setValuesToView(Map valores) 
        throws Exception 
    {
        if (esEliminable() && 
            (Boolean) valores.get("eliminado")) { 
            throw new ObjectNotFoundException(); 
        }
        else {
            super.setValuesToView(valores); 
        }
    }
}

