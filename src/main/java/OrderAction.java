import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderAction {

    public static Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    public static int getOrderId(int trackOrder){
        OrderJson order = given()
                .queryParam("t", trackOrder)
                .get("/api/v1/orders/track")
                .body().as(OrderJson.class);

        return   order.getOrder().getId();

    }


}
