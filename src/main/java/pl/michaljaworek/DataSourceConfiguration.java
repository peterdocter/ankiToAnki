package pl.michaljaworek;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {

	private static final String DATABASE_PATH_ARGUMENT_NAME = "databasePath";
	private static final String DEFAULT_ANKI_DATABASE_LOCATION = "/home/mj/Documents/Anki/Użytkownik 1/collection.anki2";

	@Bean
	@Autowired
	public DataSource dataSource(ApplicationArguments arguments) {
		String databasePath;
		if (arguments.containsOption(DATABASE_PATH_ARGUMENT_NAME)) {
			databasePath = arguments.getOptionValues(DATABASE_PATH_ARGUMENT_NAME).get(0);
		} else {
			databasePath = DEFAULT_ANKI_DATABASE_LOCATION;
		}
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("org.sqlite.JDBC");
		dataSourceBuilder.url("jdbc:sqlite:" + databasePath);
		return dataSourceBuilder.build();
	}

}
