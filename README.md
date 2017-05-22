<p align="center">
  <img src="https://raw.githubusercontent.com/Breinify/brein-api-library-java/master/documentation/img/logo.png" alt="Breinify API Java Library" width="250">
</p>

<p align="center">
Breinify's DigitalDNA API puts dynamic behavior-based, people-driven data right at your fingertips.
</p>

### Step By Step Introduction

#### What is Breinify's DigitialDNA

Breinify's DigitalDNA API puts dynamic behavior-based, people-driven data right at your fingertips. We believe that in many situations, a critical component of a great user experience is personalization. With all the data available on the web it should be easy to provide a unique experience to every visitor, and yet, sometimes you may find yourself wondering why it is so difficult.

Thanks to **Breinify's DigitalDNA** you are now able to adapt your online presence to your visitors needs and **provide a unique experience**. Let's walk step-by-step through a simple example.

### Quick start

#### Step 1: Download the Library

Download the Library from JCenter Repository <<more details to come>>


#### Step 2: Integrate the library

Integrate the Library into your Android project. 


#### Step 3: Configure the library

In order to use the library you need a valid API-key, which you can get for free at [https://www.breinify.com](https://www.breinify.com). In this example, we assume you have the following api-key:

**772A-47D7-93A3-4EA9-9D73-85B9-479B-16C6**

```Java
// this is the valid api-key
final String VALID_API_KEY = "772A-47D7-93A3-4EA9-9D73-85B9-479B-16C6";
final String VALID_SECRET = "====erwer43gfd8((88("

// this is the URL of the Breinify service
final String serviceEndpoint = "https://api.breinify.com";

// create the configuration object
final BreinConfig breinConfig = new BreinConfig(VALID_API_KEY, VALID_SECRET);

// set the configuration for later usage
Breinify.setConfig(breinConfig);

```

The Breinify class is now configured with a valid configuration object.


#### Step 4: Start using the library

##### Placing activity triggers

The engine powering the DigitalDNA API provides two endpoints. The first endpoint is used to inform the engine about the activities performed by visitors of your site. The activities are used to understand the user's current interest and infer the intent. It becomes more and more accurate across different users and verticals as more activities are collected. It should be noted, that any personal information is not stored within the engine, thus each individual's privacy is well protected. The engine understands several different activities performed by a user, e.g., landing, login, search, item selection, or logout.

The engine is informed of an activity by executing *Breinify.activity(...)*. 

```Java
// create a user you are interested in 
final BreinUser breinUser = new BreinUser("user.anywhere@email.com")
         .setFirstName("User")
         .setLastName("Anyhere");
               
// invoke an activity noting that the user has logged in
Breinify.activity(breinUser, 
         BreinActivityType.LOGIN,
         BreinCategoryType.HOME, 
         "Login-Description", 
         false);

```

That's it! 

### Further links
To understand all the capabilities of Breinify's DigitalDNA API, take a look at:

* [Breinify's Website](https://www.breinify.com).
