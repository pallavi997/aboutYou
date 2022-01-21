package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class productSearchUsingFilters {

    @Test(priority = 1)
    public void searchProductWithMultipleFilters() {
        Response res =
                given()
                        .when()
                        .get("https://api-cloud.aboutyou.de/v1/products?with=attributes:key(color|brand|pattern)&filters[color]=38925&filters[brand]=6459&filters[pattern]=35007")

                        .then()
                        .statusCode(200)
                        .extract().response();

        Assert.assertEquals(res.getBody().asString().contains("\"current\":17,\"total\":17"), true);
        Assert.assertEquals(res.getBody().asString().contains("value\":\"only_sons"), true);
        Assert.assertEquals(res.getBody().asString().contains("label\":\"Grau"), true);
        Assert.assertEquals(res.getBody().asString().contains("value\":\"checked"), true);
    }

    @Test(priority = 2)
    public void searchByWomenSFilterInMenSCategory() {
        Response res =
                given()

                        .when()
                        .get("https://api-cloud.aboutyou.de/v1/products?with=attributes&filters[categoryPath]=/manner&filters[genderCategory]=76354&filters[strapsStyle]=35037")

                        .then()
                        .statusCode(200)
                        .extract().response();

        Assert.assertEquals(res.getBody().asString().contains("\"current\":0,\"total\":0"), true);
    }

    @Test(priority = 3)
    public void searchByInvalidCharacters() {
        Response res =
                given()
                        .when()
                        .get("https://api-cloud.aboutyou.de/v1/products?with=attributes&filters[brand]=^°<>``|ßÖÜÄ")

                        .then()
                        .statusCode(200)
                        .extract().response();
        Assert.assertEquals(res.getBody().asString().contains("\"current\":0,\"total\":0"), true);
    }

    @Test(priority = 4)
    public void maxPriceIsLessThanMinPrice() {
        Response res =
                given()
                        .when()
                        .get("https://api-cloud.aboutyou.de/v1/products?with=priceRange&filters[maxPrice]=4449&filters[minPrice]=4500")

                        .then()
                        .statusCode(200)
                        .extract().response();
        Assert.assertEquals(res.getBody().asString().contains("\"current\":0,\"total\":0"), true);
    }
}
