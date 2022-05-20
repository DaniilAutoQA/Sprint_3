import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierAction {

    @Step("Courier creation")
    public static Response createCourier(Courier car) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(car)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Login courier")
    public static Response loginCourier(Courier courier) {
        return given().header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

    }

    @Step("Getting ID courier")
    public static int getIdCourier(Courier courier) {
        return given().header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().extract().body().path("id");
    }

    @Step("Courier deletion")
    public static Response deleteCourier(int id) {
        return given().delete("/api/v1/courier/" + id);
    }
}
