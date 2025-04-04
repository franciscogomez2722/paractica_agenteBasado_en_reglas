import React, { useState } from 'react';
import './TaskAssignment.css'; // Importa el archivo CSS

const TaskAssignment = () => {
    const [taskPriority, setTaskPriority] = useState('');
    const [taskType, setTaskType] = useState('');
    const [assignedTasks, setAssignedTasks] = useState([]);
    const [message, setMessage] = useState('');

    const handleAssignTask = () => {
        fetch(`http://localhost:8080/api/assignTask?taskPriority=${taskPriority}&taskType=${taskType}`)
            .then(response => response.json()) // Convierte la respuesta a JSON
            .then(data => {
                // Imprime el JSON en la consola
                console.log("Respuesta del backend:", data);

                // Si la respuesta contiene un mensaje de error, no agregamos la tarea
                if (data.error) {
                    setMessage(data.error);
                    return; // No continuar con el agregado de la tarea
                }

                // Suponiendo que el backend devuelve un objeto con la estructura deseada
                const taskData = {
                    employee: data.employee,  // Nombre del empleado
                    role: data.role,  // Rol del empleado
                    priority: taskPriority,  // Prioridad de la tarea
                    type: taskType  // Tipo de tarea
                };
                setAssignedTasks(prevTasks => [...prevTasks, taskData]);  // Agregar tarea asignada
                setMessage('Tarea asignada exitosamente');
            })
            .catch(error => {
                console.error('Error:', error);
                setMessage('Hubo un error al asignar la tarea');
            });
    };

    return (
        <div className="task-container">
            <h1 className="heading">Asignación de Tareas al Equipo</h1>

            <div className="form-group">
                <label className="label">
                    Prioridad de la tarea:
                    <select
                        className="select"
                        onChange={e => setTaskPriority(e.target.value)}
                        value={taskPriority}
                    >
                        <option value="">Seleccionar</option>
                        <option value="high">Alta</option>
                        <option value="low">Baja</option>
                    </select>
                </label>
            </div>

            <div className="form-group">
                <label className="label">
                    Tipo de tarea:
                    <select
                        className="select"
                        onChange={e => setTaskType(e.target.value)}
                        value={taskType}
                    >
                        <option value="">Seleccionar</option>
                        <option value="Development">Desarrollo</option>
                        <option value="Design">Diseño</option>
                    </select>
                </label>
            </div>

            <button className="button" onClick={handleAssignTask}>Asignar Tarea</button>

            <p className="message">{message}</p>

            {/* Tabla de tareas asignadas */}
            <h2 className="table-heading">Tareas Asignadas</h2>
            <table className="task-table">
                <thead>
                    <tr>
                        <th className="table-header">Empleado</th>
                        <th className="table-header">Rol</th>
                        <th className="table-header">Prioridad</th>
                        <th className="table-header">Tipo de Tarea</th>
                    </tr>
                </thead>
                <tbody>
                    {assignedTasks.length > 0 ? assignedTasks.map((task, index) => (
                        <tr key={index}>
                            <td className="table-cell">{task.employee}</td>
                            <td className="table-cell">{task.role}</td>
                            <td className="table-cell">{task.priority}</td>
                            <td className="table-cell">{task.type}</td>
                        </tr>
                    )) : (
                        <tr>
                            <td colSpan="4" className="table-cell">No hay tareas asignadas</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default TaskAssignment;
