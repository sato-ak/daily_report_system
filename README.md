# daily_report_system
# Name
 
”daily_report_system" is a program for managing daily reports with Java.

# DEMO
* You can write your daily report.<br>
* You can read all employees' daily report.<br>
* You can manage an employee information.(administrator only) 

### Top page
<img src="Toppage.png" width="60%">

### Create a report page
<img src="Create.png" width="30%">


# Features
Your reports are listed on a calender. <br>
Click report title on a calender and you'll jump to the detail page.

<img src="ToppageToDetail.gif" width="60%">

# Requirement

* Java11
* Tomcat9
* MySQL
* pom.xml

|  library          |        version       |
|-----------------|---------------|
|java.servlet-api|4.0.1|
|mysql-connector-java|8.0.23|
|hibernate-core|5.4.28.Final|
|lombok|1.18.16|
|taglibs-standard-impl|1.2.5|
|javax.servlet.jsp.jstl-api|1.2.1|




# Installation
---
git clone https://github.com/sato-ak/daily_report_system.git
---

# Usage
Access to the login page and input your name and password to start.

Login page
http://localhost:8080/daily_report_system/?action=Auth&command=showLogin

# Author

S.Akiyama

# License
”daily_report_system" is under [MIT license](https://en.wikipedia.org/wiki/MIT_License).
