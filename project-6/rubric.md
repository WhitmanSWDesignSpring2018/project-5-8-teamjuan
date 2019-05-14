# GRADE: 48/48

## Functional and implementation requirements: 28/28 points total

### Menu: 2/2 points

Earned|Possible|Requirement  | Comments
------|--------|-------------|-
1|1|Add **Undo** and **Redo** menu items to the **Edit** menu. The **Undo** menu item should have a keyboard equivalent of Ctrl/Cmd-Z. The **Redo** menu item should have a keyboard equivalent Ctrl/Cmd-Shift-Z.
1|1|Put a separator between them and the other **Edit** menu items.
0|0|**Undo** is disabled when the program starts and when all actions have been undone; it is enabled when the first action is taken. | _Addressed in the Project 7 rubric_
0|0|**Redo** is disabled unless there is an action to redo.
0|0|**Group** is disabled when there are fewer than two items selected.
0|0|**Ungroup** is disabled when the selection is not (or does not contain) a gesture.
0|0|**Delete** is disabled when nothing is selected.
0|0|**Play** is disabled when there are no notes.
0|0|**Stop** is disabled when the composition is not playing.

### Undo/redo operations: 18/18 points

1 pt each|Undo|Redo
---------|----|----
Add      |1|1
Select   |1|1
Delete   |1|1
Move     |1|1
Stretch  |1|1
Group    |1|1
Ungroup  |1|1

Earned|Possible|Requirement
------|--------|------------
4|4|If note bars are deleted, edited, or selected as a group, for example, by using the **Select All** menu item, then an undo operation should affect all of the note bars.

### Undo/redo stack: 8/8 points

Earned|Possible|Requirement
------|--------|------------
4|4|The user should be allowed to undo actions repeatedly, all the way back to the original blank composition pane when the application was first started.
2|2|Similarly, if a sequence of undo actions was performed, then the user should be allowed to redo the whole sequence.
2|2|Taking a new action should clear the redo stack.

### No regressions - 0/0 points

All prior requirements are met, unless they have been superseded by new requirements.  (I will not replicate points taken off in Project 5.) _Not assessed to avoid double jeopardy_

## Release - 2/2 points
Earned|Possible|Requirement
------|--------|------------
2|2| The release is tagged as project-6-release.

## Reflection and elegance - 18/18 points

As promised, I am not looking closely at your code for this iteration, only spot-checking.

* 4/4 UML diagram is accurate and complete.
* 3/3 Design overview addresses strategy for Undo/Redo.
* 3/3 Design overview addresses strategy for disabling/enabling menu items.
* 6/6 Assessment of what is elegant and what is not thoughtfully addresses object-oriented design principles. | _I like that you thought about implications for change (project 7 requiremnets)._
* 1/1 Velocity is presented. 
* 1/1 Team retrospective is presented. | _Thanks for your thoughts! This was a really good reflection - sorry I did not get you feedback earlier._
