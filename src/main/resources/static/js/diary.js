/*Funcion que me ayuda a conectarme con el back
  para editar y guardar la descripcion de una anecdota*/ 

// Constantes globales
const TOAST_DELAY = 2000;

// Funciones auxiliares
function mostrarToast() {
	const toastElement = document.getElementById('toastSuccess');
	const toast = new bootstrap.Toast(toastElement, { delay: TOAST_DELAY });
	toast.show();
}

function editarDescripcion(button) {
	const storyId = button.getAttribute("data-id");
	const descripcionElem = document.getElementById("descripcion-" + storyId);

	descripcionElem.contentEditable = true;
	descripcionElem.focus();

	button.nextElementSibling.classList.remove("d-none"); // Muestra guardar
}

async function guardarDescripcion(button, countryName, tripId) {
	const storyId = button.getAttribute("data-id");
	tripId = button.getAttribute("data-trip");
	const descripcion = document.getElementById("descripcion-" + storyId).innerText;

	const params = new URLSearchParams();
	params.append("storyId", storyId);
	params.append("descripcion", descripcion);
	params.append("countryName", countryName);
	params.append("tripId", tripId);

	try {
		const response = await fetch('/stories/actualizar-descripcion', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			body: params
		});

		if (response.ok) {
			mostrarToast();
		} else {
			alert("Error al actualizar la descripción.");
		}

		// Bloquea la edición
		const descripcionElem = document.getElementById("descripcion-" + storyId);
		descripcionElem.contentEditable = false;

		// Muestra botón de editar
		button.classList.add("d-none");
		button.previousElementSibling.classList.remove("d-none");
	} catch (error) {
		console.error("Error al guardar la descripción:", error);
		alert("Error al actualizar la descripción.");
	}
}

// Función principal de inicialización
function initStory() {
	// Aquí puedes agregar cualquier inicialización adicional si es necesaria
	console.log("Módulo de historias inicializado");
}

// Evento de carga del DOM
document.addEventListener('DOMContentLoaded', initStory);
