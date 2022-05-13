import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {

    Courier courier = new Courier("tag244", "pass244", "name");


    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        CourierAction.createCourier(courier);

    }


    @Test
    @DisplayName("Check status 200 and getting ID when Courier login")
    public void checkStatusWhenAuthorizeCourier() {
        CourierAction.loginCourier(new Courier(courier.getLogin(), courier.getPassword(), null))
                .then().assertThat().statusCode(200)
                .body("id", notNullValue())
                .and().extract().body().path("id");
        CourierAction.deleteCourier(CourierAction.getIdCourier(courier));
    }


    @Test
    @DisplayName("Courier authorize without login, check status 400 and body")
    public void checkStatusAndBodyWhenAuthorizeCourierWithOutLogin() {
        CourierAction.loginCourier(new Courier(null, courier.getPassword(), null))
                .then().assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }


    @Test
    @DisplayName("Courier authorize without password, check status 400 and body")
    public void checkStatusAndBodyWhenAuthorizeCourierWithOutPassword() {
        CourierAction.loginCourier(new Courier(courier.getLogin(), null, null))
                .then().assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }


    @Test
    @DisplayName("Courier authorize with wrong password, check status 404 and body")
    public void checkStatusAndBodyWhenAuthorizeCourierWithWrongPassword() {
        CourierAction.loginCourier(new Courier(courier.getLogin(), "123", null))
                .then().assertThat().statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }


    @Test
    @DisplayName("Courier authorize with wrong login, check status 404 and body")
    public void checkStatusAndBodyWhenAuthorizeCourierWithWrongLogin() {
        CourierAction.loginCourier(new Courier("zaqwe", courier.getPassword(), null))
                .then().assertThat().statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

}
