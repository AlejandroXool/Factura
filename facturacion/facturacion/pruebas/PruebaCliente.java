package com.tuempresa.facturacion.pruebas;
 
import org.openxava.tests.*;
 
public class PruebaCliente extends ModuleTestBase { 
 
    public PruebaCliente(String nombrePrueba) {
        super(nombrePrueba, "Facturacion", "Cliente"); 
    }
 
    public void testCrearLeerActualizarBorrar() throws Exception {
        login("admin", "admin"); 
 
        // Crear
        execute("CRUD.new"); 
        setValue("numero", "77"); 
        setValue("nombre", "Cliente JUNIT"); 
        setValue("direccion.viaPublica", "Calle JUNIT");                                                
        setValue("direccion.codigoPostal", "77555"); 
        setValue("direccion.municipio", "La ciudad JUNIT"); 
        setValue("direccion.provincia", "La provincia JUNIT"); 
        execute("CRUD.save"); 
        assertNoErrors(); 
        assertValue("numero", ""); 
        assertValue("nombre", ""); 
        assertValue("direccion.viaPublica", ""); 
        assertValue("direccion.codigoPostal", ""); 
        assertValue("direccion.municipio", ""); 
        assertValue("direccion.provincia", ""); 
 
        // Leer
        setValue("numero", "77"); 
        execute("CRUD.refresh"); 
        assertValue("numero", "77"); 
        assertValue("nombre", "Cliente JUNIT"); 
        assertValue("direccion.viaPublica", "Calle JUNIT"); 
        assertValue("direccion.codigoPostal", "77555"); 
        assertValue("direccion.municipio", "La ciudad JUNIT");
        assertValue("direccion.provincia", "La provincia JUNIT");
 
        // Actualizar
        setValue("nombre", "Cliente JUNIT MODIFICADO");
        execute("CRUD.save"); 
        assertNoErrors(); 
        assertValue("numero", ""); 
        assertValue("nombre", ""); 
 
        // Verifica si se ha modificado
        setValue("numero", "77"); 
        execute("CRUD.refresh"); 
        assertValue("numero", "77"); 
        assertValue("nombre", "Cliente JUNIT MODIFICADO"); 
                                                        
        // Borrar
        execute("Facturacion.delete"); 
        assertMessage("Cliente borrado satisfactoriamente"); 
    }
 
}

