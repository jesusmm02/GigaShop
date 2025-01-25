const dniEndPoint = "http://localhost:8080/GigaShop/RegistroController"; // Cambiado el nombre de 'URL' a 'dniEndpoint'

const inputDNI = document.getElementById('nif');
console.log(inputDNI);

const parrafoMensaje = document.getElementById('mensaje');

inputDNI.addEventListener('blur', async () => {
    let numeros = inputDNI.value.trim();

    // Limpia cualquier mensaje anterior
    parrafoMensaje.textContent = '';
    parrafoMensaje.className = ''; // Elimina clases previas

    if (numeros === '') {
        parrafoMensaje.textContent = "El campo DNI está vacío.";
        parrafoMensaje.className = "text-danger"; // Estilo para mensajes de error
        return;
    }

    if (numeros.length !== 8 || !/^\d{8}$/.test(numeros)) {
        parrafoMensaje.textContent = "El DNI debe tener exactamente 8 dígitos.";
        parrafoMensaje.className = "text-danger"; // Estilo para mensajes de error
        return;
    }

    console.log("Solicitando letra para DNI:", numeros);

    try {
        const data = new URLSearchParams();
        data.append('accion', 'generarDni');
        data.append('numeros', numeros);

        let response = await fetch(dniEndPoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: data.toString()
        });

        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }

        let resultado = await response.json();
        console.log("Respuesta del servidor:", resultado);

        if (resultado.data) {
            inputDNI.value = numeros + resultado.data;
            parrafoMensaje.textContent = "DNI generado correctamente.";
            parrafoMensaje.className = "text-success"; // Estilo para mensajes de éxito
        } else {
            throw new Error("Respuesta inválida del servidor.");
        }
    } catch (error) {
        console.error("Error en la solicitud:", error);
        parrafoMensaje.textContent = "Error al calcular la letra del DNI.";
        parrafoMensaje.className = "text-danger"; // Estilo para mensajes de error
    }
});
