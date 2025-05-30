// Constantes globales
const ALLOWED_FILE_TYPES = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];

// Variables globales
let uploadArea;
let uploadMessage;

// Funciones auxiliares
function handleImageUpload(event) {
    const file = event.target.files[0];
    
    if (file && file.type.startsWith('image/')) {
        const reader = new FileReader();

        reader.onload = function(e) {
            // Elimina cualquier imagen previa
            const prevImage = uploadArea.querySelector('img');
            if (prevImage) uploadArea.removeChild(prevImage);

            // Oculta el mensaje
            uploadMessage.style.display = 'none';

            // Crea y añade la imagen
            const img = document.createElement('img');
            img.src = e.target.result;
            img.alt = 'Vista previa';
            uploadArea.appendChild(img);
        };

        reader.readAsDataURL(file);
    }
}

// Función principal de inicialización
function initAddStory() {
    // Obtener referencias a los elementos del DOM
    const fileInput = document.getElementById('visit-images');
    uploadArea = document.getElementById('upload-area');
    uploadMessage = uploadArea.querySelector('.upload-message');

    // Añadir event listener para la carga de imágenes
    if (fileInput) {
        fileInput.addEventListener('change', handleImageUpload);
    }
}

// Evento de carga del DOM
document.addEventListener('DOMContentLoaded', initAddStory); 