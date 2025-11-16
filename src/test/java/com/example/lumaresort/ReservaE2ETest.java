package com.example.lumaresort;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ReservaE2ETest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @LocalServerPort
    private int port = 8080;

    private static final String BASE_URL = "http://localhost:4200";

    private static String correoUsuario;
    private static String contrasenaUsuario = "password123";
    private static String primeraHabitacion;
    private static String segundaHabitacion;
    private static String fechaInicio;
    private static String fechaFin;

    @BeforeAll
    public static void setupClass() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // options.addArguments("--headless");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    @AfterAll
    public static void teardownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    @DisplayName("landing page")
    public void test01_navegarALandingPage() {

        driver.get(BASE_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("app-header-component")));

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("localhost:4200"),
                "Debe estar en la página principal");

    }

    @Test
    @Order(2)
    @DisplayName("registro e intentar registro con correo invalido")
    public void test02_intentarRegistroConCorreoInvalido() {

        driver.get(BASE_URL + "/signup");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form")));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[name='nombre']")));

        WebElement nombreInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[name='nombre']")));
        nombreInput.clear();
        nombreInput.sendKeys("Juan");

        WebElement apellidoInput = driver.findElement(By.cssSelector("input[name='apellido']"));
        apellidoInput.clear();
        apellidoInput.sendKeys("Perez");

        WebElement correoInput = driver.findElement(By.cssSelector("input[name='correo']"));
        correoInput.clear();
        correoInput.sendKeys("juanperezinvalido");

        WebElement contrasenaInput = driver.findElement(By.cssSelector("input[name='contrasena']"));
        contrasenaInput.clear();
        contrasenaInput.sendKeys("usuario");

        WebElement cedulaInput = driver.findElement(By.cssSelector("input[name='cedula']"));
        cedulaInput.clear();
        cedulaInput.sendKeys("1234567890");

        WebElement telefonoInput = driver.findElement(By.cssSelector("input[name='telefono']"));
        telefonoInput.clear();
        telefonoInput.sendKeys("3001234567");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        String validationMessage = correoInput.getAttribute("validationMessage");
        assertNotNull(validationMessage, "Debe haber un mensaje de validación");
        assertFalse(validationMessage.isEmpty(),
                "El mensaje de validación no debe estar vacío");
    }

    @Test
    @Order(3)
    @DisplayName("Corregir y registrarse correctamente")
    public void test03_registrarseCorrectamente() throws InterruptedException {

        driver.get(BASE_URL + "/signup");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form")));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[name='nombre']")));

        String timestamp = String.valueOf(System.currentTimeMillis());
        correoUsuario = "usuario" + timestamp + "@test.com";

        WebElement nombreInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[name='nombre']")));
        nombreInput.clear();
        nombreInput.sendKeys("Juan");

        WebElement apellidoInput = driver.findElement(By.cssSelector("input[name='apellido']"));
        apellidoInput.clear();
        apellidoInput.sendKeys("Pérez");

        WebElement correoInput = driver.findElement(By.cssSelector("input[name='correo']"));
        correoInput.clear();
        correoInput.sendKeys(correoUsuario);

        WebElement contrasenaInput = driver.findElement(By.cssSelector("input[name='contrasena']"));
        contrasenaInput.clear();
        contrasenaInput.sendKeys(contrasenaUsuario);

        WebElement cedulaInput = driver.findElement(By.cssSelector("input[name='cedula']"));
        cedulaInput.clear();
        cedulaInput.sendKeys("1234567890");

        WebElement telefonoInput = driver.findElement(By.cssSelector("input[name='telefono']"));
        telefonoInput.clear();
        telefonoInput.sendKeys("3001234567");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[type='submit']")));
        submitButton.click();

        try {
            WebElement successMsg = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(".success-state, .success-content")));
            assertTrue(successMsg.isDisplayed(), "Debe mostrar mensaje de éxito");

            //Thread.sleep(2000);
        } catch (TimeoutException e) {
            System.out.println("hubo un error al registrar el usuario");
        }
    }

    @Test
    @Order(4)
    @DisplayName("inicio de sesion con el usuario registrado")
    public void test04_iniciarSesion() throws InterruptedException {

        driver.get(BASE_URL + "/login");

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[name='correo'], input[type='email']")));

        WebElement loginCorreo = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[name='correo'], input[type='email']")));
        loginCorreo.clear();
        //Thread.sleep(200);
        loginCorreo.sendKeys(correoUsuario);

        WebElement loginPassword = driver.findElement(
                By.cssSelector("input[name='contrasena'], input[type='password']"));
        loginPassword.clear();
        //Thread.sleep(200);
        loginPassword.sendKeys(contrasenaUsuario);

        //Thread.sleep(500);
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/perfil"),
                    ExpectedConditions.urlContains("/home"),
                    ExpectedConditions.urlContains("localhost:4200")
            ));

            Thread.sleep(1000);

        } catch (TimeoutException e) {

            try {
                WebElement errorMsg = driver.findElement(By.cssSelector(".error-message, .alert-danger, .error, .mat-error"));
            } catch (Exception ex) {
                System.out.println("   No se encontro mensaje de error visible");
            }
            throw e;
        }
    }

    @Test
    @Order(5)
    @DisplayName("Crear primera reserva para la siguiente semana")
    public void test05_crearPrimeraReserva() throws InterruptedException {

        driver.get(BASE_URL + "/reservas");

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".reservas-container, .form-section")));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".loading-state")));

        LocalDate hoy = LocalDate.now();
        LocalDate inicioReserva = hoy.plusDays(7);
        LocalDate finReserva = inicioReserva.plusDays(3);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        fechaInicio = inicioReserva.format(formatter);
        fechaFin = finReserva.format(formatter);

        System.out.println("   Fecha inicio: " + fechaInicio);
        System.out.println("   Fecha fin: " + fechaFin);

        // Llenar las fechas
        WebElement fechaInicioInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='date']")));

        // Usar JavaScript para establecer el valor de la fecha
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = arguments[1];", fechaInicioInput, fechaInicio);
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", fechaInicioInput);

        // Encontrar el segundo input de fecha (fecha fin)
        WebElement fechaFinInput = driver.findElements(By.cssSelector("input[type='date']")).get(1);
        js.executeScript("arguments[0].value = arguments[1];", fechaFinInput, fechaFin);
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", fechaFinInput);

        Thread.sleep(1000);

        // Esperar y seleccionar una habitación
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".habitacion-card")));

        WebElement primeraHabitacionCard = driver.findElement(By.cssSelector(".habitacion-card"));

        // Obtener el numero de habitacion
        WebElement habNumero = primeraHabitacionCard.findElement(By.cssSelector(".hab-number"));
        String numeroTexto = habNumero.getText();
        primeraHabitacion = numeroTexto.replaceAll("[^0-9]", "");

        System.out.println("   Habitación seleccionada: " + primeraHabitacion);

        js.executeScript("arguments[0].click();", primeraHabitacionCard);

        wait.until(ExpectedConditions.attributeContains(primeraHabitacionCard, "class", "selected"));

        //Thread.sleep(1000);
        WebElement btnConfirmar = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".btn-reserve")));
        js.executeScript("arguments[0].click();", btnConfirmar);

        // Esperar el exito
        WebElement modalExito = null;
        try {
            modalExito = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".modal-overlay, .success-icon, .modal-content.success")));
        } catch (TimeoutException e) {
            try {
                modalExito = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".modal, .success, [class*='modal'], [class*='success']")));
            } catch (TimeoutException e2) {

                try {
                    WebElement error = driver.findElement(By.cssSelector(".error, .alert-danger, .mat-error"));
                    System.out.println("   Mensaje de error: " + error.getText());
                } catch (Exception ex) {
                    System.out.println("   No se encontró mensaje de error");
                }

                fail("No se mostró el modal de confirmación de reserva");
            }
        }

        assertNotNull(modalExito, "El modal de éxito debe existir");

        Thread.sleep(1500);

        // Cerrar el modal
        try {
            WebElement btnCerrar = driver.findElement(By.cssSelector(".btn-modal-close"));
            js.executeScript("arguments[0].click();", btnCerrar);
            // Thread.sleep(1000);
        } catch (Exception e) {
            try {
                js.executeScript("arguments[0].click();", modalExito);
                //Thread.sleep(500);
            } catch (Exception ex) {
                System.out.println("   No se pudo cerrar el modal de exito");
            }
        }
    }

    @Test
    @Order(6)
    @DisplayName("Crear segunda reserva con fechas cruzadas")
    public void test06_crearSegundaReservaConFechasInterceptadas() throws InterruptedException {

        driver.get(BASE_URL + "/reservas");

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".reservas-container")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".loading-state")));

        // Calcular fechas interceptadas
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSegundaReserva = hoy.plusDays(8);
        LocalDate finSegundaReserva = inicioSegundaReserva.plusDays(3);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaInicioSegunda = inicioSegundaReserva.format(formatter);
        String fechaFinSegunda = finSegundaReserva.format(formatter);

        // Llenar las fechas
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement fechaInicioInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='date']")));
        js.executeScript("arguments[0].value = arguments[1];", fechaInicioInput, fechaInicioSegunda);
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", fechaInicioInput);

        WebElement fechaFinInput = driver.findElements(By.cssSelector("input[type='date']")).get(1);
        js.executeScript("arguments[0].value = arguments[1];", fechaFinInput, fechaFinSegunda);
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", fechaFinInput);

        Thread.sleep(2000);

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".habitacion-card")));

        var habitacionesCards = driver.findElements(By.cssSelector(".habitacion-card"));

        assertTrue(habitacionesCards.size() > 0, "Debe haber habitaciones disponibles");

        WebElement segundaHabitacionCard = null;

        // Buscar una habitacion diferente a la primera
        for (WebElement card : habitacionesCards) {
            WebElement habNumero = card.findElement(By.cssSelector(".hab-number"));
            String numeroTexto = habNumero.getText();
            String numeroHab = numeroTexto.replaceAll("[^0-9]", "");

            if (!numeroHab.equals(primeraHabitacion)) {
                segundaHabitacionCard = card;
                segundaHabitacion = numeroHab;
                break;
            }
        }

        assertNotNull(segundaHabitacionCard,
                "Debe haber al menos una habitación diferente disponible");

        js.executeScript("arguments[0].click();", segundaHabitacionCard);

        wait.until(ExpectedConditions.attributeContains(segundaHabitacionCard, "class", "selected"));

        //Thread.sleep(1000);
        // Confirmar reserva
        WebElement btnConfirmar = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".btn-reserve")));
        js.executeScript("arguments[0].click();", btnConfirmar);

        // Esperar el exito
        WebElement modalExito = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".modal-overlay, .success-icon, .modal-content.success")));

        assertTrue(modalExito.isDisplayed(), "Debe mostrar modal de éxito para la segunda reserva");
    }

    @Test
    @Order(7)
    @DisplayName("Verificar que las habitaciones asignadas son diferentes")
    public void test07_verificarHabitacionesDiferentes() {

        assertNotNull(primeraHabitacion, "Primera habitación debe estar asignada");
        assertNotNull(segundaHabitacion, "Segunda habitación debe estar asignada");
        assertNotEquals(primeraHabitacion, segundaHabitacion,
                "Las habitaciones deben ser diferentes para reservas con fechas interceptadas");
    }

    @Test
    @Order(8)
    @DisplayName("Verificar que la primera habitacion no está disponible para las fechas interceptadas")
    public void test08_verificarPrimeraHabitacionNoDisponible() throws InterruptedException {

        driver.get(BASE_URL + "/reservas");

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".reservas-container")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".loading-state")));

        // Configurar las mismas fechas de la primera reserva
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement fechaInicioInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='date']")));
        js.executeScript("arguments[0].value = arguments[1];", fechaInicioInput, fechaInicio);
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", fechaInicioInput);

        WebElement fechaFinInput = driver.findElements(By.cssSelector("input[type='date']")).get(1);
        js.executeScript("arguments[0].value = arguments[1];", fechaFinInput, fechaFin);
        js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", fechaFinInput);

        Thread.sleep(2000);

        // Verificar que la primera habitacion no aparece en la lista o está marcada como no disponible
        var habitacionesCards = driver.findElements(By.cssSelector(".habitacion-card"));

        boolean primeraHabitacionEncontrada = false;

        for (WebElement card : habitacionesCards) {
            WebElement habNumero = card.findElement(By.cssSelector(".hab-number"));
            String numeroTexto = habNumero.getText();
            String numeroHab = numeroTexto.replaceAll("[^0-9]", "");

            if (numeroHab.equals(primeraHabitacion)) {
                primeraHabitacionEncontrada = true;
                // Si aparece, verificar que esté marcada como ocupada
                WebElement estadoBadge = card.findElement(By.cssSelector(".feature-badge:has(.feature-icon)"));
                String estadoTexto = estadoBadge.getText().toLowerCase();

                assertFalse(estadoTexto.contains("disponible"),
                        "La habitación " + primeraHabitacion + " NO debe estar disponible para estas fechas");
                break;
            }
        }

        // Si no se encuentra en la lista
        if (!primeraHabitacionEncontrada) {
        }

    }
}
