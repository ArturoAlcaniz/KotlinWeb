if(sessionStorage.getItem("lang") === null)
    sessionStorage.setItem("lang", "es")

var data = {
    us:
            {
                tittle: "Portal",
                pass: "Password",
                checkPass: "Confirm password",
                buttonRegister: "Register",
                buttonLogin: "Login",
                questionRegister: "Are you already registered?",
                questionLogin: "It's not registered?",
                emailNotValid: "Invalid email",
                passNotValid: "Invalid password",
                incorrectCredentials: "Incorrect credentials",
                loginOK: "Logged in successfully",
                passNotSame: "Passwords don't match",
                passLow: "Password too short, minimum 6 characters",
                emailExists: "The email entered already exists",
                registerOK: "Registered successfully",
                fail: "Unexpected failure"
            },
    es:
            {
                tittle: "Inicio",
                pass: "Contraseña",
                checkPass: "Confirmar contraseña",
                buttonRegister: "Registrarse",
                buttonLogin: "Logearse",
                questionRegister: "¿Ya esta registrado?",
                questionLogin: "¿No esta registrado?",
                emailNotValid: "Email no valido",
                passNotValid: "Contraseña no valida",
                incorrectCredentials: "Credenciales incorrectas",
                loginOK: "Logeado correctamente",
                passNotSame: "Las contraseñas no coinciden",
                passLow: "Contraseña muy corta, minimo 6 caracteres",
                emailExists: "El email introducido ya existe",
                registerOK: "Registrado correctamente",
                fail: "Fallo inesperado"
            }
}
