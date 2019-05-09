PROJECT 7 REFLECTION

For project 7, we added TuneParser, StringToXMLConverter, and ClipboardManager as classes to implement the cut/copy/paste behavior and the open/save file functionality. 

Seeing that we did not get project 6 feedback, we spent some time addressing the issues noted in our respository. Also, like previous projects, our first meeting session consisted of refactoring. This time around, we added a ClickHandler class to deal with all mouse events to reduce the cross-class dependencies and to minimize coupling. Notably, promptUnsaved() in the FileManager class was pretty complicated as well when it came to some of our new implementation. 

As for elegance, we believe that our TuneParser is particuarly elegant since it deals with all the parsing in a single class, rather than interspering the responsibilites throughout all the other classes. This follows the Single Responsibility Principle. By thinking ahead in prior projects, we also benefitted partially from actively trying to adhere to the Open-Closed Principle -- some of our classes did not have an additional modifications in the methods, but rather, extensions, so that we could implement what was needed. Technical debt is still very real, and we did run into issues. With a lot of various adjustments, we were able to implement the new features. While coding, we also made sure to consistently name classes in a style that mirrored our previous projects; we also double checked all the parameters and their order. 

As for planning poker -- we estimated 72 story points. This again meant an average velocity of 2 for an estimated 10.1 hours of work. This project took around 16 hours. Cut/copy/paste had various small bugs that we didn't anticipate which added some time, and we did except file handling to take up a lot of our time. Thankfully, our parser was functioning properly from the get go and we were able to allocate our time to implementing the rest of the expected functionality without too many time constraints. 

Below are our planning poker breakdowns:

    1. Menu changes
    J J J J 
    0 0 0 0 
    
    2. Cut/Copy
    2 2 3 2 
    1 hour 1 hour 1 hour 1.5 hours 1 hour
    
    3 Paste
    3 3 3 3
    1.5 hours 1.5 hours 1.5 hours 1.5 hours
    
    4. About dialog box
    A A 2 A
    .5 hours .5 hours 1 hour .5 hours
    
    5. File open/save/save as
    5 5 5 5 
    2.5 hours 2.5 hours 2.5 hours 2.5 hours
    
    6. Release preparataion
    2 2 2 2
    1 hour 1 hour 1 hour 1 hour
    
    7. Refactoring
    3 5 5 5 
    1.5 hours 2.5 hours 2.5 hours 2.5 hours
    
As for working as team, we followed what worked: refactoring first, and then working as a group to talk through concepts and plans. We programmed like we usually did for the most part -- that is, huddled around one main computer and one main typer/programmer. We worked through a rota of who typed, and those who were not on typing duty were on other computers searching and trying things as we worked on the project. 
We're excited to finish working on TuneComposer next time -- all of our team strategies have worked well so far so we are hoping to keep with it for just a few more days! 
