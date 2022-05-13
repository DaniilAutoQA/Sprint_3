import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteCourierTest {

    Courier courier = new Courier("tag244", "pass244", "name");


    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        CourierAction.createCourier(courier);
    }

    @Test
    @DisplayName("Check status 200 and body message when deleted Courier")
    public void deleteCourier() {
        CourierAction.deleteCourier(CourierAction.getIdCourier(courier))
                .then()
                .assertThat()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Check body message when send response without ID")
    public void deleteCourierWithOutID() {
        given().delete("/api/v1/courier/")
                .then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Check status 200 and body message when send response with nonexistent ID")
    public void deleteCourierWithNonexistentID() {
        CourierAction.deleteCourier(1)
                .then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Курьера с таким id нет"));
    }

}
