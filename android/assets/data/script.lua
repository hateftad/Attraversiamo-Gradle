


function init(levelCfg)
	
	nrOfPlayers = levelCfg:getNrOfPlayers()
	finishFacingLeft = levelCfg:finishLeft()
	hasPortal = levelCfg:hasPortal()	
	first = 0
	second = 0
	inGoal = false
	loadNextLevel = false;
end

function updateLights(light)

  	if light:getName() == "portalLight" then
  		local a = light:getAlpha();
      if inGoal == false then
      	if a >= 1 then
      		 light:setColor("Red")
      		 inc = -0.008
      	elseif a < 0.1 then
      		 inc = 0.008
      	end
      	light:setAlpha(a + inc)
      else 
      	light:setAlpha(1)
      	light:setColor("Green")
      end
  			
    end    

end

function updatePlayer(player, touch)

		if player:getPlayerNr() == "playerOne" then
			 first = touch:endReached()
		else
			 second = touch:endReached()
		end
		
		local result = first + second
		if result == nrOfPlayers then
			 inGoal = true;
			 reachedTheEnd = true
		end
		
		if inGoal == true then 
			 player:setFacingLeft(finishFacingLeft)	
		end
end

function updateParticles(particle)

		if particle:isSetToStart() == true then
			 particle:start();
			 particle:setToStart(false)
		end
		
		if particle:isCompleted() == true then
			 inGoal = false
			 loadNextLevel = true
		end
end

