# GRADE: 70/80 

## Original composition - 4/6 points

| Points Earned | Points Possible | Requirement | Comments
|--------------:|----------------:|-------------|---------
|1|1| A composition is presented.
|0|1| The composition is original.
|3|4| The composition is artistic and/or demonstrates the features of your software. | _Thanks for pointing out that the up/down octave functions were important to constructing your composition. I'm not sure I would have gotten that on my own._

## Demo - 6/6 points

| Points Earned | Points Possible | Requirement | Comments
|--------------:|----------------:|-------------|---------
|4|4| Each new feature was justified and demonstrated.
|1|1| The composition was played.
|1|1| All team members were included.

## User manual - 6/6 points

| Points Earned | Points Possible | Requirement | Comments
|--------------:|----------------:|-------------|---------
|2|2| There is a file named ```manual.md``` in the top-level directory. | _I found it in the ```project-8``` directory._
|2|2| It describes each new feature added in Project 8.
|2|2| It explains how to use each new feature.

## Feature selection - 8/12 points

One difficult enhancement may be worth several easier enhancements.

_Your enhancements were not particularly ambitious, but also not trivial. I'm glad you had fun with it.  It would have been interesting to see some of the more challenging transpositions or a more structured random composition generator. I do appreciate that you allocated significant time to addressing technical debt and I'm giving you some credit for that._

_C# mode is pretty funny._

_I appreciate that you prompt a save before generating a random composition. That is valuable for user control and freedom._

## Execution -  28/28 points

### Implementation of new features - 20/20 points

* All new features behave as described in the user manual.  If the description in the user manual is incomplete, then the behavior follows standard conventions and is otherwise unsurprising.
* There are no uncaught exceptions from new features.
* There is no debugging output from new features.

### No regressions - 8/8 points

All prior requirements are met, unless they have been superseded by new requirements or are documented by the team as known bugs for this iteration.

## Release - 2/2 points

| Pts Earned | Pts Possible | Requirement | Comments
|-----------:|-------------:|-------------|---------
| 1 | 1 | The release is tagged as ```project-8-release```.
| 1 | 1 | Documentation is in the ```project-8``` directory.

## Reflection and elegance - 16/20 points

| Pts Earned | Pts Possible | Requirement | Comments
|-----------:|-------------:|-------------|---------
| 4 | 4 | UML diagram is accurate and complete.
| 3 | 4 | New classes/methods are reasonably self-explanatory. | _It's surprising to see an empty file (```Transpose.java```) in your repository. It's also surprising to see that the ```RandomComposition``` class needs a ```Pane``` to operate on. That seems like an odd choice - an inappropriately concrete dependency - and it's not discussed in the non-existent Javadoc for the class._
| 2 | 2 | Design overview addresses changes from Project 7 in general.
| 2 | 2 | Design overview addresses the implementation of each new feature.
| 3 | 6 | Assessment of what is elegant and what is not thoughtfully addresses object-oriented design principles. | _You mention some principles; you could have been more specific about how you applied them. I would also note that by moving mouse handlers out of ```Note``` and ```Gesture```, you brought your code closer to the ideal of the MVC architecture._
| 1 | 1 | Velocity is presented. 
| 1 | 1 | Team retrospective is presented. | _I'm glad you ended on a triumphant note. I look forward to reading your individual reflections!_
