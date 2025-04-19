package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints2;
import api.payload.User;
import io.restassured.response.Response;

//Note : This second class is used for routes.properties file Approach.

public class UserTests2 {

	Faker faker;
	User userPayload;

	Logger logger;

	@BeforeClass
	public void setUp() {
		faker = new Faker();
		userPayload = new User();

		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstname(faker.name().firstName());
		userPayload.setLastname(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());

		// Logs
		logger = LogManager.getLogger(this.getClass());

		logger.debug("Debugging...");

	}

	@Test(priority = 1)
	public void testPostUser() {

		logger.info("********** Creating user  ***************");
		Response response = UserEndPoints2.createUser(userPayload);
		response.then().log().all();

		Assert.assertEquals(response.getStatusCode(), 200);

		logger.info("**********User is creatged  ***************");
	}

	@Test(priority = 2)
	public void testGetUserByName() {
		logger.info("********** Reading User Info ***************");

		Response response = UserEndPoints2.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);

		logger.info("**********User info  is displayed ***************");
	}

	@Test(priority = 3)
	public void testUpdateUserByName() {
		logger.info("********** Updating User ***************");
		// Update data using Payload
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());

		Response response = UserEndPoints2.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().body();

//		response.then().log().body().statusCode(200);
		Assert.assertEquals(response.getStatusCode(), 200);

		logger.info("********** User updated ***************");
		// Checking data after update
		Response responseAfterUpdate = UserEndPoints2.readUser(this.userPayload.getUsername());

		Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
	}

	@Test(priority = 4)
	public void testDeleteUserByName() {
		logger.info("**********   Deleting User  ***************");

		Response response = UserEndPoints2.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(response.getStatusCode(), 200);

		logger.info("********** User deleted ***************");

	}
}
