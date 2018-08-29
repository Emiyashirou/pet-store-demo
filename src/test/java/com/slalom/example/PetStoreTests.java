package com.slalom.example;

import com.slalom.example.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PetStoreTests {

    @Test
    public void functionalTest() throws URISyntaxException, IOException{

        String url = "http://localhost:8080/pet";
        Pet pet = new Pet();
        Category category = new Category();
        category.setName("Polar Bear");
        pet.setCategory(category);
        pet.setName("Doggie");
        Tag tag = new Tag();
        tag.setName("Young");
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(tag);
        pet.setTags(tags);
        pet.setStatus("available");
        pet.setPhotoUrls("http://sample.photoUrls");
        given().contentType("application/json").body(pet).when().post(url).then().body("name", equalTo("Doggie"));

        url = "http://localhost:8080/pet?status=available,pending";
        given().when().get(url).then().body("[0].name", equalTo("Doggie"));

        url = "http://localhost:8080/pet/1";
        given().when().get(url).then().body("name", equalTo("Doggie"));

        url = "http://localhost:8080/pet/1";
        pet.setName("Puppy");
        given().contentType("application/json").body(pet).when().put(url).then().body("name", equalTo("Puppy"));

        url = "http://localhost:8080/pet/1";
        given().header("api_key", "special-key").when().delete(url).then().statusCode(204);

        url = "http://localhost:8080/store/order";
        StoreOrder order = new StoreOrder();
        order.setPetId(1l);
        order.setQuantity(1);
        order.setShipDate(new Date());
        order.setStatus("placed");
        order.setComplete(false);
        given().contentType("application/json").body(order).when().post(url).then().body("status", equalTo("placed"));

        url = "http://localhost:8080/store/order/1";
        given().when().get(url).then().body("status", equalTo("placed"));

        url = "http://localhost:8080/store/order/1";
        given().header("api_key", "special-key").when().delete(url).then().statusCode(204);

        url = "http://localhost:8080/user";
        User user = new User();
        user.setUsername("leili@slalom");
        user.setFirstName("lei");
        user.setLastName("li");
        user.setEmail("lei.li@slalom.com");
        user.setPassword("strongpassword");
        user.setPhone("6146482168");
        user.setUserStatus(0);
        given().contentType("application/json").header("api_key", "special-key").body(user).when().post(url).then().body("username", equalTo("leili@slalom"));

        url = "http://localhost:8080/user/leili@slalom";
        given().when().get(url).then().body("username", equalTo("leili@slalom"));

        url = "http://localhost:8080/user/leili@slalom";
        user.setFirstName("Tommy");
        user.setLastName("Smith");
        given().contentType("application/json").header("api_key", "special-key").body(user).when().put(url).then().body("firstName", equalTo("Tommy"));

        url = "http://localhost:8080/user/leili@slalom";
        given().header("api_key", "special-key").when().delete(url).then().statusCode(204);
    }

}
