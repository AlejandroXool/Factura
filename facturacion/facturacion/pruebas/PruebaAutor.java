package com.tuempresa.facturacion.pruebas;
 
import org.openxava.tests.*;
 
public class PruebaAutor extends ModuleTestBase {
 
    public PruebaAutor(String nombrePrueba) {
        super(nombrePrueba, "Facturacion", "Autor");
    }
 
    public void testReadAuthor() throws Exception {
        login("admin", "admin");
        assertValueInList(0, 0, "JAVIER CORCOBADO"); 
        execute("List.viewDetail", "row=0"); 
        assertValue("nombre", "JAVIER CORCOBADO");
        assertCollectionRowCount("productos", 2); 
        assertValueInCollection("productos", 0, "numero", "2"); 
        assertValueInCollection("productos", 0, "descripcion", "Arco iris de lágrimas");
        assertValueInCollection("productos", 1, "numero", "3");
        assertValueInCollection("productos", 1, "descripcion", "Ritmo de sangre");
    }
 
}

