function ViewModel() {
    var self = this;
    var base_url = window.location.origin+window.location.pathname;
    self.locale = ko.observable(data[sessionStorage.getItem("lang")]);
    self.responseErrorChangeData = ko.observable("")
    self.responseCorrectoChangeData = ko.observable("")
    self.codeChangeData = null
    self.errorChangeData = false
    self.correctoChangeData = false

    self.cambiarDatosPerfil = function ($data, event) {
        self.codeChangeData = null
        self.errorChangeData = false
        self.correctoChangeData = false
        var msg = {
            type: "cambiarDatosPerfil",
            email: document.getElementById("cambiarEmail").value,
            nick: document.getElementById("cambiarNick").value,
            newPass: document.getElementById("nuevaPassword").value,
            newPass2: document.getElementById("nuevaPassword2").value,
            actualPass: document.getElementById("passwordActual").value
        };

        var data = {
            data: JSON.stringify(msg),
            url: "/cambiarDatosPerfil",
            type: "post",
            contentType: 'application/json',
            success: function (response) {
                self.codeChangeData = response
                self.correctoChangeData = true
                self.correctoPerfil()
            }
        };
        $.ajax(data).fail(function ($xhr) {
            self.codeChangeData = $xhr.responseText
            self.errorChangeData = true
            self.errorPerfil()
        });
    }

    self.correctoPerfil = function () {
        self.responseErrorChangeData("")
        self.responseCorrectoChangeData(data[sessionStorage.getItem("lang")][self.codeChangeData])
    }

    self.errorPerfil = function ($data, event, mensaje) {
        self.responseCorrectoChangeData("")
        self.responseErrorChangeData(data[sessionStorage.getItem("lang")][self.codeChangeData])
    }

}

function cambiarMensajeCorrectoPerfil(mensaje) {
    document.getElementById("mensajeCorrectoPerfil").innerHTML = mensaje;
}

function cambiarMensajeErrorPerfil(mensaje) {
    document.getElementById("mensajeErrorPerfil").innerHTML = mensaje;
}

window.onload = function () {

    document.getElementById("changeLanguageES").addEventListener("click", function() {
        if(sessionStorage["lang"] != "es"){
            sessionStorage["lang"] = "es";
            vm.locale(data[sessionStorage.getItem("lang")])
        }
    });

    document.getElementById("changeLanguageEN").addEventListener("click", function() {
        if(sessionStorage["lang"] != "us"){
            sessionStorage["lang"] = "us";
            vm.locale(data[sessionStorage.getItem("lang")])
        }
    });
};

var vm = new ViewModel();
ko.applyBindings(vm);