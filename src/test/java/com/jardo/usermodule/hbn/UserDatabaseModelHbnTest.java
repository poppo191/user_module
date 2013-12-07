package com.jardo.usermodule.hbn;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Date;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.junit.Test;

import com.jardo.usermodule.containers.PasswordResetToken;
import com.jardo.usermodule.containers.User;
import com.jardo.usermodule.containers.UserPassword;

public class UserDatabaseModelHbnTest extends UMDatabaseTestCase {

	private final UserDatabaseModelHbn databaseModel;

	public UserDatabaseModelHbnTest() {
		super();
		databaseModel = new UserDatabaseModelHbn(HibernateUtil.getSessionFactory());
	}

	@Override
	protected IDataSet createInitialDataSet() throws FileNotFoundException, DataSetException {
		ReplacementDataSet result = loadFlatXmlDataSet("userDatabaseModelHbnTest/initial.xml");

		result.addReplacementObject("[NOW]", new Date());

		return result;
	}

	@Test
	public void testAddPasswordResetToken() throws SQLException, Exception {
		fillDatabase("userDatabaseModelHbnTest/beforeAddPasswordResetToken.xml");

		PasswordResetToken token = new PasswordResetToken(1, "ea587b759f423f0bfadfe7aeba0ee3fe", new Date());

		boolean result = databaseModel.addPasswordResetToken(token);
		assertEquals(true, result);

		IDataSet expectedDataSet = loadFlatXmlDataSet("userDatabaseModelHbnTest/afterAddPasswordResetToken.xml");
		assertTableContent(expectedDataSet, "um_password_reset_token", new String[] { "date_time" });
	}

	@Test
	public void testAddUser() throws SQLException, Exception {
		fillDatabase("userDatabaseModelHbnTest/beforeAddUser.xml");

		UserPassword password = new UserPassword("ea1baa4cad9d822a51a1aa267a618fb2ac6d5d98a89709a595487ea493a69e90", "7886788cb39bf33c856ef18206a81ce4b498dc5a1a4199abc0cb0fb686eab008");
		User user = new User(-1, "carl", "carl@test.com", "ea587b759f423f0bfadfe7aeba0ee3fe", false, password);

		int result = databaseModel.addUser(user);

		assertEquals(4, result);

		IDataSet expectedDataSet = loadFlatXmlDataSet("userDatabaseModelHbnTest/afterAddUser.xml");

		assertTableContent(expectedDataSet, "um_user", new String[] { "reg_date", "rank" });
	}

	@Test
	public void testCancelAllPasswordResetTokens() throws DatabaseUnitException, SQLException, Exception {
		fillDatabase("userDatabaseModelHbnTest/beforeCancelAllPasswordResetTokens.xml");
		databaseModel.cancelAllPasswordResetTokens(1);

		IDataSet expectedDataSet = loadFlatXmlDataSet("userDatabaseModelHbnTest/afterCancelAllPasswordResetTokens.xml");
		assertTableContent(expectedDataSet, "um_password_reset_token", new String[] {});
	}

	@Test
	public void testConfirmUserRegistration() throws DatabaseUnitException, SQLException, Exception {
		fillDatabase("userDatabaseModelHbnTest/beforeConfirmRegistration.xml");
		databaseModel.confirmUserRegistration("john@test.com");

		IDataSet expectedDataSet = loadFlatXmlDataSet("userDatabaseModelHbnTest/afterConfirmRegistration.xml");
		assertTableContent(expectedDataSet, "um_user", new String[] {});
	}

	@Test
	public void testDeleteUser() throws DatabaseUnitException, SQLException, Exception {
		fillDatabase("userDatabaseModelHbnTest/beforeDeleteUser.xml");

		boolean result = databaseModel.deleteUser(1);
		assertEquals(true, result);

		result = databaseModel.deleteUser(3);
		assertEquals(false, result);

		IDataSet expectedDataSet = loadFlatXmlDataSet("userDatabaseModelHbnTest/afterDeleteUser.xml");
		assertTableContent(expectedDataSet, "um_user", new String[] {});
	}

	@Test
	public void testGetRegisteredUserCount() throws DatabaseUnitException, SQLException, Exception {
		fillDatabase("userDatabaseModelHbnTest/beforeGetRegisteredUserCount.xml");

		int result = databaseModel.getRegisteredUserCount(null);
		assertEquals(2, result);

		result = databaseModel.getRegisteredUserCount(parseDate("2013-01-02"));
		assertEquals(1, result);
	}

	@Test
	public void testGetNewestPasswordResetToken() throws DatabaseUnitException, SQLException, Exception {
		fillDatabase("userDatabaseModelHbnTest/beforeGetNewestPasswordResetToken.xml");

		// non-existing user
		PasswordResetToken result = databaseModel.getNewestPasswordResetToken("non-existing@test.com");
		assertEquals(null, result);

		// no token for user
		result = databaseModel.getNewestPasswordResetToken("mike@test.com");
		assertEquals(null, result);

		// newest token
		result = databaseModel.getNewestPasswordResetToken("john@test.com");
		assertNotNull(result);
		assertEquals("785bb1e5e77a14325fd31ebeae836fff", result.getKey());
	}

	@Test
	public void testGetUserByEmail() throws DatabaseUnitException, SQLException, Exception {
		fillDatabase("userDatabaseModelHbnTest/userList.xml");

		User result = databaseModel.getUserByEmail("non-existing@test.com");
		assertEquals(null, result);

		// deleted user
		result = databaseModel.getUserByEmail("allan@test.com");
		assertEquals(null, result);

		result = databaseModel.getUserByEmail("john@test.com");
		assertNotNull(result);
		assertEquals(1, result.getId());
	}

	@Test
	public void testGetUserByName() throws DatabaseUnitException, SQLException, Exception {
		fillDatabase("userDatabaseModelHbnTest/userList.xml");

		User result = databaseModel.getUserByName("non-existing");
		assertEquals(null, result);

		// deleted user
		result = databaseModel.getUserByName("allan");
		assertEquals(null, result);

		result = databaseModel.getUserByName("john");
		assertNotNull(result);
		assertEquals(1, result.getId());
	}

	@Test
	public void testGetUserIdByEmail() throws DatabaseUnitException, SQLException, Exception {
		fillDatabase("userDatabaseModelHbnTest/userList.xml");

		int result = databaseModel.getUserIdByEmail("non-existing@test.com");
		assertEquals(-1, result);

		// deleted user
		result = databaseModel.getUserIdByEmail("allan@test.com");
		assertEquals(-1, result);

		result = databaseModel.getUserIdByEmail("john@test.com");
		assertEquals(1, result);
	}

	@Test
	public void testGetUserPassword() throws DatabaseUnitException, SQLException, Exception {
		fillDatabase("userDatabaseModelHbnTest/userList.xml");

		// non-exiting user
		UserPassword result = databaseModel.getUserPassword(10);
		assertEquals(null, result);

		// deleted user
		result = databaseModel.getUserPassword(3);
		assertEquals(null, result);

		result = databaseModel.getUserPassword(1);
		assertNotNull(result);
		assertEquals("7342f7c8d3d1d68ee7c03ab72b3deac613b837a4c4c07042cbeca974ea7ba218", result.getHash());
		assertEquals("7886788cb39bf33c856ef18206a81ce4b498dc5a1a4199abc0cb0fb686eab008", result.getSalt());
	}

	@Test
	public void testIsEmailRegistered() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsUserNameRegistered() {
		fail("Not yet implemented");
	}

	@Test
	public void testMakeLogInRecord() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetUserPassword() {
		fail("Not yet implemented");
	}

}
