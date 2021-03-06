# GRADE: 59/64 

## Functional and implementation requirements: 32/36 points total

Earned|Possible|Requirement | Comments
------|--------|------------|----------
1|1| Add two new menu items to the Edit menu called Group and Ungroup.
4|4| When the Group menu item is chosen, all selected note bars are grouped into one gesture. A gesture behaves as if it is just one note bar with a complex sound to it.  If nothing is selected when the Group menu item is chosen, nothing happens.
1|2| Selecting any note in a gesture causes the same thing to happen to all the others in the gesture. | _See [Issue #10](https://github.com/WhitmanSWDesignSpring2018/project-5-8-teamjuan/issues/10#issuecomment-484272792)_
2|2| Unselecting any note in a gesture with control-click causes the same thing to happen to all the others in the gesture.
2|2| Drag-selecting over a gesture consistently selects or unselects all the notes in the gesture.
2|4| Dragging note bars in a gesture moves or stretches all notes in the gesture. | _I can move notes but not stretch them. See also **Stretch Goal** below._
3|3| Gestures are indicated visually by a thin black dotted line around the group of note bars. 
1|1| A selected gesture is indicated using a style similar to that of a selected note bar.
4|4| Gestures can include note bars or other gestures (that is, gestures can be nested).
3|4| When the Ungroup menu item is chosen, the selected gesture is ungrouped. Note that nested inner gestures are not ungrouped; you have to ungroup the nested gestures separately after ungrouping the outer gesture.  If no groups are selected when Ungroup is chosen, nothing happens. | _If multiple gestures are selected, only one is ungrouped._
1|1| After selecting the Group menu item, the new gesture becomes the only selected item. 
2|2| After choosing Ungroup, all the inner notebars and gestures of the ungrouped gesture are selected.

### "Stretch goal" - 1 point

Earned|Possible|Requirement | Comments
------|--------|------------|----------
1|0| When the right edge of a gesture is grabbed and stretched left or right, the duration and offset of everything in the gesture should stretch proportionally. | _Duration stretches, while offset remains the same._

### No regressions - 1.5/2 points

Earned|Possible|Requirement | Comments
------|--------|------------|----------
3.5|4| All prior requirements are met, unless they have been superseded by new requirements. | _Platform-independent keyboard shortcuts_

### Release tag - 2/2 points

Earned|Possible|Requirement | Comments
------|--------|------------|----------
2|2| The release is tagged as project-5-release.

## Reflection - 11.5/12 points

For project 5 and beyond, I will expect your discussion of design and elegance to refer explicitly to design principles we have discussed in class.

Earned|Possible|Requirement | Comments
------|--------|------------|----------
3|3| UML | _Thorough!_
2|2| Design overview
2|2| What is elegant?
2|2| What is not elegant?
0.5|1| Velocity | _I'm impressed that you completed the project requirements so quickly!_
1|1| Collaboration statement
1|1| Team retrospective

## Elegance - 11/12 points

I looked at your UML diagram to assess the elegance of your class design.  You will get a grade for class design starting with Project 6.
I spot-checked a new class (MoveableRect) and a modified class (Note) to assess method-level design and self-explanatory code.

Earned|Possible|Requirement | Comments
------|--------|------------|----------
0|0| Class design | _TuneComposer is the big issue. In ```MoveableRect``` and ```Note```, I like that you were careful to make fields private. See also (this issue)[https://github.com/WhitmanSWDesignSpring2018/project-5-8-teamjuan/issues/21#issue-434508787]_
8|8| Method design | _Nice short methods_
3|4| Self-explanatory code | _Search for GitHub Issues containing the word "self-explanatory"_
