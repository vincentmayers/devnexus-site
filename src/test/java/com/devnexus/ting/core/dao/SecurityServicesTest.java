/**
 *
 */
package com.devnexus.ting.core.dao;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

/**
 * @author Gunnar Hillert
 * @version $Id: IndustryDaoTest.java 605 2010-08-31 05:31:30Z ghillert $
 */
public class SecurityServicesTest extends BaseDaoIntegrationTest {

	private final static Logger LOGGER = LoggerFactory.getLogger(SecurityServicesTest.class);

	@Autowired private PasswordEncoder passwordEncoder;

	/**
	 * Test to verify that the seed data is correctly populated. Typically there
	 * should be 10 industries in the system:
	 *
	 */
	@Test
	@Rollback(false)
	public void testPasswordEncoder() {
		final String password = passwordEncoder.encodePassword("testing", null);
		LOGGER.info("Password: " + password);

	}

}
