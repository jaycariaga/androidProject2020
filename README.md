# androidProject2020
<h1>Authors: Jason Cariaga, Adam Piziak</h1>
This project is meant to tackle MongoDB with mobile app database integration using android studio.  
The application is intended on organizing data created from teams of users with an easy way of accessing specific sets of data without hassle.

<h2> Approach </h2>
<br />
Develop secure http APIs with mongoDB and Node.js and run it thru the Android HTTP client. This should be able to handle incoming queries safely and securely from users.
<br />
<h2>Important packages used</h2>
1. Retrofit2 - REST client for Android and good with retrieving/sending JSON data
2. RxJava2 - Provides faster processing of API calls with methods that handle low-level threading and asynchronization
3. RecyclerView - Provides method of displaying arrays of JSON data as a ListView (most important for our UI display)


<br />
<h1> Work Progress Documentation/Steps taken </h1>
<br />
1. Created GUI of Login/Sign up pages with ensurity of user uniqueness
2. Secure password keeping using Crypto hashkeys: created within NodeJS server 
3. Developed a Homepage that allows the user to join or create a group/team
4. Developed a navigating bar for options: messaging services, search widget, team main menu (meant as a settings view for the user or a control panel for the admin of the team), a data search panel, and lastly a task/scheduler widget with a submission sub-view 
5. Widgets Completed: Messaging (v1)
6. <b>Widgets TODO: Data, Search, Team Main Menu (Settings), and Task/Schedulers </b>
