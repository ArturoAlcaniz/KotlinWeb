if(sessionStorage.getItem("lang") === null)
    sessionStorage.setItem("lang", "es")

var data = {
    us:
            {
                tittle: "Profile",
                name: "Nick",
                newPass: "New password",
                cnewPass: "Confirm new password",
                actualPass: "Actual password",
                buttonChangeData: "Change data",
                emailNotValid: "Invalid email",
                passNotSame: "Passwords don't match",
                newPassNotValid: "Invalid new password",
                newPassLow: "New password too short, minimum 6 characters",
                nickNotValid: "Invalid nick",
                emailExists: "The email entered already exists",
                actualPassWrong: "Incorrect actual password",
                changeDataOK: "Succesful data change",
                placeNewPass: "Blank not to change"
            },
    es:
            {
                tittle: "Perfil",
                name: "Alias",
                newPass: "Nueva contraseña",
                cnewPass: "Confirma nueva contraseña",
                actualPass: "Contraseña actual",
                buttonChangeData: "Cambiar datos",
                emailNotValid: "Email no valido",
                passNotSame: "Las contraseñas no coinciden",
                newPassNotValid: "Nueva contraseña no valida",
                newPassLow: "Contraseña nueva muy corta, minimo 6 caracteres",
                nickNotValid: "Alias no valido",
                emailExists: "El email introducido ya existe",
                actualPassWrong: "Contraseña actual incorrecta",
                changeDataOK: "Cambio de datos realizado correctamente",
                placeNewPass: "En blanco para no cambiar"
            }
}