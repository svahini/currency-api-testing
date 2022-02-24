package com.vahini.integrations;

import com.vahini.utils.Constants;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

public class FetchCurrency {

    @Step("Get list of all currencies data")
    public Response getAllCurrency() {
        return SerenityRest.given().log().all().urlEncodingEnabled(false)
                .when()
                .get(Constants.LIST_OF_CURRENCIES);
    }

    @Step("Get single currency data")
    public Response getSingleCurrency(String currency) {
        return SerenityRest.given().log().all().urlEncodingEnabled(false)
                .pathParam("variableFromArray", currency)
                .when()
                .get(Constants.DATA);
    }
}
