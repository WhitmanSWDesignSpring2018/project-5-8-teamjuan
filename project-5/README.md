Team Juan - Madi Crowley, Melissa Kohl, Michelle Zhang, and Sage Levin

PROJECT 6 REFLECTION 

For project 6, we decided to add a HistoryManager class so that we could track each action - that is, adding a note, moving them on the Pane, and grouping them, etc.. In doing so, we used a stack to store all the actions so that we can easily undo and redo actions, and the creation of this class makes it so that we do not have to modify any of our other classes to deal with modifying other classes. 

Some of our original implementation has changed. We spent our first time meeting refactoring what we had after we submitted Project 5; we made NoteHandler so that we could move all the handler methods into static NoteHandler. Sage notably removed the TuneComposer's dependency NoteHandler's variables, following the Law of Demeter. After doing our code review, we resolved a majority of the issues noted on GitHub: keyboard shortcuts being platform-independent, grouping behavior of gestures, refactoring, renaming methods and variables, dealing with the the Gesture rectangle getting out-of-synce, and the issues relating to KeyFrame upon launching the program.

Our strategy to add a HistoryManager class, as noted above, was so that we did not have to modify anything; rather, we extended functionality through the addition of this class, which does adhere to SOLID programming principles. However, in practice, this has issues. In fact, the actuality, there is some pretty ugly code. There is a lot of cross-class dependency, and we are not keeping track of the changes - we are deleting and re-creating things as we go along. We looked ahead at Project 7 and realized that some of our strategies regarding Project 6 will have to completely change. 

As for Planning Poker, we tried. We estimated 60 story points from breaking down the expectations for the project into 6 stories, which meant an estimated velocity of 2 for about 10 hours of work. In actuality, the project took about 12 hours. We did not anticipate undo/redo being so complicated when it came to tracking where they are on the Pane. Since we did not do planning poker for the previous project, we can't really compare what we estimated. 

Below are our planning poker breakdowns: 

        1. Updating the menu
        0 0 0 0
        J J J J

        2. Undo single action 
        1.5 hours 1.5 hours 13 hours 2.5 hours
        3 3 K 5

        3. Repeated undo 
        1.5 hours 1.5 hours 1 hour 30 minutes
        3 3 2 A 

        4. Redo
        1 hour 1 hour 30 minutes 30 minutes
        2 2 A A 

        5. Disable when appropriate
        30 minutes 30 minutes 1.5 hours 2.5 hours 
        A A 3 5

        6. Disabling other menu items when appropriate 
        1.5 1.5 1.5 30 
        3 3 3 A

As a team, we spent the first time re-factoring with Melissa at the computer. The next time we met, Michelle and Madi worked on figuring out how to implement enabling/disabling menu item options as well as updating the menu in SceneBuilder as well as dealing with fx:ids in FXML so that menu items could be accessed and enabled/disabled, and Sage and Melissa worked on writing HistoryManager.java so that undo/redo could be later implemented. After that, we worked together at a single computer, working to merge the ideas and make sure that undo/redo could account for all actions on the Pane. 

For the next time, we'd like to improve the elegance and quality of the code we wrote for Project 6, as well as resolve some other issues. Refactoring is definitely in the plan. Next time we also hope to benefit more from Planning Poker. We aim to also continue meeting on Tuesdays and Thursdays 4-6. 
