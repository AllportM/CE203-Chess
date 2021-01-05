# CE203-Chess
Two player chess game featuring AI player  
  
This was my second year project for CE203 - Application programming (Java module) in which we were tasked to develop a basic 2d shape based game such as pong, minesweeper etc; I chose to make Chess. The main tasks were to have a main user interface in which shapes could be moved, translated, or transformed, a Game Object / Shape superclass to be used to which sub classes implemented/realized abstract methods, interfaces to allow user input through keyboard and mouse, and the game keeping track of user scores whilst being able to display the top five scored of all time.  
  
I am proud of this assignment, which took under 2 months to create. I'd implemented an AI player using a state-space representation model with the minimax algorithm for efficiency and heurustics. I'd designed each piece to utilize a shape class to allow for their drawing, which made use of the composite design pattern for shape grouping, and had implemented a composite pattern for GridBag Layout (which we know is very finnicky and messy when written in code).  
  
There is one main bug in which the king as allowed to move into a check position, which shouldn't be allowed and broke the ai. It is only in one specific move though which made it nigh on impossible to track down in time for assignment completion. To be fair, although I'm proud of the design for the pieces, shapes, file streams etc; the code for the main logic of the game and the pieces moves is rather complex and could do with re-factoring / re-writing, it got a little spaghettified with the game logic. 
  
I attained a 100% grade for this assignment, my first 100% grade attained.
