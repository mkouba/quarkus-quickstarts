package org.acme.hibernate.reactive;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.IsEmptyString.emptyString;

@QuarkusTest
public class FruitsRoutesTest {

    @Test
    public void testListAllFruits() {
        System.out.println("TESTLISTALLFRUITS");
        //List all, should have all 3 fruits the database has initially:
        given()
			.when()
				.get("/ovoce/")
			.then()
				.statusCode(200)
				.body(
					containsString("Cherry"),
					containsString("Apple"),
					containsString("Banana"));

        // Update Cherry to Pineapple
        given()
			.when()
				.body("{\"name\" : \"Pineapple\"}")
				.contentType("application/json")
				.put("/ovoce/1")
			.then()
				.statusCode(200)
				.body(
					containsString("\"id\":"),
					containsString("\"name\":\"Pineapple\""));

        //List all, Pineapple should've replaced Cherry:
        given()
			.when()
				.get("/ovoce/")
			.then()
				.statusCode(200)
				.body(
					not(containsString( "Cherry" )),
					containsString("Pineapple"),
					containsString("Apple"),
					containsString("Banana"));

        //Delete Pineapple:
        given()
			.when()
				.delete("/ovoce/1")
			.then()
				.statusCode(204);

        //List all, Pineapple should be missing now:
        given()
			.when()
				.get("/ovoce/")
			.then()
                .statusCode(200)
                .body(
					not(containsString( "Pineapple")),
					containsString("Apple"),
					containsString("Banana"));

        //Create the Pear:
        given()
			.when()
				.body("{\"name\" : \"Pear\"}")
				.contentType("application/json")
				.post("/ovoce/")
			.then()
				.statusCode(201)
				.body(
					containsString("\"id\":"),
					containsString("\"name\":\"Pear\""));

        //List all, Pineapple should be still missing now:
        given()
			.when()
				.get("/ovoce/")
			.then()
				.statusCode(200)
				.body(
					not(containsString("Pineapple")),
					containsString("Apple"),
					containsString("Banana"),
					containsString("Pear"));
    }

    @Test
    public void testEntityNotFoundForDelete() {
        System.out.println("TESTENTITYNOTFOUNDFORDELETE");
        given()
			.when()
				.delete("/ovoce/9236")
			.then()
				.statusCode(404)
				.body(emptyString());
    }

    @Test
    public void testEntityNotFoundForUpdate() {
        System.out.println("TESTENTITYNOTFOUNDFORUPDATE");
        given()
			.when()
				.body("{\"name\" : \"Watermelon\"}")
				.contentType("application/json")
				.put("/ovoce/32432")
			.then()
				.statusCode(404)
				.body(emptyString());
    }

	@Test
	public void testMissingNameForUpdate() {
	    System.out.println("TESTMISSINGNAMEFORUPDATE");
		given()
			.when()
				.contentType("application/json")
				.put("/ovoce/3")
			.then()
				.statusCode(422)
				.body(
					containsString("\"code\":422"),
					containsString("\"error\":\"Fruit name was not set on request.\""),
					containsString("\"exceptionType\":\"java.lang.IllegalArgumentException\"")
				);
	}
}
