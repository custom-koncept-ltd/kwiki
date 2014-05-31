Command Line
============

add the plugin group to settings.xml

	<pluginGroups>
		<pluginGroup>com.kncept.kwiki</pluginGroup>
	</pluginGroups>


otherwise you will have to type
	mvn com.kncept.kwiki:kwiki-maven-plugin:run
instead of
	mvn kwiki:run


Targets
=======

* <code>mvn kwiki:docs</code> will generate the precompiled user docs.
* <code>mvn kwiki:wait</code> will run a live preview server on port 8080 
* <code>mvn kwiki:run</code> is broken - its meant to be an async version of the above
* <code>mvn kwiki:stop</code> will stop a running preview server
** sending a query string of "stop" will also stop the server [default location](http://localhost:8080/?stop)


Project Integration
===================

add to your project definition:

	<build>
		<plugins>
			<plugin>
				<groupId>com.kncept.kwiki</groupId>
				<artifactId>kwiki-maven-plugin</artifactId>
				<version>1.0-SNAPSHOT</version>
				<executions>
					<execution>
						<id>docs</id>
						<goals>
							<goal>jar</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>

now a *-userdocs.jar will be generated and attached to your maven build.