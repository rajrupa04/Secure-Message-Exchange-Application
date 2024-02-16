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
