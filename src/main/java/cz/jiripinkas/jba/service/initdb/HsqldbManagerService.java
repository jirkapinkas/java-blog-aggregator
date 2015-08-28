package cz.jiripinkas.jba.service.initdb;

import javax.annotation.PostConstruct;

import org.hsqldb.util.DatabaseManagerSwing;
import org.springframework.stereotype.Service;

import cz.jiripinkas.jba.annotation.DevProfile;

@DevProfile
@Service
public class HsqldbManagerService {

	@PostConstruct
	public void getDbManager(){
	   DatabaseManagerSwing.main(
		new String[] { "--url", "jdbc:hsqldb:mem:test", "--user", "sa", "--password", "", "--noexit"});
	}

}
