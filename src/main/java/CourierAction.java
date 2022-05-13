import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierAction {

    public static Response createCourier(Courier car) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(car)
                .when()
                .post("/api/v1/courier");
    }

    public static Response loginCourier(Courier courier) {
        return given().header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

    }

    public static int getIdCourier(Courier courier) {
        return given().header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().extract().body().path("id");
    }

    public static Response deleteCourier(int id) {
        return given().delete("/api/v1/courier/" + id);
    }
}
