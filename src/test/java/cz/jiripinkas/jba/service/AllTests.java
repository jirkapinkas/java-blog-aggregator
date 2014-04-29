package cz.jiripinkas.jba.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BlogServiceTest.class, ItemServiceTest.class, RssServiceTest.class })
public class AllTests {

}
