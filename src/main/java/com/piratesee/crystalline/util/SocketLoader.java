package com.piratesee.crystalline.util;

import java.util.Arrays;
import java.util.Map;

import com.piratesee.crystalline.SocketGems;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class SocketLoader {
    private static final Map<Ingredient, Integer> stats = Map.copyOf(SocketGems.stats);

    public static int tester(ItemStack item, Level level){


        int charge = 0;

        var chargeValue = stats.get(item);
        if (chargeValue != null){
        	System.out.print(chargeValue);
            return chargeValue;
        }

        return charge;
    }
	
}
