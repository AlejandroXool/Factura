package com.tuempresa.facturacion.pruebas;
 
public class PruebaPedido extends PruebaDocumentoComercial {
 
    public PruebaPedido(String nombrePrueba) {
        super(nombrePrueba, "Pedido");
    }
    
    public void testPonerFactura() throws Exception {
        login("admin", "admin");
        assertListNotEmpty();
        execute("List.orderBy", "property=numero"); 
        execute("List.viewDetail", "row=0");
        assertValue("entregado", "false"); 
        execute("Sections.change", "activeSection=1");
        assertValue("factura.numero", "");
        assertValue("factura.anyo", "");
        execute("Reference.search",
            "keyProperty=factura.anyo");
        String anyo = getValueInList(0, "anyo");
        String numero = getValueInList(0, "numero");
        execute("ReferenceSearch.choose", "row=0");
        assertValue("factura.anyo", anyo);
        assertValue("factura.numero", numero);
     
        // Los pedidos no entregados no pueden tener factura
        execute("CRUD.save");
        assertErrorsCount(1); 
        setValue("entregado", "true");
        execute("CRUD.save"); 
        assertNoErrors();
     
        // Un pedido con factura no se puede borrar
        execute("Mode.list"); 
        execute("Facturacion.deleteRow", "row=0"); 
        assertError("Imposible borrar Pedido por: " + 
            "Pedido asociado a factura no puede ser eliminado"); 
     
        // Restaurar los valores originales
        execute("List.viewDetail", "row=0");
        setValue("factura.anyo", "");
        setValue("entregado", "false");
        execute("CRUD.save");
        assertNoErrors();
    }
    
    public void testDiasEntrega() throws Exception {
        login("admin", "admin");
        assertListNotEmpty(); 
        execute("List.viewDetail", "row=0"); 
    	
        setValue("fecha", "5/6/2020");
        assertValue("diasEntregaEstimados", "1");
        setValue("fecha", "6/6/2020");
        assertValue("diasEntregaEstimados", "3");
        setValue("fecha", "7/6/2020");
        assertValue("diasEntregaEstimados", "2");
        execute("CRUD.save");
        execute("Mode.list"); 
        assertValueInList(0, "diasEntrega", "2"); 

        execute("List.viewDetail", "row=0");
        setValue("fecha", "13/1/2020");
        assertValue("diasEntregaEstimados", "7");
        execute("CRUD.save");
        execute("Mode.list"); 
        assertValueInList(0, "diasEntrega", "7");        
    }
 
}
