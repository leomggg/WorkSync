package com.example.worksync.model;

import java.io.Serializable;

public class Tarea implements Serializable {
    private String idMongo;
    private String titulo;
    private String descripcion;
    private int idEmpleadoRelacional;
    private boolean completada;
    private String prioridad; // "Alta", "Media", "Baja"

    public Tarea(String idMongo, String titulo, String descripcion, int idEmpleadoRelacional) {
        this(idMongo, titulo, descripcion, idEmpleadoRelacional, false, "Media");
    }

    public Tarea(String idMongo, String titulo, String descripcion, int idEmpleadoRelacional, boolean completada, String prioridad) {
        this.idMongo = idMongo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idEmpleadoRelacional = idEmpleadoRelacional;
        this.completada = completada;
        this.prioridad = prioridad;
    }

    public String getIdMongo() { return idMongo; }
    public void setIdMongo(String idMongo) { this.idMongo = idMongo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getIdEmpleadoRelacional() { return idEmpleadoRelacional; }
    public void setIdEmpleadoRelacional(int idEmpleadoRelacional) { this.idEmpleadoRelacional = idEmpleadoRelacional; }
    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { this.completada = completada; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
}