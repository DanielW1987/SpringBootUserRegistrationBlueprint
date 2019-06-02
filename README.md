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
You can log in via the URL `http://localhost:8080/login`. The login form looks like this:

![Login](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/login.png)

There are two example users with the following credentials:

| Email                        | Password       |
| ---------------------------- | -------------- |
| john.doe@example.com         | john.doe       |
| maria.thompson@example.com   | maria.thompson |

<a name="registration"></a>
### 2.2 Registration
You can register via the URL `http://localhost:8080/register`. The register form looks like this:

![Register 2](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/register-2.png)

The register form provides full server-side validation. The validation deals with the following possible incorrect input values:
- empty values (also handle blanks and null values)
- password strength (at least 8 characters)
- email syntax
- emil already registered
- `Password` must match `Verify password`

The validation messages are displayed as follows:

![Register 3](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/register-3.png)

After submitting the registration form you will receive an email asking you to confirm your registration:

![Register 4](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/register-4.png)

The confirmation workflows handle the following possible issues:
- token is expired 
- token was not found (already confirmed or invalid token)

<a name="reset-password"></a>
### 2.3 Reset password
...

![Forgot password 1](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/forgot-password.png)

...

![Forgot password 2](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/forgot-password-2.png "Backend")

...

![Forgot password 3](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/forgot-password-3.png "Backend")

<a name="backend"></a>
### 2.4 Backend
After logging in, you will be taken to a simple admin area that displays all created users. You can also log out using the link in the upper right corner.

![Backend](https://github.com/Waginator/SpringBootUserRegistrationBlueprint/blob/master/readme-data/backend.png "Backend")

<a name="next-features"></a>
## 3 Next Features
The upcoming features are:
- A small frontend demo with better integration of the registration and login workflow
- Distinction between frontend and backend users (user roles)
- Create backend and frontend users via admin area
- Remember me functionality
- See date and time of the last sucessful login
- Logging of all failed login attempts for an account
- Logon delay or complete deactivation of an account if too many failed logon attempts are made
- Immediate logout of a user if his or her account is deactivated but the session is still active
- Configuration of a password policy
