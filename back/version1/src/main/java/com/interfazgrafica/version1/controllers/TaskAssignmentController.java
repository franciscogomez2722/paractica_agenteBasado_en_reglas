package com.interfazgrafica.version1.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskAssignmentController {

    // Lista de miembros del equipo con su carga de trabajo (por ejemplo, número de tareas asignadas)
    private static List<Member> members = new ArrayList<>();
    
    static {
        members.add(new Member("Alice", 1, "Developer"));
        members.add(new Member("Bob", 2, "Developer"));
        members.add(new Member("Charlie", 0, "Developer"));
    }

    // Asignación de tareas
    @GetMapping("/assignTask")
    public ResponseEntity<Object> assignTask(@RequestParam String taskPriority, @RequestParam String taskType) {
        // Asignación de tareas
        Member selectedMember = selectMember(taskPriority, taskType);
        
        if (selectedMember != null) {
            selectedMember.addTask();  // Incrementa la carga de trabajo del miembro asignado
            TaskResponse response = new TaskResponse(selectedMember.getName(), selectedMember.getRole(), taskPriority, taskType);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ErrorResponse errorResponse = new ErrorResponse("No hay miembros disponibles para asignar la tarea.");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    private Member selectMember(String taskPriority, String taskType) {
        // Regla 1: Asignar tarea a la persona que no tenga tarea asignada
        for (Member member : members) {
            if (member.getTaskCount() == 0) {
                return member;  // Asigna la tarea a la persona que no tiene tareas
            }
        }

        // Regla 2: Si todos tienen tareas asignadas, asigna una tarea a cualquiera que tenga menos de 4 tareas
        for (Member member : members) {
            if (member.getTaskCount() < 4) {
                return member;  // Asigna una tarea a alguien que tiene menos de 4 tareas
            }
        }

        return null;  // Si todos están ocupados con 4 o más tareas
    }

    // Clase interna para representar a los miembros del equipo
    static class Member {
        private String name;
        private int taskCount;
        private String role;

        public Member(String name, int taskCount, String role) {
            this.name = name;
            this.taskCount = taskCount;
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public int getTaskCount() {
            return taskCount;
        }

        public String getRole() {
            return role;
        }

        public void addTask() {
            this.taskCount++;
        }
    }

    // Respuesta exitosa
    static class TaskResponse {
        private String employee;
        private String role;
        private String priority;
        private String type;

        public TaskResponse(String employee, String role, String priority, String type) {
            this.employee = employee;
            this.role = role;
            this.priority = priority;
            this.type = type;
        }

        public String getEmployee() {
            return employee;
        }

        public String getRole() {
            return role;
        }

        public String getPriority() {
            return priority;
        }

        public String getType() {
            return type;
        }
    }

    // Respuesta de error
    static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}
