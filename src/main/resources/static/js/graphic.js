// Constantes globales
const CHART_LABELS = ['Vuelos', 'Alojamiento', 'Transporte', 'Varios'];
const CHART_COLORS = [
	'rgba(214, 185, 128, 0.8)',
	'rgba(125, 181, 97, 0.7)',
	'rgba(178, 115, 188, 0.7)',
	'rgba(75, 192, 192, 0.7)'
];

// Variables globales
let chart = null;

// Funciones auxiliares
async function fetchData(tripId) {
	try {
		const response = await fetch(`/trips/valores-grafico?tripId=${tripId}`);
		if (!response.ok) throw new Error("Error en la petición");
		const data = await response.json();
		return data;
	} catch (error) {
		console.error("Error al obtener los datos:", error);
		return [0, 0, 0, 0];
	}
}

function renderChart(canvasId, valores) {
	const ctx = document.getElementById(canvasId).getContext('2d');

	chart = new Chart(ctx, {
		type: 'pie',
		data: {
			labels: CHART_LABELS,
			datasets: [{
				label: 'Gastos en €',
				data: valores,
				backgroundColor: CHART_COLORS,
				borderColor: 'white',
				borderWidth: 2
			}]
		},
		options: {
			responsive: true,
			plugins: {
				legend: {
					position: 'bottom'
				},
				tooltip: {
					callbacks: {
						label: function(context) {
							const dataset = context.dataset;
							const total = dataset.data.reduce((sum, val) => sum + val, 0);
							const valor = context.parsed;
							const porcentaje = ((valor / total) * 100).toFixed(1);
							return `${context.label}: €${valor} (${porcentaje}%)`;
						}
					}
				}
			}
		}
	});
}

// Función principal de inicialización
async function initGraphic() {
	const tripId = document.getElementById('trip-id').dataset.tripId;
	const valores = await fetchData(tripId);
	renderChart('mi-grafico', valores);
}

// Evento de carga del DOM
document.addEventListener('DOMContentLoaded', initGraphic);
