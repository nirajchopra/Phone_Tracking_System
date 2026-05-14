# Phone Tracking System
```text
└── Phone_Tracking_System
    ├── pom.xml
    ├── src
    │   └── main
    │       ├── java
    │       │   └── com
    │       │       └── pts
    │       │           ├── controller
    │       │           │   ├── DashboardServlet.java
    │       │           │   ├── LoginServlet.java
    │       │           │   ├── LogoutServlet.java
    │       │           │   ├── ProfileServlet.java
    │       │           │   ├── RegisterServlet.java
    │       │           │   ├── TestServlet.java
    │       │           │   └── TrackLocationServlet.java
    │       │           ├── dao
    │       │           │   ├── PhoneLocationDAO.java
    │       │           │   ├── TrackingRequestDAO.java
    │       │           │   └── UserDAO.java
    │       │           ├── model
    │       │           │   ├── PhoneLocation.java
    │       │           │   ├── SearchType.java
    │       │           │   ├── TrackingRequest.java
    │       │           │   └── User.java
    │       │           ├── service
    │       │           │   ├── LocationService.java
    │       │           │   └── UserService.java
    │       │           └── util
    │       │               ├── AuthenticationFilter.java
    │       │               ├── HibernateUtil.java
    │       │               ├── PasswordUtil.java
    │       │               └── ValidationUtil.java
    │       ├── resource
    │       │   └── hibernate.cfg.xml
    │       └── webapp
    │           ├── css
    │           │   ├── auth.css
    │           │   ├── dashboard.css
    │           │   └── style.css
    │           ├── dashboard.jsp
    │           ├── error.jsp
    │           ├── index.jsp
    │           ├── js
    │           │   ├── auth.js
    │           │   ├── dashboard.js
    │           │   └── register.js
    │           ├── login.jsp
    │           ├── navbar.jsp
    │           ├── profile.jsp
    │           ├── register.jsp
    │           └── WEB-INF
    │               └── web.xml
    └── target
        ├── classes
        │   ├── com
        │   │   └── pts
        │   │       ├── controller
        │   │       │   ├── DashboardServlet.class
        │   │       │   ├── LoginServlet.class
        │   │       │   ├── LogoutServlet.class
        │   │       │   ├── ProfileServlet.class
        │   │       │   ├── RegisterServlet.class
        │   │       │   ├── TestServlet.class
        │   │       │   └── TrackLocationServlet.class
        │   │       ├── dao
        │   │       │   ├── PhoneLocationDAO.class
        │   │       │   ├── TrackingRequestDAO.class
        │   │       │   └── UserDAO.class
        │   │       ├── model
        │   │       │   ├── PhoneLocation.class
        │   │       │   ├── SearchType.class
        │   │       │   ├── TrackingRequest.class
        │   │       │   ├── User.class
        │   │       │   └── UserRole.class
        │   │       ├── service
        │   │       │   ├── LocationService.class
        │   │       │   └── UserService.class
        │   │       └── util
        │   │           ├── AuthenticationFilter.class
        │   │           ├── HibernateUtil.class
        │   │           ├── PasswordUtil.class
        │   │           └── ValidationUtil.class
        │   └── hibernate.cfg.xml
        ├── m2e-wtp
        │   └── web-resources
        │       └── META-INF
        │           ├── MANIFEST.MF
        │           └── maven
        │               └── com.pts
        │                   └── Phone_Tracking_System
        │                       ├── pom.properties
        │                       └── pom.xml
        ├── maven-archiver
        │   └── pom.properties
        ├── maven-status
        │   └── maven-compiler-plugin
        │       └── compile
        │           └── default-compile
        │               ├── createdFiles.lst
        │               └── inputFiles.lst
        ├── Phone_Tracking_System-1.0.0
        │   ├── css
        │   │   ├── auth.css
        │   │   ├── dashboard.css
        │   │   └── style.css
        │   ├── dashboard.jsp
        │   ├── error.jsp
        │   ├── index.jsp
        │   ├── js
        │   │   ├── auth.js
        │   │   ├── dashboard.js
        │   │   └── register.js
        │   ├── login.jsp
        │   ├── register.jsp
        │   └── WEB-INF
        │       ├── classes
        │       │   └── com
        │       │       └── pts
        │       │           ├── controller
        │       │           │   ├── DashboardServlet.class
        │       │           │   ├── LoginServlet.class
        │       │           │   ├── LogoutServlet.class
        │       │           │   ├── RegisterServlet.class
        │       │           │   ├── TestServlet.class
        │       │           │   └── TrackLocationServlet.class
        │       │           ├── dao
        │       │           │   ├── AuthenticationFilter.class
        │       │           │   ├── PhoneLocationDAO.class
        │       │           │   ├── TrackingRequestDAO.class
        │       │           │   └── UserDAO.class
        │       │           ├── model
        │       │           │   ├── PhoneLocation.class
        │       │           │   ├── SearchType.class
        │       │           │   ├── TrackingRequest.class
        │       │           │   ├── User.class
        │       │           │   └── UserRole.class
        │       │           ├── service
        │       │           │   ├── LocationService.class
        │       │           │   └── UserService.class
        │       │           └── util
        │       │               ├── AuthenticationFilter.class
        │       │               ├── HibernateUtil.class
        │       │               └── ValidationUtil.class
        │       ├── lib
        │       │   ├── antlr-2.7.7.jar
        │       │   ├── byte-buddy-1.10.22.jar
        │       │   ├── classmate-1.5.1.jar
        │       │   ├── commons-beanutils-1.9.4.jar
        │       │   ├── commons-codec-1.11.jar
        │       │   ├── commons-collections-3.2.2.jar
        │       │   ├── commons-digester-2.1.jar
        │       │   ├── commons-logging-1.2.jar
        │       │   ├── commons-validator-1.7.jar
        │       │   ├── dom4j-2.1.3.jar
        │       │   ├── FastInfoset-1.2.15.jar
        │       │   ├── hibernate-commons-annotations-5.1.2.Final.jar
        │       │   ├── hibernate-core-5.4.32.Final.jar
        │       │   ├── httpclient-4.5.13.jar
        │       │   ├── httpcore-4.4.13.jar
        │       │   ├── istack-commons-runtime-3.0.7.jar
        │       │   ├── jackson-annotations-2.13.2.jar
        │       │   ├── jackson-core-2.13.2.jar
        │       │   ├── jackson-databind-2.13.2.jar
        │       │   ├── jandex-2.2.3.Final.jar
        │       │   ├── javassist-3.27.0-GA.jar
        │       │   ├── javax.activation-api-1.2.0.jar
        │       │   ├── javax.persistence-api-2.2.jar
        │       │   ├── jaxb-api-2.3.1.jar
        │       │   ├── jaxb-runtime-2.3.1.jar
        │       │   ├── jboss-logging-3.4.1.Final.jar
        │       │   ├── jboss-transaction-api_1.2_spec-1.1.1.Final.jar
        │       │   ├── jstl-1.2.jar
        │       │   ├── mysql-connector-java-8.0.28.jar
        │       │   ├── protobuf-java-3.11.4.jar
        │       │   ├── spring-security-crypto-5.6.2.jar
        │       │   ├── stax-ex-1.8.jar
        │       │   └── txw2-2.3.1.jar
        │       └── web.xml
        └── Phone_Tracking_System-1.0.0.war
