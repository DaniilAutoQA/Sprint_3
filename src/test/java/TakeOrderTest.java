import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TakeOrderTest {

    Order order = Order.getDefaultOrder();
    Courier courier = new Courier("tag2444", "pass244", "name");
    int idOrder;
    int idCourier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        CourierAction.createCourier(courier);
        idCourier = CourierAction.getIdCourier(courier);
        idOrder = OrderAction.getOrderId(OrderAction.createOrder(order).then().extract().body().path("track"));

    }

    @Test
    @DisplayName("Check take order")
    public void takeOrder() {
        given()
                .put(String.format("api/v1/orders/accept/%s?courierId=%s", idOrder, idCourier))
                .then()
                .assertThat().statusCode(200)
                .body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Check status and body when take order without id Courier")
    public void takeOrderWithOutIdCourier() {
        given()
                .put(String.format("api/v1/orders/accept/%s?courierId=", idOrder))
                .then()
                .assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для поиска"));

    }

    @Test
    @DisplayName("Check status and body when take order with nonexistent id Courier")
    public void takeOrderWithNonexistentIdCourier() {
        given()
                .put(String.format("api/v1/orders/accept/%s?courierId=%s", idOrder, 0))
                .then()
                .assertThat().statusCode(404)
                .body("message", equalTo("Курьера с таким id не существует"));

    }

    @Test
    @DisplayName("Check status and body when take order without id order")
    public void takeOrderWithOutIdOrder() {
        given()
                .put(String.format("api/v1/orders/accept/courierId=%s", idCourier))
                .then()
                .assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для поиска"));

    }

    @Test
    @DisplayName("Check status and body when take order with nonexistent id order")
    public void takeOrderWithNonexistentIdOrder() {
        given()
                .put(String.format("api/v1/orders/accept/%s?courierId=%s", 0, idCourier))
                .then()
                .assertThat().statusCode(404)
                .body("message", equalTo("Заказа с таким id не существует"));

    }

    @After
    public void tearDown() {
        if (idCourier > 0) {
            CourierAction.deleteCourier(idCourier);
        }
    }


}
