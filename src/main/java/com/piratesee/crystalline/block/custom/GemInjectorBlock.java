package com.piratesee.crystalline.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class GemInjectorBlock extends Block {

	public GemInjectorBlock(Properties Properties) {
		super(Properties);
		
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
	}
	
	public static final DirectionProperty FACING = FaceAttachedHorizontalDirectionalBlock.FACING;
		
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> Builder) {
		Builder.add(FACING);
		super.createBlockStateDefinition(Builder);
	}

	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
	
	@Override
	public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
		return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
	}
}
