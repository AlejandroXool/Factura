package com.tuempresa.facturacion.acciones;
 
import java.util.*;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;

import lombok.*;
 
public class EliminarSeleccionadoParaFacturacion
    extends TabBaseAction 
    implements IChainActionWithArgv { 
 
    private String nextAction = null; 
    
    @Getter @Setter
    boolean restaurar; 
    
    public void execute() throws Exception {
        if (!getMetaModel().containsMetaProperty("eliminado")) {
            nextAction="CRUD.deleteSelected"; 
            return;
        }
        marcarEntidadesSeleccionadasComoEliminadas(); 
    }
 
    private MetaModel getMetaModel() {
        return MetaModel.get(getTab().getModelName());
    }
 
    public String getNextAction() throws Exception {
        return nextAction; 
    }
 
    public String getNextActionArgv() throws Exception {
        return "row=" + getRow(); // Argumento a enviar a la la acción encadenada
    }
 
    private void marcarEntidadesSeleccionadasComoEliminadas() throws Exception {
        Map<String, Object> valores = new HashMap<>();      
        valores.put("eliminado", !isRestaurar()); 
        Map<String, Object>[] clavesSeleccionadas = getSelectedKeys(); 
        if (clavesSeleccionadas != null) {
            for (int i = 0; i < clavesSeleccionadas.length; i++) { 
                Map<String, Object> clave = clavesSeleccionadas[i]; 
                try {
                    MapFacade.setValues( 
                        getTab().getModelName(),
                        clave,
                        valores);
                }
                catch (javax.validation.ValidationException ex) { 
                    addError("no_delete_row", i, clave);
                    addError("remove_error", getTab().getModelName(), ex.getMessage()); 
                }
                catch (Exception ex) { 
                    addError("no_delete_row", i, clave); 
                }
            }
        }
        getTab().deselectAll(); 
        resetDescriptionsCache(); 
    }

}
