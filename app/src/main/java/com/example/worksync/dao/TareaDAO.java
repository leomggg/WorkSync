package com.example.worksync.dao;

import com.example.worksync.config.MongoConfig;
import com.example.worksync.model.Tarea;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
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
                .append("idEmpleadoRelacional", tarea.getIdEmpleadoRelacional())
                .append("completada", tarea.isCompletada())
                .append("prioridad", tarea.getPrioridad());
        collection.insertOne(doc);
    }

    public List<Tarea> listarPorEmpleado(int idEmpleado) {
        List<Tarea> tareas = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("idEmpleadoRelacional", idEmpleado))) {
            tareas.add(new Tarea(
                    doc.getObjectId("_id").toString(),
                    doc.getString("titulo"),
                    doc.getString("descripcion"),
                    doc.getInteger("idEmpleadoRelacional"),
                    doc.containsKey("completada") ? doc.getBoolean("completada") : false,
                    doc.containsKey("prioridad") ? doc.getString("prioridad") : "Media"
            ));
        }
        return tareas;
    }

    public void actualizarEstado(String idMongo, boolean completada) {
        collection.updateOne(Filters.eq("_id", new ObjectId(idMongo)), 
                Updates.set("completada", completada));
    }
}