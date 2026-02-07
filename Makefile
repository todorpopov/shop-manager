start:
	mvn clean compile
	mvn exec:java -Dexec.mainClass="com.shop_manager.Main"