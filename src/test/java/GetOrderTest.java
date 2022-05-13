import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderTest {

    Order order = Order.getDefaultOrder();
    Courier courier = new Courier("tag2444", "pass244", "name");
    int idCourier;
    int trackOrder;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        CourierAction.createCourier(courier);
        idCourier = CourierAction.getIdCourier(courier);
        trackOrder = OrderAction.createOrder(order).then().extract().body().path("track");

    }

    @Test
    @DisplayName("Check get Order")
    public void getOrder() {
        OrderJson order = given()
                .queryParam("t", trackOrder)
                .get("/api/v1/orders/track")
                .body().as(OrderJson.class);
        assertThat(order, notNullValue());

    }

    @Test
    @DisplayName("Check getting ALL orders")
    public void getAllOrders() {
        given()
                .get("/api/v1/orders")
                .then()
                .assertThat()
                .statusCode(200)
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Check status and body when send response get withOut track number")
    public void getOrderWithOutNumber() {
        given()
                .get("/api/v1/orders/track")
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для поиска"));

    }

    @Test
    @DisplayName("Check status and body when send response get with nonexistent track number")
    public void getOrderWithNonexistentNumber() {
        given()
                .queryParam("t", 0)
                .get("/api/v1/orders/track")
                .then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Заказ не найден"));
    }

    @After
    public void tearDown() {
        if (idCourier > 0) {
            CourierAction.deleteCourier(idCourier);
        }
    }


}
