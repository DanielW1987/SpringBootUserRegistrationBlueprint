# Spring Boot User Registration Blueprint
This repository contains a Spring Boot based blueprint project with user registration and authentication workflow via email.

## Table of contents
1. [ Project Setup ](#project-setup)

2. [ Features ](#features)

    2.1 [ Login ](#login)

    2.2 [ Registration ](#registration)

    2.3 [ Reset password ](#reset-password)

    2.4 [ Backend ](#backend)

3. [ Next features ](#next-features)

<a name="project-setup"></a>
## 1 Project Setup
To setup the project please apply the following steps:
- Clone the project via `git clone https://github.com/Waginator/SpringBootUserRegistrationBlueprint.git`
- Define the data source connection details in file `application.properties`. Define at least the following properties for mysql connection:
    - `spring.datasource.username`
    - `spring.datasource.password`
    - `spring.datasource.url`
- Define the email server connection details in file `application.properties`. Define at least the following properties for the connection:
    - `spring.mail.host`
    - `spring.mail.username`
    - `spring.mail.password`
    - `mail.from.email`
- Define the token secret property `token-secret` also in file `application.properties`.
- Execute the main class `SpringBootUserRegistrationBlueprintApplication`
- Go to your web browser and visit `http://localhost:8080`
- The welcome page looks like the following:
![Welcome](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/welcome.png)

<a name="features"></a>
## 2 Features
<a name="login"></a>
### 2.1 Login
You can log in under the URL `http://localhost:8080/login`. The login form looks like this:


![Login](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/login.png)


There are two example users with the following credentials:

| Email                        | Password       |
| ---------------------------- | -------------- |
| john.doe@example.com         | john.doe       |
| maria.thompson@example.com   | maria.thompson |

<a name="registration"></a>
### 2.3 Registration
...

![Register 1](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/register.png)

...

![Register 2](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/register-2.png)

...

![Register 3](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/register-3.png)

...

![Register 4](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/register-4.png)

<a name="reset-password"></a>
### 2.4 Reset password
...

![Forgot password 1](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/forgot-password.png)

...

![Forgot password 2](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/forgot-password-2.png "Backend")

...

![Forgot password 3](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/forgot-password-3.png "Backend")

<a name="backend"></a>
### 2.5 Backend
...

![Backend](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/backend.png "Backend")

<a name="next-features"></a>
## 3 Next Features
