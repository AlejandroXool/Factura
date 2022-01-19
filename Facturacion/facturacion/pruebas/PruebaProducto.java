package com.tuempresa.facturacion.pruebas;
 
import static org.openxava.jpa.XPersistence.commit;
import static org.openxava.jpa.XPersistence.getManager;

import java.math.*;

import org.openxava.tests.*;

import com.tuempresa.facturacion.modelo.*;
 
public class PruebaProducto extends ModuleTestBase {
 
    private Autor autor; 
    private Categoria categoria; 
    private Producto producto1; 
    private Producto producto2; 
 
    public PruebaProducto(String testName) {
        super(testName, "Facturacion", "Producto");
    }
 
    protected void setUp() throws Exception { 
        super.setUp(); 
        crearProductos(); 
    }
 
    protected void tearDown() throws Exception {                                                  
        super.tearDown(); 
        borrarProductos(); 
    }
 
    public void testBorrarDesdeLista() throws Exception {
        login("admin", "admin");
        setConditionValues("", "JUNIT"); 
        setConditionComparators("=", "contains_comparator"); 
        execute("List.filter"); 
        assertListRowCount(2); 
        checkRow(1); 
        execute("Facturacion.deleteSelected"); 
        assertListRowCount(1); 
    }
 
    public void testSubirFotos() throws Exception { 
    	login("admin", "admin");
     
    	// Buscar producto1
    	execute("CRUD.new");
    	setValue("numero", Integer.toString(producto1.getNumero())); 
    	execute("CRUD.refresh");
    	assertFilesCount("fotos", 0);
     
    	// Subir fotos
    	uploadFile("fotos", "web/xava/images/add.gif"); 
    	uploadFile("fotos", "web/xava/images/attach.gif"); 
     
    	// Verificar
    	execute("CRUD.new");
    	assertFilesCount("fotos", 0);
    	setValue("numero", Integer.toString(producto1.getNumero())); 
    	execute("CRUD.refresh");
    	assertFilesCount("fotos", 2);
    	assertFile("fotos", 0, "image");
    	assertFile("fotos", 1, "image");
    	
    	// Quitar fotos
    	removeFile("fotos", 1);
    	removeFile("fotos", 0);
    }
    
    public void testValidarISBN() throws Exception {
        login("admin", "admin");
     
        // Buscar producto1
        execute("CRUD.new");
        setValue("numero", Integer.toString(producto1.getNumero()));
        execute("CRUD.refresh");
        assertValue("descripcion", "Producto JUNIT 1");
        assertValue("isbn", "");
     
        // Con un formato de ISBN incorrecto
        setValue("isbn", "1111");
        execute("CRUD.save"); 
        assertError("1111 no es un valor válido para ISBN de Producto: " +
            "ISBN inválido o inexistente");
     
        // ISBN no existe aunque tiene un formato correcto
        setValue("isbn", "9791034369997");
        execute("CRUD.save"); 
        assertError("9791034369997 no es un valor válido para ISBN de " +
            "Producto: ISBN inválido o inexistente");
     
        // ISBN existe
        setValue("isbn", "9780932633439");
        execute("CRUD.save"); 
        assertNoErrors();
    }
 
    private void crearProductos() {
        // Crear objetos Java
        autor = new Autor(); 
        autor.setNombre("JUNIT Author"); 
        categoria = new Categoria();
        categoria.setDescripcion("Categoria JUNIT");
        producto1 = new Producto();
        producto1.setNumero(900000001);
        producto1.setDescripcion("Producto JUNIT 1");
        producto1.setAutor(autor);
        producto1.setCategoria(categoria);
        producto1.setPrecio(new BigDecimal("10"));
        producto2 = new Producto();
        producto2.setNumero(900000002);
        producto2.setDescripcion("Producto JUNIT 2");
        producto2.setAutor(autor);
        producto2.setCategoria(categoria);
        producto2.setPrecio(new BigDecimal("20"));
     
        // Marcar los objetos como persistentes
        getManager().persist(autor); 
        getManager().persist(categoria); 
        getManager().persist(producto1); 
        getManager().persist(producto2);
     
        // Confirma los cambios en la base de datos
        commit(); 
    }
 
    private void borrarProductos() { 
		borrar(producto1, producto2, autor, categoria); 
		commit(); 
	}
	
	private void borrar(Object ... entidades) { 
		for (Object entidad : entidades) { 
			getManager().remove(getManager().merge(entidad)); 
		}
	}

}
