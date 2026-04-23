package com.ejercicios.resource;

import com.ejercicios.dto.ProductoDTO;
import com.ejercicios.entity.Producto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.core.Context;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Path("/api/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoResource {

    private static final List<Producto> productos = new ArrayList<>();
    private static final AtomicLong ids = new AtomicLong(1);

    static {
        productos.add(new Producto("Laptop", 999.99));
        productos.add(new Producto("Mouse", 25.50));
        productos.add(new Producto("Teclado", 45.00));
    }

    @GET
    public List<ProductoDTO> listarTodos() {
        return productos.stream()
                .map(p -> new ProductoDTO(p.getId(), p.getNombre(), p.getPrecio()))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(p -> Response.ok(new ProductoDTO(p.getId(), p.getNombre(), p.getPrecio())).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response crear(ProductoDTO dto) {
        Producto producto = new Producto(dto.getNombre(), dto.getPrecio());
        producto.setId(ids.getAndIncrement());
        productos.add(producto);
        return Response.status(Response.Status.CREATED)
                .entity(new ProductoDTO(producto.getId(), producto.getNombre(), producto.getPrecio()))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, ProductoDTO dto) {
        for (Producto p : productos) {
            if (p.getId().equals(id)) {
                p.setNombre(dto.getNombre());
                p.setPrecio(dto.getPrecio());
                return Response.ok(new ProductoDTO(p.getId(), p.getNombre(), p.getPrecio())).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        boolean eliminado = productos.removeIf(p -> p.getId().equals(id));
        if (eliminado) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}