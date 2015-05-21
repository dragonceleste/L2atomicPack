/*
 * Copyright (C) 2004-2015 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package custom.HappyHour;

import ai.npc.AbstractNpcAI;

import com.l2jserver.gameserver.custom.HappyHourEvent;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.ListenerRegisterType;
import com.l2jserver.gameserver.model.events.annotations.RegisterEvent;
import com.l2jserver.gameserver.model.events.annotations.RegisterType;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerLogin;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.util.Broadcast;

/**
 * @author Eze
 */
public class HappyHour extends AbstractNpcAI
{
	private HappyHour()
	{
		super(HappyHour.class.getSimpleName(), "custom");
		
		startQuestTimer("HHExpSp", 1 * 60000, null, null);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equals("HHExpSp"))
		{
			HappyHourEvent.getInstance().setHappyHour(true, false);
			Broadcast.toAllOnlinePlayers("La Hora Feliz comienza Exp y Sp se aumentaran durante la proxima hora");
			startQuestTimer("FinishExpSp", 60 * 60000, null, null);
		}
		else if (event.equals("FinishExpSp"))
		{
			HappyHourEvent.getInstance().setHappyHour(false, false);
			Broadcast.toAllOnlinePlayers("La Hora Feliz termina Exp y Sp vuelve a su estado original");
			startQuestTimer("HHDropSpoil", 120 * 60000, null, null);
		}
		else if (event.equals("HHDropSpoil"))
		{
			HappyHourEvent.getInstance().setHappyHour(false, true);
			Broadcast.toAllOnlinePlayers("La Hora Feliz comienza Drop y Spoil se aumentaran durante la proxima hora");
			startQuestTimer("FinishDropSpoil", 60 * 60000, null, null);
		}
		else if (event.equals("FinishDropSpoil"))
		{
			HappyHourEvent.getInstance().setHappyHour(false, false);
			Broadcast.toAllOnlinePlayers("La Hora Feliz termina Drop y Spoil vuelve a su estado original");
			startQuestTimer("HHExpSp", 120 * 60000, null, null);
		}
		
		HappyHourEvent.getInstance().inHappyHour();
		return super.onAdvEvent(event, npc, player);
	}
	
	@RegisterEvent(EventType.ON_PLAYER_LOGIN)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void playerLogin(OnPlayerLogin event)
	{
		L2PcInstance player = event.getActiveChar();
		if (HappyHourEvent.getInstance().getHappyHourEvent().equals("ExpSp"))
		{
			broadcastToPlayerLogin(player, "La Hora Feliz esta en progreso");
		}
	}
	
	private void broadcastToPlayerLogin(L2PcInstance player, String msg)
	{
		player.sendPacket(new CreatureSay(0, Say2.ANNOUNCEMENT, "", msg));
	}
	
	public static void main(String[] args)
	{
		new HappyHour();
	}
}
