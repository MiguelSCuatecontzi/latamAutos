package com.qualitystream.latamautos;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) // Ordena la ejecución de los métodos de prueba alfabéticamente
public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class); // Configuración del logger
    private static ExtentSparkReporter sparkReporter;
    private static ExtentReports extent;
    private static ExtentTest test1, test2, test3;
    
    private final String USER_EMAIL = "test@latamautos.com";
    private final String USER_PASSWORD = "C0ntr@señ@";
    private final String INVALID_EMAIL = "invalid@test.com";
    private final String INVALID_PASSWORD = "passwordInvalido123";
    
    private WebElement email;
    private WebElement password;
    private WebElement loginButton;

    @BeforeClass
    public static void setUpClass() {
        // Configuración inicial de ExtentReports para generación de reportes
        sparkReporter = new ExtentSparkReporter("ReportePruebas.html");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Reporte de Pruebas Automatizadas");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Crear los tests en ExtentReports
        test1 = extent.createTest("Prueba de Inicio de Sesión LatamAutos");
        test2 = extent.createTest("Prueba de Inicio de Sesión Fallido");
        test3 = extent.createTest("Prueba de Cerrar Sesión");
    }
    
    @Before
    public void setUp() {
        // Configuración del driver de Chrome
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Tiempo de espera para elementos
        driver.manage().window().maximize();
        driver.get("https://admin.seminuevos.com/login");
        
        // Inicializar los elementos del formulario de inicio de sesión
        initializeElements();
    }

    private void initializeElements() {
        // Inicialización de los elementos del formulario de inicio de sesión
        email = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"email\"]")));
        password = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"password\"]")));
        loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div/div/div[2]/form/div[4]/button")));
    }
    
    @Test
    public void test1_LatamAutos() {
        // Prueba de inicio de sesión exitoso
        try {
            test1.log(Status.INFO, "Iniciando prueba de inicio de sesión");
            logger.info("Validando la visibilidad de los campos de email y password");

            if (isElementPresent(email) && isElementPresent(password)) {
                email.clear();
                email.sendKeys(USER_EMAIL);
                File screenshotFile = captureScreenshot(driver, "correoIngresado_test1");
                test1.pass("Correo electrónico ingresado").addScreenCaptureFromPath(screenshotFile.getAbsolutePath());

                password.clear();
                password.sendKeys(USER_PASSWORD);
                screenshotFile = captureScreenshot(driver, "contrasenaIngresada_test1");
                test1.pass("Contraseña ingresada").addScreenCaptureFromPath(screenshotFile.getAbsolutePath());

                loginButton.click();
                
                wait.until(ExpectedConditions.urlContains("seminuevos"));
                Thread.sleep(5000);

                screenshotFile = captureScreenshot(driver, "espera5Segundos_test1");
                test1.pass("Esperado 5 segundos después de hacer clic en iniciar sesión")
                     .addScreenCaptureFromPath(screenshotFile.getAbsolutePath());
                logger.info("Inicio de sesión exitoso");
            } else {
                test1.fail("Campo de email o password no está disponible.");
                logger.error("Campo de email o password no está disponible.");
            }
        } catch (Exception e) {
            File screenshotFile = captureScreenshot(driver, "errorInicioSesion_test1");
            test1.fail("Error en la prueba de inicio de sesión")
                 .addScreenCaptureFromPath(screenshotFile.getAbsolutePath());
            logger.error("Error en la prueba de inicio de sesión", e);
        }
    }
    
    
    @Test
    public void test3_Logout() {
        // Prueba de cierre de sesión
        try {
            test3.log(Status.INFO, "Iniciando prueba de cierre de sesión");

            test1_LatamAutos(); // Ejecuta inicio de sesión exitoso antes de cerrar sesión

            WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"login-bar\"]/div/a/i[1]")));
            dropdown.click();
            
            Thread.sleep(5000);
            
            File screenshotFile = captureScreenshot(driver, "dropdown_visible");
            test3.pass("Dropdown mostrado correctamente").addScreenCaptureFromPath(screenshotFile.getAbsolutePath());

            WebElement logoutBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"logoutBtn\"]")));
            logoutBtn.click();

            Thread.sleep(3000);

            screenshotFile = captureScreenshot(driver, "after_logout");
            test3.pass("Logout exitoso, redirigido a la pantalla de inicio de sesión")
                 .addScreenCaptureFromPath(screenshotFile.getAbsolutePath());
            logger.info("Cierre de sesión completado correctamente");

        } catch (Exception e) {
            File screenshotFile = captureScreenshot(driver, "errorCerrarSesion_test3");
            test3.fail("Error en la prueba de cierre de sesión")
                 .addScreenCaptureFromPath(screenshotFile.getAbsolutePath());
            logger.error("Error en la prueba de cierre de sesión", e);
        }
    }
    
    
    @Test
    public void test2_LoginFallido() {
        // Prueba de inicio de sesión con credenciales inválidas
        try {
            test2.log(Status.INFO, "Iniciando prueba de inicio de sesión con credenciales inválidas");
            logger.info("Validando la visibilidad de los campos de email y password");

            if (isElementPresent(email) && isElementPresent(password)) {
                email.clear();
                email.sendKeys(INVALID_EMAIL);
                File screenshotFile = captureScreenshot(driver, "correoInvalidoIngresado_test2");
                test2.pass("Correo electrónico inválido ingresado")
                     .addScreenCaptureFromPath(screenshotFile.getAbsolutePath());

                password.clear();
                password.sendKeys(INVALID_PASSWORD);
                screenshotFile = captureScreenshot(driver, "contrasenaInvalidaIngresada_test2");
                test2.pass("Contraseña inválida ingresada")
                     .addScreenCaptureFromPath(screenshotFile.getAbsolutePath());

                loginButton.click();

                By mensajeErrorLocator = By.xpath("//div[contains(text(),'Credenciales inválidas')]");
                WebElement mensajeError = wait.until(ExpectedConditions.visibilityOfElementLocated(mensajeErrorLocator));

                if (mensajeError.isDisplayed()) {
                    screenshotFile = captureScreenshot(driver, "mensajeErrorMostrado_test2");
                    test2.pass("Mensaje de error mostrado correctamente")
                         .addScreenCaptureFromPath(screenshotFile.getAbsolutePath());
                    logger.info("Test de login fallido completado exitosamente");
                } else {
                    test2.fail("No se mostró el mensaje de error esperado");
                    logger.error("No se mostró el mensaje de error esperado");
                }
            } else {
                test2.fail("Campo de email o password no está disponible.");
                logger.error("Campo de email o password no está disponible.");
            }
        } catch (Exception e) {
            File screenshotFile = captureScreenshot(driver, "errorTestLoginFallido_test2");
            test2.fail("Error en la prueba de inicio de sesión fallido")
                 .addScreenCaptureFromPath(screenshotFile.getAbsolutePath());
            logger.error("Error en la prueba de inicio de sesión fallido", e);
        }
    }

    private boolean isElementPresent(WebElement element) {
        // Verifica si un elemento está presente
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private File captureScreenshot(WebDriver driver, String screenshotName) {
        // Captura una pantalla y la guarda en el directorio de capturas
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotDir = System.getProperty("user.dir") + "/screenshots/";
        File screenshotFile = new File(screenshotDir + screenshotName + ".png");

        try {
            Files.createDirectories(Paths.get(screenshotDir));
            Files.copy(srcFile.toPath(), screenshotFile.toPath());
        } catch (IOException e) {
            logger.error("Error al guardar la captura de pantalla", e);
        }
        return screenshotFile;
    }

    @After
    public void tearDown() {
        // Cierra el driver después de cada prueba
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterClass
    public static void tearDownClass() {
        // Finaliza el reporte de ExtentReports después de todas las pruebas
        extent.flush();
    }
}