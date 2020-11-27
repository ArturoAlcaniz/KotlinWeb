
function ViewModel() {
    let self = this;
    self.locale = ko.observable(data[sessionStorage.getItem("lang")]);
    self.responseErrorLogin = ko.observable("")
    self.responseErrorRegister = ko.observable("")
    self.responseCorrectoLogin = ko.observable("")
    self.responseCorrectoRegister = ko.observable("")
    self.codeLogin = null
    self.errorLogin = false
    self.correctoLogin = false
    self.codeRegister = null
    self.errorRegister = false
    self.correctoRegister = false

    self.login = function ($data, event) {
        self.codeRegister = null
        self.errorRegister = false
        self.correctoRegister = false
        const msg = {
            type: "login",
            email: document.getElementById("email-login").value,
            pass: document.getElementById("pwd-login").value
        };
        const data = {
            data: JSON.stringify(msg),
            url: "login",
            type: "post",
            contentType: 'application/json',
            success: function (response) {
                self.codeLogin = response
                self.CorrectoLogin()
                sleep(700).then(() => {
                    window.location.href = "home";
                });
            }
        };
        $.ajax(data).fail(function ($xhr) {
            self.codeLogin = $xhr.responseText
            self.ErrorLogin()
        });
    }
    self.register = function ($data, event) {
        self.codeLogin = null
        self.errorLogin = false
        self.correctoLogin = false
        const msg = {
            type : "register",
            email : getEmailRegistro(),
            pwd1 : getPwd1Registro(),
            pwd2 : getPwd2Registro()
        };
        const data = {
            data: JSON.stringify(msg),
            url: "register",
            type: "post",
            contentType: 'application/json',
            success: function (response) {
                self.codeRegister = response
                self.CorrectoRegister()
                sleep(700).then(() => {
                    window.location.href = "home";
                });
            }
        };
        $.ajax(data).fail(function ($xhr) {
            self.codeRegister = $xhr.responseText
            self.ErrorRegister()
        });
    }

    self.ErrorLogin = function () {
        self.responseCorrectoLogin("")
        self.responseCorrectoRegister("")
        self.responseErrorRegister("")
        self.errorLogin = true
        self.responseErrorLogin(data[sessionStorage.getItem("lang")][self.codeLogin])
    }
    self.ErrorRegister = function () {
        self.responseCorrectoRegister("")
        self.responseCorrectoLogin("")
        self.responseErrorLogin("")
        self.errorRegister = true
        self.responseErrorRegister(data[sessionStorage.getItem("lang")][self.codeRegister])
    }
    self.CorrectoLogin = function () {
        self.responseErrorLogin("")
        self.responseErrorRegister("")
        self.responseCorrectoRegister("")
        self.correctoLogin = true
        self.responseCorrectoLogin(data[sessionStorage.getItem("lang")][self.codeLogin])
    }
    self.CorrectoRegister = function () {
        self.responseErrorRegister("")
        self.responseErrorLogin("")
        self.responseCorrectoLogin("")
        self.correctoRegister = true
        self.responseCorrectoRegister(data[sessionStorage.getItem("lang")][self.codeRegister])
    }
}

$('.toggle').click(function(){
    $('.formulario').animate({
        height: "toggle",
        'padding-top': 'toggle',
        'padding-bottom': 'toggle',
        opacity: 'toggle'
    }, "slow");
});
function getEmailRegistro(){
    return document.getElementById("emailRegistro").value
}

function getPwd1Registro(){
    return document.getElementById("pass1").value
}

function getPwd2Registro() {
    return document.getElementById("pass2").value
}

function sleep (time) {
    return new Promise((resolve) => setTimeout(resolve, time));
}

window.onload = function () {

    document.getElementById("changeLanguageES").addEventListener("click", function() {
        if(sessionStorage["lang"] != "es"){
            sessionStorage["lang"] = "es";
            vm.locale(data[sessionStorage.getItem("lang")])
            if(vm.codeLogin != null){
                if(vm.errorLogin)
                    vm.responseErrorLogin(data["es"][vm.codeLogin])
                if(vm.correctoLogin)
                    vm.responseCorrectoLogin(data["es"][vm.codeLogin])
            }
            if(vm.codeRegister != null){
                if(vm.errorRegister)
                    vm.responseErrorRegister(data["es"][vm.codeRegister])
                if(vm.correctoRegister)
                    vm.responseCorrectoRegister(data["es"][vm.correctoRegister])
            }
        }
    });

    document.getElementById("changeLanguageEN").addEventListener("click", function() {
        if(sessionStorage["lang"] != "us"){
            sessionStorage["lang"] = "us";
            vm.locale(data[sessionStorage.getItem("lang")])
            if(vm.codeLogin != null){
                if(vm.errorLogin)
                    vm.responseErrorLogin(data["us"][vm.codeLogin])
                if(vm.correctoLogin)
                    vm.responseCorrectoLogin(data["us"][vm.codeLogin])
            }
            if(vm.codeRegister != null){
                if(vm.errorRegister)
                    vm.responseErrorRegister(data["us"][vm.codeRegister])
                if(vm.correctoRegister)
                    vm.responseCorrectoRegister(data["us"][vm.codeRegister])
            }
        }
    });
};

var vm = new ViewModel();
ko.applyBindings(vm, document.getElementById("html"));