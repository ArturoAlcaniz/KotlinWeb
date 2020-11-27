var url = "ws://localhost:8080/juegos";
var ws = new WebSocket(url);

function ViewModel() {
    var self = this;

    self.jugadores = ko.observableArray([]);

    self.getRanking = function ($data, event) {
        var msg = {
            type: "ranking"
        };
        var data = {
            data: JSON.stringify(msg),
            url: "/getRanking",
            type: "post",
            contentType: 'application/json',
            success: function (response) {
                console.log(response)
                console.log(Object.keys(response)[0])
                console.log(Object.values(response)[0])
                console.log(Object.values(response)[0].efectivo)
                console.log(Object.keys(response).length)
                var i;

                Object.values(response).forEach(function (item, index) {
                    console.log(item)
                    var email = item.email
                    var efectivo = item.efectivo
                    var actividad = item.LastAccesed
                    self.jugadores.push({index: index, email: email, efectivo: efectivo, actividad: actividad})
                })

                /*for(i = Object.keys(response).length-1; i>0; i--){
                    console.log(Object.keys(response)[i])
                    var element = document.createElement("label");
                    element.innerHTML = Object.keys(response)[i]+" &nbsp "+ Object.values(response)[i].efectivo
                    document.getElementById("ranking").appendChild(element)
                }*/
            }
        };
        $.ajax(data).fail(function ($xhr) {
            console.log($xhr)
        });
    }
}

function ranking() {
    return document.getElementById("ranking");
}

var vm = new ViewModel();
ko.applyBindings(vm);
vm.getRanking()
