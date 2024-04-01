# Term Project : Secure message exchange application

## What will the application do?
This application will be able to carry out the following tasks:
- Allow a user to **log in** (or **create** a new user if required)
- Create a personalised **hub** for each user, containing all the information they require at a glance
- Provide users the opportunity to send/receive **encrypted** messages
- Create a **secret key** every time a user sends an encrypted message so that the rightful recipient can decrypt the message using the key
- Provide **alerts** whenever a user has more than one unread message
- Maintain an internal **hierarchy** based on the importance of a message (emergency, urgent or regular)

## Who will use this application?
This application can be used by users working in fields where confidential 
information has to be exchanged, and secure communications is a necessity. 
Some real-life scenarios include employees working in the healthcare industry, 
law enforcement or legal professionals.

## Why this application, specifically?
Being inspired by franchises like _Mission:Impossible_ and _James Bond_ as a young teenager,
I expressed a strong interest towards the idea of utmost security and confidentiality in communication,
especially in high-stakes scenarios. I was exposed to multiple examples of how important 
secure communications are, and how quickly things can go wrong if there is a security breach.
I also quickly realised that the requirement of such technologies
extends beyond fiction - for there are multiple real life uses of a secure message transmitting 
application, as stated above. By leveraging the existing concepts from such popular movies, 
I believe that I can create a compelling application that can address real-world concerns.

## User stories:
- As a user, I want to be able to send an **arbitrary** number of **encrypted** messages to another user who has an account.
- As a user, I want to be able to be able to **view** my personalised hub when I log in.
- As a user, I should be able to **add/modify** information in my hub (eg: notes, reminders, etc.)
- As a user, I want to be able to decide how **urgent** a message is, when I send a message.
- As a user, I want to be able to set **emergency** contacts, and the system should be able to alert them with 
urgent messages if needed.
- As a user, I want to be able to add a **new message** and specify the **recipient** and **urgency** for each message.
- As a user, I want each of my messages to have a **shared key** accessible to only the sender and recipient. 
- As a user, I want to be able to **organize** my messages into folders. 
- As a user, I want to be able to **view** how many **unread** messages I have, and how urgent they are.
- As a user, when I select the **quit** option from the **hub menu**, I want to be reminded to **save** the information in my hub to file and have the **option** to do so or not.
- As a user, I want to have the **option** to **reload** the saved hub when I start the application.

## Instructions for Grader:


- You can generate the first required action related to the user story "adding multiple Xs to a Y" when your Hub loads. In each of the **Notes**, **Reminders** and **Contact List** tabs, you can add a new entry by pressing the respective **"Add"** buttons. You can also send a message to another user by clicking on the **Send new message** button in the **Messages** tab (thus adding another X to a Y).
  

- You can generate the second required action related to the user story "adding multiple Xs to a Y" in the **Notes** panel in the Hub, where you can **search** for a note based on the note content. As an alternative, you also have the option to **delete** a note based on the note ID.  


- You can locate the first visual component when the application is first ran (by running the **MainAppUI** class), alongside the prompt asking the user to enter if they are a new user or not. In addition, there is another visual component displayed when the user logs in successfully, as well as when the user saves their data to file.  


- You can **save** the state of my application after adding changes to the hub by clicking the **"Save"** button at the bottom of the panel. If you wish to not save your changes, you can choose to quit by clicking the **"Quit without saving"** button


- You can **reload** the state of my application just after logging in successfully, if you are an existing user who has a saved hub. When the prompt to reload the hub from file is presented, clicking **yes** will allow you to reload the hub from file. Clicking **no** will generate a new empty hub for you. 

## Phase 4: Task 2
- Added a new user with username: florence123.  
- Added a new note with note ID: 4 and note content: new note 4!.  
- Added a new note with note ID: 1 and note content: Today is a sunny day!.  
- Removed the note with note ID: 1.  
- Added a new reminder with reminder date: 2024-04-19 and reminder content: CPSC 210 final.  
- Sent a message of urgency level REGULAR from florence123 to john_doe.  
- Added a new notification with urgency level: REGULAR and content: You have 1 new message of type: REGULAR to recipient's hub.