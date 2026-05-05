package com.example.worksync;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class TareaDAO {
    private final MongoCollection<Document> collection;

    public TareaDAO() {
        MongoDatabase db = MongoConfig.getInstance().getDatabase();
        this.collection = db.getCollection("tareas");
    }

    public void insertar(Tarea tarea) {
        Document doc = new Document("titulo", tarea.getTitulo())
                .append("descripcion", tarea.getDescripcion())
                .append("idEmpleadoRelacional", tarea.getIdEmpleadoRelacional());
        collection.insertOne(doc);
    }

    public List<Tarea> listarPorEmpleado(int idEmpleado) {
        List<Tarea> tareas = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("idEmpleadoRelacional", idEmpleado))) {
            tareas.add(new Tarea(
                    doc.getObjectId("_id").toString(),
                    doc.getString("titulo"),
                    doc.getString("descripcion"),
                    doc.getInteger("idEmpleadoRelacional")
            ));
        }
        return tareas;
    }
}