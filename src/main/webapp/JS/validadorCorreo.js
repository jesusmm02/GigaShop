const URL = "http://localhost:8080/GigaShop/RegistroController";

const emailInput = document.getElementById('email');
const estadoSpan = document.getElementById('estado');

emailInput.addEventListener('blur', async () => {
    

    // Crear los datos para la solicitud
    const data = new URLSearchParams();
    data.append('accion', 'comprobarEmail');
    data.append('email', emailInput.value.trim());

    // Realizar la solicitud al backend
    try {
        let response = await fetch(URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: data.toString()
        });
        
        console.log("respuestaVuelta");

        // Procesar la respuesta
        if (response.ok) {
            let resultado = await response.json();
            if (resultado.disponible) {
                estadoSpan.textContent = "Correo disponible";
                estadoSpan.style.color = "green";
            } else {
                estadoSpan.textContent = "Correo no disponible";
                estadoSpan.style.color = "red";
            }
        } else {
            estadoSpan.textContent = "Error al comprobar el correo";
            estadoSpan.style.color = "orange";
        }
    } catch (error) {
        estadoSpan.textContent = "Error de conexión";
        estadoSpan.style.color = "orange";
    }
});



