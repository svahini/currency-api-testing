package com.vahini.currencylist;

import com.vahini.integrations.FetchCurrency;
import com.vahini.testbase.TestBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.greaterThan;


@RunWith(SerenityRunner.class)
public class CurrencyListTest extends TestBase {

    static ArrayList<String> currencyList = new ArrayList<>();
    static int valueFirstIteration;
    @Steps
    FetchCurrency steps;

    @Title("Getting the list of all currencies and verifying GBP & USD in the list, list has more than 20 items & storing all abbreviations to an ArrayList ")
    @Test
    public void getAllCurrency() throws IOException {
        Response response = steps.getAllCurrency();
        response.then().log().all();
        //Storing response in a Map
        HashMap result = new ObjectMapper().readValue(response.asString(), HashMap.class);
        //Adding the Set of keys from Map in to List
        currencyList.addAll(result.keySet());
        //Sorting the currencyList
        Collections.sort(currencyList);

        Assert.assertThat(currencyList, hasItems("gbp", "usd"));
        Assert.assertThat(currencyList.size(), greaterThan(20));
        System.out.println(currencyList);
    }

    @Title("Getting single currency record and checking that all currencies have same amount of currencies to exchange to")
    @Test
    public void getSingleCurrency() throws IOException {
        //Iteration of the list to get single currency
        for (String currency : currencyList) {
            if (currency == currencyList.get(0)) {
                Response response = steps.getSingleCurrency(currency);
                //Extracting currency amount and storing in static variable
                valueFirstIteration = response.then().extract().path(currency + "." + currency);
                HashMap firstIteration = new ObjectMapper().readValue(response.asString(), HashMap.class);
                System.out.println("Currency Amount at first iteration: " + valueFirstIteration);
            } else {
                Response response = steps.getSingleCurrency(currency);
                int currencyValueNextIteration = response.then().extract().path(currency + "." + currency);
                System.out.println("Currency Amount for this iteration: " + currencyValueNextIteration);
                //Verifying the currency amount to previously stored amount
                Assert.assertThat(currencyValueNextIteration, equalTo(valueFirstIteration));
            }
        }
    }
}
