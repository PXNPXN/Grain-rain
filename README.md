# Grain-rain-app
It's an app for people to relax and be more productive.It provides quiet, pure music, as well as listening to 
online programs and radio stations.In addition, the memo function is also a big feature of it, can add alarm, 
remind people.To improve work efficiency.

We use the API interface, which is the test version provided by dragonfly FM, 
URL:https://open.qingting.fm/documents/168

In the project, we not only used the Activity but also Fragment and Service components.Use a Listview, Recycleview 
and Cardview, ImageButton, Seekbar view controls. The last Activity to play music is written using the kotlin 
framework, which feels shorter in code than Java and makes the controls easier to use.This project also USES some 
android-provided dialogs and custom dialogs.

Our theme function is after the local login, play local music and download our network music, network programs and 
radio broadcast, memo alarm reminder.

Function 1:

    Login-Functio:
    
    Login function is a simple local login, users can register new users, change the password, data is saved in SQLite.A third party login could not be made due to time constraints.
  
Function 2:

    Play local music and download online music:
    
    1. white noise function(Homepage): you can play built-in music and switch music by sliding the page. if you want to 
    pause or replay music, just click the pause/replay sign in the middle of this page. you can also download white noise 
    music by clicking "+" sign at the top right of this page.
    
Function 3:

    Network programs and radio broadcasts:
    We use the interface provided by dragonfly FM to read the data and put it into the ListView.Play network music and 
    radio through the player, and use the seekbar control to display program duration.And get the album image to animate 
    the rotation.
    
Function 4:

    Memo function:
    
    Users can dynamically add and delete memos. When adding, a content will be input, which can be modified later, and 
    then set an alarm for the memo. We used AlarmManage and DatePickerDialog, TimePickerDialog, which come with Android 
    system, to ring the alarm in the form of Service broadcast.The data in the memo is also stored in the SQLite library.
    The ability to add an alarm to multiple memos is distinguished by the id of the memo.
    
So that's what we did with the app.

Group member：

 Name           CQU_Number
 
 Xiaonan Peng    20171627
 
 Shu Mo          20171592

    
