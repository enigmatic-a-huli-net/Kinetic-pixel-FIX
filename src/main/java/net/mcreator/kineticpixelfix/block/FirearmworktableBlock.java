package net.mcreator.kineticpixelfix.block;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;

import com.mojang.serialization.MapCodec;

import net.mcreator.kineticpixelfix.block.entity.FirearmworktableBlockEntity;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Containers;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.mcreator.kineticpixelfix.world.inventory.FirearmworktableguiMenu;
import net.mcreator.kineticpixelfix.init.KineticPixelFixModBlockEntities;

import io.netty.buffer.Unpooled;

@EventBusSubscriber(modid = net.mcreator.kineticpixelfix.KineticPixelFixMod.MODID)
public class FirearmworktableBlock extends BaseEntityBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public static final MapCodec<FirearmworktableBlock> CODEC = simpleCodec(properties -> new FirearmworktableBlock());

@Override
protected MapCodec<? extends BaseEntityBlock> codec() {
	return CODEC;
}

	public FirearmworktableBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(1f, 10f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false).instrument(NoteBlockInstrument.BIT));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	/** Регистрируем fluid capability для блок-энтити, чтобы внешние трубы (Create и т.д.) могли заливать лаву. */
	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
			Capabilities.FluidHandler.BLOCK,
			KineticPixelFixModBlockEntities.FIREARMWORKTABLE.get(),
			(be, side) -> be.getLavaTank()
		);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public InteractionResult useWithoutItem(BlockState blockstate, Level world, BlockPos pos, Player entity, BlockHitResult hit) {
		super.useWithoutItem(blockstate, world, pos, entity, hit);
		if (entity instanceof ServerPlayer player) {
			player.openMenu(new MenuProvider() {
				@Override
				public Component getDisplayName() {
					return Component.literal("Firearm worktable");
				}

				@Override
				public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
					return new FirearmworktableguiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
				}
			}, pos);
		}
		return InteractionResult.SUCCESS;
	}
	@Override
public RenderShape getRenderShape(BlockState state) {
	return RenderShape.MODEL;
}

@Nullable
@Override
public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
	return new FirearmworktableBlockEntity(pos, state);
}
@Override
public <T extends BlockEntity> BlockEntityTicker<T> getTicker(net.minecraft.world.level.Level level, BlockState state, BlockEntityType<T> type) {
	return (lvl, pos, st, be) -> {
		if (be instanceof net.mcreator.kineticpixelfix.block.entity.FirearmworktableBlockEntity firearmWorktable) {
			net.mcreator.kineticpixelfix.block.entity.FirearmworktableBlockEntity.tick(lvl, pos, st, firearmWorktable);
		}
	};
}

@Override
public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
	if (!state.is(newState.getBlock())) {
		BlockEntity blockEntity = level.getBlockEntity(pos);

		if (blockEntity instanceof FirearmworktableBlockEntity firearmWorktable) {
			for (int i = 0; i < firearmWorktable.getInventory().getSlots(); i++) {
				ItemStack stack = firearmWorktable.getInventory().getStackInSlot(i);

				if (!stack.isEmpty()) {
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack.copy());
					firearmWorktable.getInventory().setStackInSlot(i, ItemStack.EMPTY);
				}
			}
		}
	}

	super.onRemove(state, level, pos, newState, movedByPiston);
}

}
