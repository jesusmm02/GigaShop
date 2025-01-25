document.addEventListener('DOMContentLoaded', function() {
    const avatarInput = document.getElementById("avatar");
    const errorDiv = document.getElementById("avatarError");

    avatarInput.addEventListener('change', function(event) {
        const file = event.target.files[0];
        
        // Validaciones
        const allowedTypes = ['image/png', 'image/jpg', 'image/jpeg'];
        const maxSizeKB = 100;

        // Limpiar mensaje de error previo
        errorDiv.textContent = '';

        // Comprobar tipo de archivo
        if (!allowedTypes.includes(file.type)) {
            errorDiv.textContent = 'Solo se permiten archivos PNG o JPG';
            avatarInput.value = ''; // Limpiar selección
            return;
        }

        // Comprobar tamaño de archivo
        const fileSizeKB = file.size / 1024;
        if (fileSizeKB > maxSizeKB) {
            errorDiv.textContent = 'El archivo no puede superar 100 KB';
            avatarInput.value = ''; // Limpiar selección
            return;
        }
    });
});


