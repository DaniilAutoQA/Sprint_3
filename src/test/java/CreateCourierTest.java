import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    Courier courier = new Courier("tag2444", "pass244", "name");
    int idCourier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";

    }

    @Test
    @DisplayName("Check courier creation")
    @Description("Checking status code and body message when courier creation with correct parameters")
    public void createCourier() {
        CourierAction.createCourier(courier)
                .then().assertThat().statusCode(201)
                .body("ok", equalTo(true));
        idCourier = CourierAction.getIdCourier(courier);
    }

    @Test
    @DisplayName("Create two equally Couriers")
    @Description("Checking status code and body message when create two equally couriers")
    public void createSameCourier() {
        CourierAction.createCourier(courier);
        CourierAction.createCourier(courier)
                .then().assertThat().statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        CourierAction.deleteCourier(CourierAction.getIdCourier(courier));
    }

    @Test
    @DisplayName("Create Courier without login")
    @Description("Checking status code and body message when create courier without login")
    public void createCourierWithOutLogin() {
        CourierAction.createCourier(new Courier(null, "pas2", "das2"))
                .then().assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Create Courier without password and firstName")
    @Description("Checking status code and body when create courier without password and firstName")
    public void createCourierWithOutPassAndName() {
        CourierAction.createCourier(new Courier("tagOne", null, null))
                .then().assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Create Courier with empty login")
    @Description("Checking status code and body when create courier with empty login")
    public void createCourierWithEmptyLogin() {
        CourierAction.createCourier(new Courier("", courier.getPassword(), courier.getFirstName()))
                .then().assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }


    @Test
    @DisplayName("Create Courier with empty password")
    @Description("Checking status code and body when create courier with empty password")
    public void createCourierWithEmptyPassword() {
        CourierAction.createCourier(new Courier(courier.getLogin(), "", courier.getFirstName()))
                .then().assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @After
    public void tearDown() {
        if (idCourier > 0) {
            CourierAction.deleteCourier(idCourier);
        }
    }

}

