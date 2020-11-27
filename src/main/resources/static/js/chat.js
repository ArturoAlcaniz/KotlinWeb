const url = "ws://localhost:8080/juegos";
const ws = new WebSocket(url);

function ViewModel() {
    let self = this;
    self.mensajes = ko.observableArray([]);

    self.joinToChat = function () {
        const msg = {
            type: "joinToChat"
        };

        const data = {
            data: JSON.stringify(msg),
            url: "/joinToChat",
            type: "post",
            contentType: 'application/json',
            success: function (response) {

            }
        };
        $.ajax(data).fail(function () {
            console.log("Error al unirse al chat")
        })
    }

    self.enviarMensaje = function ($data, event) {
        const msg = {
            type: "enviarMensaje",
            mensaje: document.getElementById("Mensaje").value
        };

        const data = {
            data: JSON.stringify(msg),
            url: "/enviarMensaje",
            type: "post",
            contentType: 'application/json',
            success: function (response) {
                self.corrrectoMensaje($data, event, response)
                document.getElementById("Mensaje").value = ""
            }
        };
        $.ajax(data).fail(function ($xhr) {
            self.errorMensaje($data, event, $xhr.responseText)
        });
    }

    self.corrrectoMensaje = function ($data, event, mensaje) {
        cambiarMensajeErrorChat("")
        cambiarMensajeCorrectoChat(mensaje)
    }

    self.errorMensaje = function ($data, event, mensaje) {
        cambiarMensajeCorrectoChat("")
        cambiarMensajeErrorChat(mensaje)
    }

    ws.onmessage = function (event) {
        const data = JSON.parse(event.data)

        if (data.type === "MensajeChat"){
            self.mensajes.push(data)
        }

        if (data.type === "startChat"){

            data.mensajes.forEach(function (item) {
                let la = document.createElement("LABEL");
                let la2 = document.createElement("LABEL");
                let la3 = document.createElement("LABEL");

                la.classList = "FechaProx";
                la2.classList = "Nombre";
                la3.classList = "Mensajes";

                la.innerHTML = '(' + item.fecha + ')&nbsp';
                la2.innerHTML = item.usuario + ':&nbsp';
                la3.innerHTML = item.mensaje;

                const br = document.createElement("BR");
                const E = document.getElementById("startChat")

                E.appendChild(la)
                E.appendChild(la2)
                E.appendChild(la3)
                E.appendChild(br)
            });

        }

        if (data.type === "ComandoChat"){
            console.log(data)
            if (data.comando === "/clear")
                self.mensajes(null)
        }

    }
    $(document).ready(function() {
        vm.joinToChat()
    });
}

function cambiarMensajeCorrectoChat(mensaje) {
    document.getElementById("mensajeCorrectoChat").innerHTML = mensaje;
}

function cambiarMensajeErrorChat(mensaje) {
    document.getElementById("mensajeErrorChat").innerHTML = mensaje;
}

var vm = new ViewModel();
ko.applyBindings(vm);







