package com.ejercicios.json;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoJsonService {

    public String crearJsonProducto(String nombre, double precio, String categoria, boolean disponible) {
        JsonObject producto = Json.createObjectBuilder()
                .add("nombre", nombre)
                .add("precio", precio)
                .add("categoria", categoria)
                .add("disponible", disponible)
                .add("fechaRegistro", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
        
        return producto.toString();
    }

    public String crearJsonListaProductos(List<Map<String, Object>> productos) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (Map<String, Object> prod : productos) {
            JsonObject producto = Json.createObjectBuilder()
                    .add("id", (Integer) prod.get("id"))
                    .add("nombre", (String) prod.get("nombre"))
                    .add("precio", (Double) prod.get("precio"))
                    .add("categoria", (String) prod.get("categoria"))
                    .add("disponible", (Boolean) prod.get("disponible"))
                    .build();
            arrayBuilder.add(producto);
        }
        
        JsonArray jsonArray = arrayBuilder.build();
        return jsonArray.toString();
    }

    public Map<String, Object> parsearJsonProducto(String jsonString) {
        JsonObject jsonObject = Json.createReader(new java.io.StringReader(jsonString)).readObject();
        
        Map<String, Object> producto = new HashMap<>();
        producto.put("nombre", jsonObject.getString("nombre"));
        producto.put("precio", jsonObject.getJsonNumber("precio").doubleValue());
        producto.put("categoria", jsonObject.getString("categoria"));
        producto.put("disponible", jsonObject.getBoolean("disponible"));
        
        return producto;
    }

    public String crearJsonRespuesta(String status, String mensaje, Object data) {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("status", status)
                .add("mensaje", mensaje)
                .add("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        if (data instanceof JsonObject) {
            builder.add("data", (JsonObject) data);
        } else if (data instanceof JsonArray) {
            builder.add("data", (JsonArray) data);
        } else {
            builder.add("data", JsonValue.NULL);
        }
        
        return builder.build().toString();
    }

    public String crearJsonAnidado() {
        JsonObject direccion = Json.createObjectBuilder()
                .add("calle", "Av. Principal")
                .add("ciudad", "Madrid")
                .add("pais", "España")
                .add("codigoPostal", "28001")
                .build();

        JsonObject proveedor = Json.createObjectBuilder()
                .add("nombre", "Acme Corp")
                .add("telefono", "+34 900 123 456")
                .add("email", "contacto@acme.com")
                .build();

        JsonObject producto = Json.createObjectBuilder()
                .add("id", 1)
                .add("nombre", "Laptop Pro")
                .add("precio", 1299.99)
                .add("categoria", "Electrónica")
                .add("direccionEnvio", direccion)
                .add("proveedor", proveedor)
                .add("etiquetas", Json.createArrayBuilder()
                        .add("promocion")
                        .add("nuevo")
                        .add("envio-gratis"))
                .build();

        return producto.toString();
    }
}