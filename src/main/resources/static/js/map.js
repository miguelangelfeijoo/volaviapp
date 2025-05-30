// Constantes globales
//const BASE_URL = 'http://localhost:9000/';
const BASE_URL = 'https://volaviapp.onrender.com/';
const MAX_INITIALIZATION_ATTEMPTS = 3;
const MAP_STYLES = {
	default: {
		fillColor: '#ccc',
		weight: 1,
		opacity: 1,
		color: 'white',
		fillOpacity: 0.7
	},
	visited: {
		fillColor: '#3d2352',
		weight: 1,
		opacity: 1,
		color: 'white',
		fillOpacity: 0.7
	},
	hover: {
		weight: 2,
		color: '#ffffff',
		fillColor: '#6bc0bb',
		fillOpacity: 0.8
	}
};

// Variables globales
let map = null;
let visitedCountries = new Set();
let countryLayers = {};
let initializationAttempts = 0;

// Funciones auxiliares
function setupMap() {
	if (map) {
		map.remove();
	}

	const mapContainer = document.getElementById('map');
	if (!mapContainer) {
		console.error('Map container not found');
		return;
	}

	mapContainer.style.display = 'block';
	mapContainer.style.height = '100vh';

	map = L.map('map', {
		minZoom: 2,
		maxZoom: 10,
		zoomControl: true,
		scrollWheelZoom: true
	}).setView([0, 0], 2);

	L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
	}).addTo(map);

	invalidateMapSize();
	loadCountriesData();
}

async function loadVisitedCountries() {
	try {
		const response = await fetch(`${BASE_URL}map/visitados`);
		const visits = await response.json();
		visitedCountries = new Set(visits.map(visit => visit.countryName));
		updateMapColors();
	} catch (error) {
		console.error('Error loading visited countries:', error);
	}
}

function updateMapColors() {
	for (const [countryName, layer] of Object.entries(countryLayers)) {
		layer.setStyle({
			fillColor: visitedCountries.has(countryName) ? MAP_STYLES.visited.fillColor : MAP_STYLES.default.fillColor
		});
	}
}

function invalidateMapSize() {
	if (!map) return;

	map.invalidateSize();

	setTimeout(() => {
		map.invalidateSize();

		if (initializationAttempts < MAX_INITIALIZATION_ATTEMPTS) {
			initializationAttempts++;
			invalidateMapSize();
		}
	}, 500);
}

async function loadCountriesData() {
	try {
		const response = await fetch('https://raw.githubusercontent.com/nvkelso/natural-earth-vector/master/geojson/ne_10m_admin_0_countries_lakes.geojson');
		const data = await response.json();
		
		data.features.forEach(country => {
			const countryName = country.properties.NAME_ES || country.properties.NAME;

			const countryLayer = L.geoJSON(country.geometry, {
				style: () => ({
					...MAP_STYLES.default,
					fillColor: visitedCountries.has(countryName) ? MAP_STYLES.visited.fillColor : MAP_STYLES.default.fillColor
				})
			});

			countryLayer.on('click', () => {
				window.location.href = `/trips/plan-your-trip?name=${encodeURIComponent(countryName)}`;
			});

			countryLayer.on('mouseover', (e) => {
				const layer = e.target;
				layer.setStyle(MAP_STYLES.hover);
				layer.bindTooltip(countryName).openTooltip();
			});

			countryLayer.on('mouseout', (e) => {
				const layer = e.target;
				const isVisited = visitedCountries.has(countryName);
				layer.setStyle({
					...MAP_STYLES.default,
					fillColor: isVisited ? MAP_STYLES.visited.fillColor : MAP_STYLES.default.fillColor
				});
				layer.closeTooltip();
			});

			countryLayers[countryName] = countryLayer;
			countryLayer.addTo(map);
		});
	} catch (error) {
		console.error('Error loading GeoJSON:', error);
	}
}

// Función principal de inicialización
function initMap() {
	if (document.readyState === 'loading') {
		document.addEventListener('DOMContentLoaded', setupMap);
	} else {
		setupMap();
	}
	loadVisitedCountries();
}

// Eventos
document.addEventListener('DOMContentLoaded', initMap);

window.addEventListener('resize', () => {
	if (map) {
		map.invalidateSize();
	}
});
