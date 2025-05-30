// Constantes globales
const CATEGORY_TRANSLATIONS = {
    flight: 'Vuelo',
    hotels: 'Alojamiento',
    transport: 'Transporte',
    other: 'Gastos varios'
};

// Funciones auxiliares
function showAddExpenseModal(category) {
    const modal = document.getElementById('expense-modal');
    const form = document.getElementById('expense-form');
    const modalTitle = modal.querySelector('.modal-header h3');

    modalTitle.textContent = `Añadir gasto de ${CATEGORY_TRANSLATIONS[category]}`;
    document.getElementById('expense-category').value = category.toUpperCase();
    modal.classList.add('show');
}

function hideAddExpenseModal() {
    const modal = document.getElementById('expense-modal');
    modal.classList.remove('show');
    document.getElementById('expense-form').reset();
}

// Función principal de inicialización
function initExpenses() {
    // Añadir event listeners a todos los botones de añadir gasto
    document.querySelectorAll('.add-expense-icon').forEach(button => {
        button.addEventListener('click', (e) => {
            const category = e.currentTarget.dataset.category;
            showAddExpenseModal(category);
        });
    });

    // Event listener para cerrar el modal
    document.querySelector('.expense-modal .close').addEventListener('click', hideAddExpenseModal);
}

// Evento de carga del DOM
document.addEventListener('DOMContentLoaded', initExpenses);
