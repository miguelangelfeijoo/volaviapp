/*Funcion que me permite desahilitar los días anteriores a la fecha
  actual para que no le permita seleccionar un fecha anterior*/ 

// Constantes globales
const hoy = new Date();
const año = hoy.getFullYear();
const mes = String(hoy.getMonth() + 1).padStart(2, '0');
const dia = String(hoy.getDate()).padStart(2, '0');
const FECHA_MINIMA = `${año}-${mes}-${dia}`;

// Variables globales
let fechaSalida;
let fechaLlegada;
let countdownSpan;
let tripId;

// Funciones auxiliares
function getStorageKey(field) {
    return `trip_${tripId}_${field}`;
}

function verificarFechaLlegada() {
    const fechaSeleccionada = fechaSalida.value;
    
    if (fechaSeleccionada) {
        fechaLlegada.disabled = false;
        fechaLlegada.setAttribute('min', fechaSeleccionada);
    } else {
        fechaLlegada.disabled = true;
    }
}

function actualizarDiasRestantes(fechaSeleccionada) {
    const fechaSalida = new Date(fechaSeleccionada);
    const hoy = new Date();

    fechaSalida.setHours(0, 0, 0, 0);
    hoy.setHours(0, 0, 0, 0);

    const diferenciaMs = fechaSalida - hoy;
    const diasRestantes = Math.ceil(diferenciaMs / (1000 * 60 * 60 * 24));
    const textoFinal = !isNaN(diasRestantes) ? (diasRestantes >= 0 ? diasRestantes : "0") : "-";

    countdownSpan.textContent = textoFinal;
    sessionStorage.setItem(getStorageKey("fechaSalida"), fechaSeleccionada);
    sessionStorage.setItem(getStorageKey("diasRestantes"), textoFinal);
}

function cargarFechasGuardadas() {
    const fechaSalidaGuardada = sessionStorage.getItem(getStorageKey("fechaSalida"));
    const fechaLlegadaGuardada = sessionStorage.getItem(getStorageKey("fechaLlegada"));
    const diasGuardados = sessionStorage.getItem(getStorageKey("diasRestantes"));

    if (fechaSalidaGuardada) {
        fechaSalida.value = fechaSalidaGuardada;
        fechaLlegada.disabled = false;
        fechaLlegada.setAttribute('min', fechaSalidaGuardada);
    } else {
        fechaLlegada.disabled = true;
    }

    if (fechaLlegadaGuardada) {
        fechaLlegada.value = fechaLlegadaGuardada;
    }

    if (diasGuardados) {
        countdownSpan.textContent = diasGuardados;
    }
}

// Función principal de inicialización
function initTripDates() {
    fechaSalida = document.getElementById('fechaSalida');
    fechaLlegada = document.getElementById('fechaLlegada');
    countdownSpan = document.querySelector(".timer-display .time-segment span");
    tripId = document.getElementById("trip-id")?.dataset.tripId;

    if (!fechaSalida || !fechaLlegada || !countdownSpan || !tripId) return;

    fechaSalida.setAttribute('min', FECHA_MINIMA);
    verificarFechaLlegada();
    cargarFechasGuardadas();

    // Event listeners
    fechaSalida.addEventListener('change', function() {
        verificarFechaLlegada();
        actualizarDiasRestantes(this.value);
    });

    fechaLlegada.addEventListener('change', function() {
        sessionStorage.setItem(getStorageKey("fechaLlegada"), this.value);
    });
}

// Evento de carga del DOM
document.addEventListener('DOMContentLoaded', initTripDates);
